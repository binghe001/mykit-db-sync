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
package io.mykit.db.common.db;

import io.mykit.db.common.entity.BaseDBInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author binghe
 * @version 1.0.0
 * @description 实现数据库的连接与关闭
 */
public class DbConnection {

    private final Logger logger = LoggerFactory.getLogger(DbConnection.class);
    /**
     * 创建数据库连接
     */
    protected Connection getConnection(String dbType, BaseDBInfo db) {
        try {
            Connection connection = DataSourceFactory.getDruidDataSource(dbType, db).getConnection();
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 关闭并销毁数据库连接
     */
    protected void destoryConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
                conn = null;
                this.logger.info("数据库连接关闭");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
