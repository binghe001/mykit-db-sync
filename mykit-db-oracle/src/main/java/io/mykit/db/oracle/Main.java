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
package io.mykit.db.oracle;

import io.mykit.db.common.utils.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * @author binghe
 * @version 1.0.0
 * @description 程序的启动入口类
 */
public class Main {

    private static Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args){
        logger.info("同步数据开始===>>>" + DateUtils.parseDateToString(new Date(), DateUtils.DATE_TIME_FORMAT));

        logger.info("同步数据结束===>>>" + DateUtils.parseDateToString(new Date(), DateUtils.DATE_TIME_FORMAT));
    }
}
