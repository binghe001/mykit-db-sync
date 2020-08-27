# 作者及联系方式
作者：冰河  
QQ：2711098650  
微信：sun_shine_lyz  
微信公众号： 冰河技术

# mykit-db-transfer
mykit中分离出的强大数据数据库同步工具——mykit-db-sync中的支持MySQL和SQL Server数据互传的mykit-db-transfer模块  
基于java开发的功能强大、配置灵活的数据库之间同步工具，和数据产生器一样，均是前段时间因为项目需要编写的小工具，在实际应用场景中，我们经常需要定期将一个数据库的数据同步到另外一个数据库中，常见的一种做法是将源数据库的数据dump为sql文件，然后到目标数据库执行sql文件完成数据库的导入，但是这种方法至少存在以下问题：
- 需要手工操作，效率低
- 当涉及数据表较多时，容易遗漏、出错
- 如果要定期同步，操作人容易忘记
- 难以应付频繁变更数据表或者字段

针对以上存在的问题，将珍贵人力从这种重复、无意义的工作中解脱出来，特意开发这个小工具，其中主要配置主要在jobs.xml中完成

# 主要功能
- 目标数据库目前只支持MySQL和SQL Sever，Oracle，源数据库为任何支持sql语法的数据库
- 根据cron表达式配置数据同步的周期和时间
- 执行多个数据同步任务
- 源数据是根据配置的sql语句查询得到，使用者可以非常灵活根据需要进行修改
- 根据配置的字段，判断同步数据是插入还是更新
- 支持源数据表与目标数据表字段的差异化

# 目前支持的数据库同步方式：
- MySQL——>MySQL  
- MySQL——>SQL Server  
- SQL Server——>SQL Server  
- SQL Server——>MySQL  
- Oracle——>MySQL

# 编译和运行

> mvn package

> cp jobs.xml ./target/jobs.xml

> cd target

> java -jar mykit-db-transfer-1.0.0.jar jobs.xml （注意：运行程序时必须指定jobs.xml文件的路径，否则会抛出 MykitDbSyncException 异常）

# 基本使用

### MySQL——>MySQL
jobs.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<root>
    <code>4500000001</code>
    <source>
        <url>jdbc:mysql://localhost:3306/test_dev?useUnicode=true&amp;characterEncoding=UTF-8&amp;useOldAliasMetadataBehavior=true</url>
        <username>root</username>
        <password>root</password>
        <dbtype>mysql</dbtype>
        <driver>com.mysql.jdbc.Driver</driver>
    </source>
    <dest>
        <url>jdbc:mysql://localhost:3306/test_prod?useUnicode=true&amp;characterEncoding=UTF-8&amp;useOldAliasMetadataBehavior=true</url>
        <username>root</username>
        <password>root</password>
        <dbtype>mysql</dbtype>
        <driver>com.mysql.jdbc.Driver</driver>
    </dest>
    <jobs>
        <job>
            <name>1</name>
            <!--每隔30秒执行一次-->
            <cron>0/5 * * * * ?</cron>
            <srcSql>select id, username, t_age, t_create_time from t_user</srcSql>
            <srcTableFields>id, username, t_age, t_create_time</srcTableFields>
            <destTable>t_user</destTable>
            <destTableFields>id, username, age, t_create_time</destTableFields>
            <destTableKey>id</destTableKey>
            <destTableUpdate>username, age, t_create_time</destTableUpdate>
        </job>
    </jobs>
</root>
```

### SQLServer——>SQLServer
jobs.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<root>
    <code>4500000001</code>
    <source>
        <url>jdbc:sqlserver://10.2.2.243:1433;DatabaseName=AC</url>
        <username>sa</username>
        <password>123456</password>
        <dbtype>sqlserver</dbtype>
        <driver>com.microsoft.sqlserver.jdbc.SQLServerDriver</driver>
    </source>
    <dest>
        <url>jdbc:sqlserver://127.0.0.1:1433;DatabaseName=AC</url>
        <username>sa</username>
        <password>123456</password>
        <dbtype>sqlserver</dbtype>
        <driver>com.microsoft.sqlserver.jdbc.SQLServerDriver</driver>
    </dest>
    <jobs>
        <job>
            <name>1</name>
            <cron>0/20 * * * * ?</cron>
            <srcSql>select id, name,phone, department, photopath, groupId, uid from t_user</srcSql>
            <destTable>t_user</destTable>
            <destTableFields>id, name,phone, department, photopath, groupId, uid</destTableFields>
            <destTableKey>id</destTableKey>
            <destTableUpdate>name,phone, department, photopath, groupId, uid</destTableUpdate>
        </job>
    </jobs>
</root>
```

### Oracle——>MySQL
jobs.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<root>
    <code>4500000001</code>
    <source>
        <url>jdbc:oracle:thin:@192.168.175.100:1521:binghe</url>
        <username>BINGHE</username>
        <password>BINGHE123</password>
        <dbtype>oracle</dbtype>
        <driver>oracle.jdbc.driver.OracleDriver</driver>
    </source>
    <dest>
        <url>jdbc:mysql://localhost:3306/test_prod?useUnicode=true&amp;characterEncoding=UTF-8&amp;useOldAliasMetadataBehavior=true</url>
        <username>root</username>
        <password>root</password>
        <dbtype>mysql</dbtype>
        <driver>com.mysql.jdbc.Driver</driver>
    </dest>
    <jobs>
        <job>
            <name>1</name>
            <!--每隔30秒执行一次-->
            <cron>0/5 * * * * ?</cron>
            <srcSql>select id, username, age, t_create_time from A_TEST</srcSql>
            <srcTableFields>id, username, age, t_create_time</srcTableFields>
            <destTable>t_user</destTable>
            <destTableFields>id, username, age, t_create_time</destTableFields>
            <destTableKey>id</destTableKey>
            <destTableUpdate>username, age, t_create_time</destTableUpdate>
        </job>
    </jobs>
</root>
```

# 扫一扫关注微信公众号

**你在刷抖音，玩游戏的时候，别人都在这里学习，成长，提升，人与人最大的差距其实就是思维。你可能不信，优秀的人，总是在一起。** 
  
扫一扫关注冰河技术微信公众号  
![微信公众号](https://img-blog.csdnimg.cn/20200716220443647.png)   