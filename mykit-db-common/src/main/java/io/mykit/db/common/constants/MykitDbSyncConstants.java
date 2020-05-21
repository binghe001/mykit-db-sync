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
package io.mykit.db.common.constants;

/**
 * @author binghe
 * @description 常量类
 * @version 1.0.0
 */
public class MykitDbSyncConstants {

    /**
     * sqlserver数据库
     */
    public static final String TYPE_DB_SQLSERVER = "sqlserver";

    /**
     * MySQL数据库
     */
    public static final String TYPE_DB_MYSQL = "mysql";
    /**
     * Oracle数据库
     */
    public static final String TYPE_DB_ORACLE = "oracle";

    /**
     * 序列化标识的字段
     */
    public static final String FIELD_SERIALVERSIONUID = "serialVersionUID";

    /**
     * 配置文件的目录
     */
    public static final String JOB_CONFIG_FILE = "jobs.xml";

    /**
     * 对应xml文件的source节点
     */
    public static final String NODE_SOURCE = "source";
    /**
     * 对应xml文件的dest节点
     */
    public static final String NODE_DEST = "dest";
    /**
     * 对应xml文件的jobs节点
     */
    public static final String NODE_JOBS = "jobs";
    /**
     * 对应xml文件的job节点
     */
    public static final String NODE_JOB = "job";
    /**
     * 对应xml文件的code节点
     */
    public static final String NODE_CODE = "code";
    /**
     * 源数据库
     */
    public static final String SRC_DB = "srcDb";
    /**
     * 目标数据库
     */
    public static final String DEST_DB = "destDb";
    /**
     * 任务信息
     */
    public static final String JOB_INFO = "jobInfo";
    /**
     * 日志标头
     */
    public static final String LOG_TITLE = "logTitle";
    /**
     * job前缀
     */
    public static final String JOB_PREFIX = "job-";
    /**
     * trigger前缀
     */
    public static final String TRIGGER_PREFIX = "trigger-";

}
