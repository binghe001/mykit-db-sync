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
package io.mykit.db.common.db;

import com.alibaba.druid.pool.DruidDataSource;
import io.mykit.db.common.constants.MykitDbSyncConstants;
import io.mykit.db.common.entity.BaseDBInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author binghe
 * @version 1.0.0
 * @description 数据源的工厂类
 */
public class DataSourceFactory {

    private static volatile Map<String, DruidDataSource> dataSourceMap;

    static{
        dataSourceMap = new HashMap<>();
    }

    /**
     * 获取DruidDataSource数据源对象
     * @param dbType 数据源的类型，取值source为源数据库；dest为目标
     * @return DruidDataSource数据源对象
     */
    public static DruidDataSource getDruidDataSource(String dbType, BaseDBInfo baseDBInfo){
        //不包含指定类型的数据源
        if (!dataSourceMap.containsKey(dbType)){
            DruidDataSource dataSource = new DruidDataSource();
            dataSource.setUrl(baseDBInfo.getUrl());
            dataSource.setDriverClassName(baseDBInfo.getDriver());
            dataSource.setUsername(baseDBInfo.getUsername());
            dataSource.setPassword(baseDBInfo.getPassword());
            dataSource.setInitialSize(10);  //初始连接数，默认0
            dataSource.setMaxActive(30);  //最大连接数，默认8
            dataSource.setMinIdle(10);  //最小闲置数
            dataSource.setMaxWait(6000);  //获取连接的最大等待时间，单位毫秒
            dataSource.setTimeBetweenEvictionRunsMillis(3600000);
            dataSource.setMinEvictableIdleTimeMillis(3600000);
            if(MykitDbSyncConstants.TYPE_DB_MYSQL.equals(baseDBInfo.getDbtype())){  //MySQL
                dataSource.setValidationQuery("SELECT 1");
            }else if(MykitDbSyncConstants.TYPE_DB_ORACLE.equals(baseDBInfo.getDbtype())){ //Oracle
                dataSource.setValidationQuery("SELECT 1 FROM DUAL");
            }else if(MykitDbSyncConstants.TYPE_DB_SQLSERVER.equals(baseDBInfo.getDbtype())){
                //TODO 待实现
            }
            dataSource.setTestWhileIdle(true);
            dataSource.setTestOnBorrow(false);
            dataSource.setTestOnReturn(false);
            dataSource.setPoolPreparedStatements(true); //缓存PreparedStatement，默认false
            dataSource.setMaxOpenPreparedStatements(20); //缓存PreparedStatement的最大数量，默认-1（不缓存）。大于0时会自动开启缓存PreparedStatement，所以可以省略上一句代码
            dataSource.setMaxPoolPreparedStatementPerConnectionSize(20);
            dataSourceMap.put(dbType, dataSource);
        }
        return dataSourceMap.get(dbType);
    }
}
