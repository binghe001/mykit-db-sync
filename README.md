# 作者及联系方式
作者：冰河  
微信：sun_shine_lyz  
QQ：2711098650  
微信公众号： 冰河技术

# mykit-db-sync
mykit中分离出的强大数据数据库同步工具——mykit-db-sync  
基于java开发的功能强大、配置灵活的数据库之间同步工具，和数据产生器一样，均是前段时间因为项目需要编写的小工具，在实际应用场景中，我们经常需要定期将一个数据库的数据同步到另外一个数据库中，常见的一种做法是将源数据库的数据dump为sql文件，然后到目标数据库执行sql文件完成数据库的导入，但是这种方法至少存在以下问题：
- 需要手工操作，效率低
- 当涉及数据表较多时，容易遗漏、出错
- 如果要定期同步，操作人容易忘记
- 难以应付频繁变更数据表或者字段

针对以上存在的问题，将珍贵人力从这种重复、无意义的工作中解脱出来，特意开发这个小工具，目前支持MySQL、SQL Server和Oracle数据库的同步

# 主要模块
## mykit-db-common
提供通用的工具类

## mykit-db-transfer
MySQL与SQL Server数据互传模块，使用此模块可以实现MySQL和SQL Server数据库之间的数据互传功能

## mykit-db-oracle
基于Logminer实现Oracle与Oracle数据库之间的数据传输，支持表结构变化的传输


# 主要功能
## mykit-db-common
提供通用的工具类

## mykit-db-transfer
详见：[《mykit-db-transfer功能说明》](mykit-db-transfer/README.md)

## mykit-db-oracle
详见：[《mykit-db-oracle功能说明》](mykit-db-oracle/README.md)

# 扫一扫关注微信公众号

**你在刷抖音，玩游戏的时候，别人都在这里学习，成长，提升，人与人最大的差距其实就是思维。你可能不信，优秀的人，总是在一起。** 
  
扫一扫关注冰河技术微信公众号  
![微信公众号](https://img-blog.csdnimg.cn/20200716220443647.png)  
