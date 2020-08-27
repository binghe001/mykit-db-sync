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
package io.mykit.db.transfer.entity;

import io.mykit.db.common.entity.BaseJobInfo;

/**
 * @author binghe
 * @description 任务信息
 * @version 1.0.0
 */
public class JobInfo extends BaseJobInfo {
    private static final long serialVersionUID = -1907092113028096170L;

    //源数据源sql
    private String srcSql;
    //源数据表字段
    private String srcTableFields;
    //目标数据表
    private String destTable;
    //目标表数据字段
    private String destTableFields;
    //目标表主键
    private String destTableKey;
    //目标表可更新的字段
    private String destTableUpdate;

    public String getSrcTableFields() {
        return srcTableFields;
    }

    public void setSrcTableFields(String srcTableFields) {
        this.srcTableFields = srcTableFields;
    }

    public String getSrcSql() {
        return srcSql;
    }

    public void setSrcSql(String srcSql) {
        this.srcSql = srcSql;
    }

    public String getDestTable() {
        return destTable;
    }

    public void setDestTable(String destTable) {
        this.destTable = destTable;
    }

    public String getDestTableFields() {
        return destTableFields;
    }

    public void setDestTableFields(String destTableFields) {
        this.destTableFields = destTableFields;
    }

    public String getDestTableKey() {
        return destTableKey;
    }

    public void setDestTableKey(String destTableKey) {
        this.destTableKey = destTableKey;
    }

    public String getDestTableUpdate() {
        return destTableUpdate;
    }

    public void setDestTableUpdate(String destTableUpdate) {
        this.destTableUpdate = destTableUpdate;
    }
}
