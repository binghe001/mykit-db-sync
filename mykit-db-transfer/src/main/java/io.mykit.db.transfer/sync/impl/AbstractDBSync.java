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
import io.mykit.db.common.exception.MykitDbSyncException;
import io.mykit.db.transfer.sync.DBSync;

import java.util.HashMap;
import java.util.Map;

/**
 * @author binghe
 * @description 执行数据库同步的抽象类
 * @version 1.0.0
 */
public abstract class AbstractDBSync implements DBSync {
    /**
     * 去除String数组每个元素中的空格
     * @param src 需要去除空格的数组
     * @return 去除空格后的数组
     */
    protected String[] trimArrayItem(String[] src){
        if(src == null || src.length == 0) return src;
        String[] dest = new String[src.length];
        for(int i = 0; i < src.length; i++){
            dest[i] = src[i].trim();
        }
        return dest;
    }

    /**
     * 构建字段的映射关系
     */
    protected Map<String, String> getFieldsMapper(String[] srcFields, String[] destFields){
        if (srcFields.length != destFields.length){
            throw new MykitDbSyncException("源数据库与目标数据库的字段必须一一对应");
        }
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < srcFields.length; i++){
            map.put(destFields[i].trim(), srcFields[i].trim());
        }
        return map;
    }
}
