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

import io.mykit.db.transfer.entity.JobInfo;
import io.mykit.db.transfer.sync.DBSync;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author binghe
 * @version 1.0.0
 * @description Oracle数据库同步实现
 */
public class OracleSync extends AbstractDBSync implements DBSync {

    private Logger logger = LoggerFactory.getLogger(OracleSync.class);

    @Override
    public String assembleSQL(String paramString, Connection paramConnection, JobInfo paramJobInfo) throws SQLException {
        //TODO 待实现
        return null;
    }

    @Override
    public void executeSQL(String sql, Connection conn) throws SQLException {
        //TODO 待实现
    }
}
