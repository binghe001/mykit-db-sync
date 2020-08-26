# 作者及联系方式
作者：冰河  
QQ：2711098650  
微信：sun_shine_lyz  
微信公众号： 冰河技术

# mykit-db-oracle
mykit中分离出的强大数据数据库同步工具——mykit-db-sync中的支持Oracle向Oracle动态传输数据的模块，同时支持数据库表结构的动态修改传输    
基于java开发的功能强大、配置灵活的数据库之间同步工具，和数据产生器一样，均是前段时间因为项目需要编写的小工具，在实际应用场景中，我们经常需要定期将一个数据库的数据同步到另外一个数据库中，常见的一种做法是将源数据库的数据dump为sql文件，然后到目标数据库执行sql文件完成数据库的导入，但是这种方法至少存在以下问题：
- 需要手工操作，效率低
- 当涉及数据表较多时，容易遗漏、出错
- 如果要定期同步，操作人容易忘记
- 难以应付频繁变更数据表或者字段

针对以上存在的问题，将珍贵人力从这种重复、无意义的工作中解脱出来，特意开发这个小工具，其中主要配置主要在jobs.xml中完成

# 主要功能
- 支持Oracle数据的动态传输
- 支持Oracle表结构的动态传输
- 执行多个数据同步任务
- 源数据是根据LogMiner解析得到的

# 目前支持的数据库同步方式：
- Oracle ——> Oracle

# 编译和运行

> mvn package

> cp jobs.xml ./target/jobs.xml

> cd target

> java -jar mykit-db-oracle-1.0.0.jar jobs.xml （注意：运行程序时必须指定jobs.xml文件的路径，否则会抛出 MykitDbSyncException 异常）

# 基本使用

### Oracle——>Oracle
jobs.xml
```
<?xml version="1.0" encoding="UTF-8"?>
<root>
    <code>4500000001</code>
    <source>
        <url>jdbc:oracle:thin:@127.0.0.1:1521:practice</url>
        <username>sync</username>
        <password>sync</password>
        <dbtype>oracle</dbtype>
        <driver>oracle.jdbc.driver.OracleDriver</driver>
        <lastScn>0</lastScn>
        <clientUserName>LOGMINER</clientUserName>
        <logPath>/home/log</logPath>
        <dataDictionary>/home/dic</dataDictionary>
    </source>
    <dest>
        <url>jdbc:oracle:thin:@127.0.0.1:1521:target</url>
        <username>target</username>
        <password>target</password>
        <dbtype>oracle</dbtype>
        <driver>oracle.jdbc.driver.OracleDriver</driver>
    </dest>
    <jobs>
        <job>
            <name>oracle</name>
            <!--每隔30秒执行一次-->
            <cron>0/30 * * * * ?</cron>
        </job>
    </jobs>
</root>
```

# 扫一扫关注微信公众号

**你在刷抖音，玩游戏的时候，别人都在这里学习，成长，提升，人与人最大的差距其实就是思维。你可能不信，优秀的人，总是在一起。** 
  
扫一扫关注冰河技术微信公众号  
![微信公众号](https://img-blog.csdnimg.cn/20200716220443647.png)   