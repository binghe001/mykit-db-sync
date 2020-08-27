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
package io.mykit.db.transfer.sync.impl;

import io.mykit.db.common.constants.MykitDbSyncConstants;
import io.mykit.db.common.utils.StringUtils;
import io.mykit.db.common.utils.Tool;
import io.mykit.db.transfer.entity.JobInfo;
import io.mykit.db.transfer.sync.DBSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author binghe
 * @description MySQL数据库同步实现
 * @version 1.0.0
 */
public class MySQLSync extends AbstractDBSync implements DBSync {
    private Logger logger = LoggerFactory.getLogger(MySQLSync.class);

    @Override
    public String assembleSQL(String srcSql, Connection conn, JobInfo jobInfo) throws SQLException {
        String uniqueName = Tool.generateString(6) + "_" + jobInfo.getName();
        String[] destFields = jobInfo.getDestTableFields().split(MykitDbSyncConstants.FIELD_SPLIT);
        destFields = this.trimArrayItem(destFields);
        //默认的srcFields数组与destFields相同
        String[] srcFields = destFields;
        String srcField = jobInfo.getSrcTableFields();
        if(!StringUtils.isEmpty(srcField)){
            srcFields = this.trimArrayItem(srcField.split(MykitDbSyncConstants.FIELD_SPLIT));
        }
        String[] updateFields = jobInfo.getDestTableUpdate().split(MykitDbSyncConstants.FIELD_SPLIT);
        updateFields = this.trimArrayItem(updateFields);
        String destTable = jobInfo.getDestTable();
        String destTableKey = jobInfo.getDestTableKey();
        PreparedStatement pst = conn.prepareStatement(srcSql);
        ResultSet rs = pst.executeQuery();
        StringBuilder sql = new StringBuilder();
        sql.append("insert into ").append(jobInfo.getDestTable()).append(" (").append(jobInfo.getDestTableFields()).append(") values ");
        long count = 0;
        while (rs.next()) {
            sql.append("(");
            for (int index = 0; index < destFields.length; index++) {
                Object fieldValue = rs.getObject(srcFields[index]);
                if (fieldValue == null){
                    sql.append(fieldValue).append(index == (destFields.length - 1) ? "" : ",");
                }else{
                    sql.append("'").append(fieldValue).append(index == (destFields.length - 1) ? "'" : "',");
                }
            }
            sql.append("),");
            count++;
        }
        if (rs != null) {
            rs.close();
        }
        if (pst != null) {
            pst.close();
        }
        if (count > 0) {
            sql = sql.deleteCharAt(sql.length() - 1);
            if ((!StringUtils.isEmpty(jobInfo.getDestTableUpdate())) && (!StringUtils.isEmpty(jobInfo.getDestTableKey()))) {
                sql.append(" on duplicate key update ");
                for (int index = 0; index < updateFields.length; index++) {
                    sql.append(updateFields[index]).append("= values(").append(updateFields[index]).append(index == (updateFields.length - 1) ? ")" : "),");
                }
                return new StringBuffer("alter table ").append(destTable).append(" add constraint ").append(uniqueName).append(" unique (").append(destTableKey).append(");").append(sql.toString())
                        .append(";alter table ").append(destTable).append(" drop index ").append(uniqueName).toString();
            }
            logger.debug(sql.toString());
            return sql.toString();
        }
        return null;
    }

    @Override
    public void executeSQL(String sql, Connection conn) throws SQLException {
        PreparedStatement pst = conn.prepareStatement("");
        String[] sqlList = sql.split(";");
        for (int index = 0; index < sqlList.length; index++) {
            pst.addBatch(sqlList[index]);
        }
        pst.executeBatch();
        conn.commit();
        pst.close();
    }
}
