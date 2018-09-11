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
package io.mykit.db.sync.sync;

import io.mykit.db.sync.entity.JobInfo;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author liuyazhuang
 * @date 2018/9/11 10:19
 * @description 数据库同步接口
 * @version 1.0.0
 */
public interface DBSync {
    /**
     *
     * @param paramString:同步参数
     * @param paramConnection：数据库连接
     * @param paramJobInfo：同步任务
     * @return
     * @throws SQLException
     */
    String assembleSQL(String paramString, Connection paramConnection, JobInfo paramJobInfo) throws SQLException;
    /**
     *
     * @param sql：要执行的SQL语句
     * @param conn：数据库连接
     * @throws SQLException
     */
    void executeSQL(String sql, Connection conn) throws SQLException;
}
