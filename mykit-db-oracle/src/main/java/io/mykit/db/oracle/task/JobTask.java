/**
 * Copyright 2020-9999 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.mykit.db.oracle.task;

import io.mykit.db.common.constants.MykitDbSyncConstants;
import io.mykit.db.common.db.DbConnection;
import io.mykit.db.common.entity.BaseDBInfo;
import io.mykit.db.common.exception.MykitDbSyncException;
import io.mykit.db.common.utils.DateUtils;
import io.mykit.db.oracle.entity.DBInfo;
import io.mykit.db.oracle.entity.JobInfo;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.sql.*;
import java.util.Date;

/**
 * @author binghe
 * @version 1.0.0
 * @description 执行的任务
 */
public class JobTask extends DbConnection implements Job {
    private final Logger logger = LoggerFactory.getLogger(JobTask.class);

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        this.logger.info("开始任务调度: {}", DateUtils.parseDateToString(new Date(), DateUtils.DATE_TIME_FORMAT));
        Connection sourceConn = null;
        Connection targetConn = null;
        JobDataMap data = context.getJobDetail().getJobDataMap();
        DBInfo srcDb = (DBInfo) data.get(MykitDbSyncConstants.SRC_DB);
        BaseDBInfo destDb = (BaseDBInfo) data.get(MykitDbSyncConstants.DEST_DB);
        JobInfo jobInfo = (JobInfo) data.get(MykitDbSyncConstants.JOB_INFO);
        String logTitle = (String) data.get(MykitDbSyncConstants.LOG_TITLE);
        ResultSet resultSet = null;
        try{
            sourceConn = getConnection(MykitDbSyncConstants.TYPE_SOURCE, srcDb);
            targetConn = getConnection(MykitDbSyncConstants.TYPE_DEST, destDb);
            if (sourceConn == null) {
                this.logger.error("请检查源数据连接!");
                throw new MykitDbSyncException("请检查源数据连接!");
            } else if (targetConn == null) {
                this.logger.error("请检查目标数据连接!");
                throw new MykitDbSyncException("请检查目标数据连接!");
            }
            //联机处理分析数据
            Statement statement = sourceConn.createStatement();
            // 添加所有日志文件，本代码仅分析联机日志
            StringBuffer sbSQL = new StringBuffer();
            sbSQL.append(" BEGIN");
            sbSQL.append(" dbms_logmnr.add_logfile(logfilename=>'" + srcDb.getLogPath() + File.separator + "redo01.log', options=>dbms_logmnr.NEW);");
            sbSQL.append(" dbms_logmnr.add_logfile(logfilename=>'" + srcDb.getLogPath() + File.separator + "redo02.log', options=>dbms_logmnr.ADDFILE);");
            sbSQL.append(" dbms_logmnr.add_logfile(logfilename=>'" + srcDb.getLogPath() + File.separator + "redo03.log', options=>dbms_logmnr.ADDFILE);");
            sbSQL.append(" END;");
            CallableStatement callableStatement = sourceConn.prepareCall(sbSQL+"");
            callableStatement.execute();

            // 打印分析日志文件信息
            resultSet = statement.executeQuery("SELECT db_name, thread_sqn, filename FROM v$logmnr_logs");
            while(resultSet.next()){
                logger.debug("已添加日志文件==>{}", resultSet.getObject(3));
            }
            logger.debug("开始分析日志文件,起始scn号：{}", srcDb.getLastScn());
            callableStatement = sourceConn.prepareCall("BEGIN dbms_logmnr.start_logmnr(startScn=>'" + srcDb.getLastScn()+ "',dictfilename=>'" + srcDb.getDataDictionary() + File.separator +"dictionary.ora',OPTIONS =>DBMS_LOGMNR.COMMITTED_DATA_ONLY+dbms_logmnr.NO_ROWID_IN_STMT);END;");
            callableStatement.execute();
            logger.debug("完成分析日志文件");

            // 查询获取分析结果
            logger.debug("查询分析结果");
            resultSet = statement.executeQuery("SELECT scn,operation,timestamp,status,sql_redo FROM v$logmnr_contents WHERE seg_owner='" + srcDb.getClientUserName() + "' AND seg_type_name='TABLE' AND operation !='SELECT_FOR_UPDATE'");

            // 连接到目标数据库，在目标数据库执行redo语句
            Statement targetStatement = targetConn.createStatement();

            String lastScn = srcDb.getLastScn();
            String operation = null;
            String sql = null;
            boolean isCreateDictionary = false;

            while(resultSet.next()){
                Object scn = resultSet.getObject(1);
                if (scn == null){
                    continue;
                }
                lastScn = String.valueOf(scn);
                if(lastScn.equals(srcDb.getLastScn()) ){
                    logger.debug("本次运行的lastScn与上次相同，跳过本次循环");
                    continue;
                }

                Object opera = resultSet.getObject(2);
                if(opera == null){
                    logger.debug("获取到的操作类型为空，跳过本次循环");
                    continue;
                }
                operation = String.valueOf(opera);
                logger.debug("operation={}", operation);
                if( "DDL".equalsIgnoreCase(operation) ){
                    isCreateDictionary = true;
                }

                Object sqlObject = resultSet.getObject(5);
                if(sqlObject == null){
                    logger.debug("获取到的SQL语句为空，跳过本次循环");
                    continue;
                }

                sql = String.valueOf(sqlObject);
                // 替换用户
                sql = sql.replace("\"" + srcDb.getClientUserName() + "\".", "");
                logger.debug("scn = {}, 自动执行sql = {}", lastScn, sql);

                try {
                    targetStatement.executeUpdate(sql.substring(0, sql.length()-1));
                } catch (SQLException e) {
                    logger.debug("测试一下,已经执行过了");
                    throw new MykitDbSyncException(e.getMessage());
                }
                //更新scn
                srcDb.setLastScn(lastScn);
                // DDL发生变化，更新数据字典
                if(isCreateDictionary ){
                    logger.debug("DDL发生变化，更新数据字典");
                    //创建数据字典
                    this.createDictionary(sourceConn, srcDb.getDataDictionary());
                    logger.debug("完成更新数据字典");
                    isCreateDictionary = false;
                }
                logger.debug("完成一次同步任务");
            }

        }catch (SQLException e){
            this.logger.error(logTitle + e.getMessage());
            this.logger.error(logTitle + " SQL执行出错，请检查是否存在语法错误");
            throw new MykitDbSyncException(logTitle + e.getMessage());
        }catch (Exception e){
            e.printStackTrace();
        }
        finally {
            this.logger.info("关闭源数据库连接");
            destoryConnection(sourceConn);
            this.logger.info("关闭目标数据库连接");
            destoryConnection(targetConn);
        }
    }

    /**
     * <p>方法名称: createDictionary|描述: 调用logminer生成数据字典文件</p>
     * @param sourceConn 源数据库连接
     * @throws Exception 异常信息
     */
    private void createDictionary(Connection sourceConn, String dictionary) throws SQLException {
        String createDictSql = "BEGIN dbms_logmnr_d.build(dictionary_filename => 'dictionary.ora', dictionary_location =>'"+ dictionary+"'); END;";
        CallableStatement callableStatement = sourceConn.prepareCall(createDictSql);
        callableStatement.execute();
    }
}
