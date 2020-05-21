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
package io.mykit.db.common.build;

import io.mykit.db.common.constants.MykitDbSyncConstants;
import org.dom4j.Element;

import java.lang.reflect.Field;

/**
 * @author binghe
 * @version 1.0.0
 * @description 基础构建类
 */
public class BaseBuilder {

    /**
     * 解析e中的元素，将数据填充到o中
     * @param e 解析的XML Element对象
     * @param o 存放解析后的XML Element对象
     * @return 存放有解析后数据的Object
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     */
    protected Object elementInObject(Element e, Object o) throws IllegalArgumentException, IllegalAccessException {
        Class<?> clazz = o.getClass();
        while (clazz != null){
            Field[] fields = clazz.getDeclaredFields();
            for (int index = 0; index < fields.length; index++) {
                Field item = fields[index];
                //当前字段不是serialVersionUID，同时当前字段不包含serialVersionUID
                if (!MykitDbSyncConstants.FIELD_SERIALVERSIONUID.equals(item.getName()) && !item.getName().contains(MykitDbSyncConstants.FIELD_SERIALVERSIONUID)){
                    item.setAccessible(true);
                    item.set(o, e.element(item.getName()).getTextTrim());
                }
            }
            clazz = clazz.getSuperclass();
        }
        return o;
    }
}
