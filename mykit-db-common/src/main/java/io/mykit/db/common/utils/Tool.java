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
package io.mykit.db.common.utils;

/**
 * @author binghe
 * @description 工具类
 * @version 1.0.0
 */
public class Tool {

    /**
     * 产生随机字符串
     * @param length 字符串的长度
     * @return 随机的字符串
     */
    public static String generateString(int length) {
        if (length < 1)
            length = 6;
        String str = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String genStr = "";
        for (int index = 0; index < length; index++) {
            genStr = genStr + str.charAt((int) ((Math.random() * 100) % 26));
        }
        return genStr;
    }
}
