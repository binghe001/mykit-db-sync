# 作者简介: 
Adam Lu(刘亚壮)，高级软件架构师，Java编程专家，Spring、MySQL内核专家，开源分布式消息引擎Mysum发起者、首席架构师及开发者，Android开源消息组件Android-MQ独立作者，国内知名开源分布式数据库中间件Mycat核心架构师、开发者，精通Java, C, C++, Python, Hadoop大数据生态体系，熟悉MySQL、Redis内核，Android底层架构。多年来致力于分布式系统架构、微服务、分布式数据库、大数据技术的研究，曾主导过众多分布式系统、微服务及大数据项目的架构设计、研发和实施落地。在高并发、高可用、高可扩展性、高可维护性和大数据等领域拥有丰富的经验。对Hadoop、Spark、Storm等大数据框架源码进行过深度分析并具有丰富的实战经验。

# 作者联系方式
QQ：2711098650

# mykit-db-sync
mykit中分离出的强大数据数据库同步工具——mykit-db-sync  
基于java开发的功能强大、配置灵活的数据库之间同步工具，和数据产生器一样，均是前段时间因为项目需要编写的小工具，在实际应用场景中，我们经常需要定期将一个数据库的数据同步到另外一个数据库中，常见的一种做法是将源数据库的数据dump为sql文件，然后到目标数据库执行sql文件完成数据库的导入，但是这种方法至少存在以下问题：
- 需要手工操作，效率低
- 当涉及数据表较多时，容易遗漏、出错
- 如果要定期同步，操作人容易忘记
- 难以应付频繁变更数据表或者字段

针对以上存在的问题，将珍贵人力从这种重复、无意义的工作中解脱出来，特意开发这个小工具，其中主要配置主要在jobs.xml中完成

# 主要功能
- 目标数据目前只支持MySQL和SQL Sever，源数据库为任何支持sql语法的数据库
- 根据cron表达式配置数据同步的周期和时间
- 执行多个数据同步任务
- 源数据是根据配置的sql语句查询得到，使用者可以非常灵活根据需要进行修改
- 根据配置的字段，判断同步数据是插入还是更新

# 目前支持的数据库同步方式：
- MySQL——>MySQL  
- MySQL——>SQL Server  
- SQL Server——>SQL Server  
- SQL Server——>MySQL  

# 编译和运行

> mvn package

>cp jobs.xml ./target/jobs.xml

> cd target

> java -jar mykit-db-sync-1.0.0.jar

# 基本使用

### MySQL——>MySQL
jobs.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<root>
    <code>4500000001</code>
    <source>
        <url>jdbc:mysql://10.2.2.231:3306/test</url>
        <username>root</username>
        <password>root</password>
        <dbtype>mysql</dbtype>
        <driver>com.mysql.jdbc.Driver</driver>
    </source>
    <dest>
        <url>jdbc:mysql://127.0.0.1:3306/test</url>
        <username>root</username>
        <password>root</password>
        <dbtype>mysql</dbtype>
        <driver>com.mysql.jdbc.Driver</driver>
    </dest>
    <jobs>
        <job>
            <name>1</name>
            <cron>0/300 * * * * ?</cron>
            <srcSql>select user_id, account,password from client_user</srcSql>
            <destTable>client_user</destTable>
            <destTableFields>user_id, account, password</destTableFields>
            <destTableKey>user_id</destTableKey>
            <destTableUpdate>account, password</destTableUpdate>
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
