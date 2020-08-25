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
package io.mykit.db.transfer.test;

import io.mykit.db.transfer.build.DBSyncBuilder;

/**
 * @author binghe
 * @version 1.0.0
 * @description 测试功能
 */
public class MykitTransferTest {

    public static void main(String[] args){
        String path = System.getProperty("user.dir");
        path = path.replace("\\", "/").concat("/").concat("mykit-db-transfer").concat("/").concat("jobs.xml");
        System.out.println(path);
        DBSyncBuilder.builder().init(path).start();
    }
}
