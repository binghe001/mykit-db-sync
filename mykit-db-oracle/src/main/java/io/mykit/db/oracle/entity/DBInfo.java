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
package io.mykit.db.oracle.entity;

import io.mykit.db.common.entity.BaseDBInfo;

/**
 * @author binghe
 * @version 1.0.0
 * @description 数据库信息
 */
public class DBInfo extends BaseDBInfo {
    private static final long serialVersionUID = -1307131236484165237L;
    //上次同步最后SCN号
    private String lastScn = "0";
    //源数据库客户端用户名
    private String clientUserName;
    //日志文件的路径
    private String logPath;
    //数据字典的路径
    private String dataDictionary;

    public String getLastScn() {
        return lastScn;
    }

    public void setLastScn(String lastScn) {
        this.lastScn = lastScn;
    }

    public String getClientUserName() {
        return clientUserName;
    }

    public void setClientUserName(String clientUserName) {
        this.clientUserName = clientUserName;
    }

    public String getLogPath() {
        return logPath;
    }

    public void setLogPath(String logPath) {
        this.logPath = logPath;
    }

    public String getDataDictionary() {
        return dataDictionary;
    }

    public void setDataDictionary(String dataDictionary) {
        this.dataDictionary = dataDictionary;
    }

}
