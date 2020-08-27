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
package io.mykit.db.transfer.sync.impl;

import io.mykit.db.common.constants.MykitDbSyncConstants;
import io.mykit.db.common.utils.StringUtils;
import io.mykit.db.transfer.entity.JobInfo;
import io.mykit.db.transfer.sync.DBSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

/**
 * @author binghe
 * @version 1.0.0
 * @description Oracle数据库同步实现
 */
public class OracleSync extends AbstractDBSync implements DBSync {

    private Logger logger = LoggerFactory.getLogger(OracleSync.class);

    @Override
    public String assembleSQL(String srcSql, Connection conn, JobInfo jobInfo) throws SQLException {
        String[] destFields = jobInfo.getDestTableFields().split(",");
        destFields = this.trimArrayItem(destFields);
        String[] srcFields = destFields;
        String srcField = jobInfo.getSrcTableFields();
        if(!StringUtils.isEmpty(jobInfo.getSrcTableFields())){
            srcFields = this.trimArrayItem(srcField.split(MykitDbSyncConstants.FIELD_SPLIT));
        }
        Map<String, String> fieldMapper = this.getFieldsMapper(srcFields, destFields);
        System.out.println(fieldMapper.toString());
        String[] updateFields = jobInfo.getDestTableUpdate().split(MykitDbSyncConstants.FIELD_SPLIT);
        updateFields = this.trimArrayItem(updateFields);
        String destTable = jobInfo.getDestTable();
        String destTableKey = jobInfo.getDestTableKey();
        PreparedStatement pst = conn.prepareStatement(srcSql);
        ResultSet rs = pst.executeQuery();
        StringBuilder sql = new StringBuilder();
        sql.append("begin ");
        while (rs.next()){
            sql.append(" update ").append(destTable).append(" set ");
            //拼接更新语句
            for(int i = 0; i < updateFields.length - 1; i++){
                //取得查询的数据
                String currentColumn = fieldMapper.get(updateFields[i].trim());
                System.out.println(currentColumn);
                Object fieldValue = rs.getObject(currentColumn);
                if (fieldValue == null){
                    sql.append(updateFields[i] + " = " + fieldValue + ", ");
                }else{
                    String fieldValueStr = fieldValue.toString();
                    //时间分割符
                    if (fieldValueStr.contains(MykitDbSyncConstants.DATE_SPLIT)){
                        sql.append(updateFields[i] + " = to_date('" + fieldValue + "', 'yyyy-mm-dd hh24:mi:ss'), ");
                    }else{
                        sql.append(updateFields[i] + " = '" + fieldValue + "', ");
                    }
                }
            }
            Object fieldValue = rs.getObject(fieldMapper.get(updateFields[updateFields.length - 1].trim()));
            if (fieldValue == null){
                sql.append(updateFields[updateFields.length - 1] + " = " + fieldValue);
            }else{
                String fieldValueStr = fieldValue.toString();
                //时间分割符
                if (fieldValueStr.contains(MykitDbSyncConstants.DATE_SPLIT)){
                    sql.append(updateFields[updateFields.length - 1] + " = to_date('" + fieldValue + "', '"+MykitDbSyncConstants.ORACLE_DATE_FORMAT+"') ");
                }else{
                    sql.append(updateFields[updateFields.length - 1] + " = '" + fieldValue + "'");
                }
            }
            sql.append( " where " ).append(destTableKey).append(" = '"+rs.getObject(fieldMapper.get(destTableKey))+"';");
            sql.append("  if sql%notfound then ");
            sql.append(" insert into ").append(destTable).append(" (").append(jobInfo.getDestTableFields()).append(") values ( ");
            for (int index = 0; index < destFields.length; index++) {
                Object value = rs.getObject(fieldMapper.get(destFields[index].trim()));
                if (value == null){
                    sql.append(value).append(index == (destFields.length - 1) ? "" : ",");
                }else{
                    String valueStr = value.toString();
                    if (valueStr.contains(MykitDbSyncConstants.DATE_SPLIT)){
                        sql.append(" to_date('" + fieldValue + "', '"+MykitDbSyncConstants.ORACLE_DATE_FORMAT+"') ").append(index == (destFields.length - 1) ? "" : ",");
                    }else{
                        sql.append("'").append(value).append(index == (destFields.length - 1) ? "'" : "',");
                    }
                }
            }
            sql.append(" );");
            sql.append(" end if; ");
        }
        sql.append(" end; ");
        logger.debug(sql.toString());
        return sql.toString();
    }

    @Override
    public void executeSQL(String sql, Connection conn) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(sql);
        pst.executeUpdate();
        conn.commit();
        pst.close();
    }
}
