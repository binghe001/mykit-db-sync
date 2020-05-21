/**
 * Copyright 2018-2118 the original author or authors.
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
package io.mykit.db.sync.task;

import io.mykit.db.sync.constants.MykitDbSyncConstants;
import io.mykit.db.sync.entity.DBInfo;
import io.mykit.db.sync.entity.JobInfo;
import io.mykit.db.sync.exception.MykitDbSyncException;
import io.mykit.db.sync.factory.DBSyncFactory;
import io.mykit.db.sync.sync.DBSync;
import io.mykit.db.sync.utils.DateUtils;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;

/**
 * @author liuyazhuang
 * @date 2018/9/11 10:30
 * @description 同步数据库任务的具体实现
 * @version 1.0.0
 */
public class JobTask implements Job {
    private final Logger logger = LoggerFactory.getLogger(JobTask.class);

    /**
     * 执行同步数据库任务
     *
     */
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        this.logger.info("开始任务调度: {}", DateUtils.parseDateToString(new Date(), DateUtils.DATE_TIME_FORMAT));
        Connection inConn = null;
        Connection outConn = null;
        JobDataMap data = context.getJobDetail().getJobDataMap();
        DBInfo srcDb = (DBInfo) data.get(MykitDbSyncConstants.SRC_DB);
        DBInfo destDb = (DBInfo) data.get(MykitDbSyncConstants.DEST_DB);
        JobInfo jobInfo = (JobInfo) data.get(MykitDbSyncConstants.JOB_INFO);
        String logTitle = (String) data.get(MykitDbSyncConstants.LOG_TITLE);
        try {
            inConn = createConnection(srcDb);
            outConn = createConnection(destDb);
            if (inConn == null) {
                this.logger.error("请检查源数据连接!");
                throw new MykitDbSyncException("请检查源数据连接!");
            } else if (outConn == null) {
                this.logger.error("请检查目标数据连接!");
                throw new MykitDbSyncException("请检查目标数据连接!");
            }

            DBSync dbHelper = DBSyncFactory.create(destDb.getDbtype());
            long start = System.currentTimeMillis();
            String sql = dbHelper.assembleSQL(jobInfo.getSrcSql(), inConn, jobInfo);
            this.logger.info("组装SQL耗时: " + (System.currentTimeMillis() - start) + "ms");
            if (sql != null) {
                this.logger.debug(sql);
                long eStart = System.currentTimeMillis();
                dbHelper.executeSQL(sql, outConn);
                this.logger.info("执行SQL语句耗时: " + (System.currentTimeMillis() - eStart) + "ms");
            }
        } catch (SQLException e) {
            this.logger.error(logTitle + e.getMessage());
            this.logger.error(logTitle + " SQL执行出错，请检查是否存在语法错误");
            throw new MykitDbSyncException(logTitle + e.getMessage());
        } finally {
            this.logger.info("关闭源数据库连接");
            destoryConnection(inConn);
            this.logger.info("关闭目标数据库连接");
            destoryConnection(outConn);
        }
    }

    /**
     * 创建数据库连接
     * @param db
     * @return
     */
    private Connection createConnection(DBInfo db) {
        try {
            Class.forName(db.getDriver());
            Connection conn = DriverManager.getConnection(db.getUrl(), db.getUsername(), db.getPassword());
            conn.setAutoCommit(false);
            return conn;
        } catch (Exception e) {
            this.logger.error(e.getMessage());
        }
        return null;
    }

    /**
     * 关闭并销毁数据库连接
     * @param conn
     */
    private void destoryConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
                this.logger.error("数据库连接关闭");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
