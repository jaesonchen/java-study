/**   
 * mysql常用的存储引擎及区别
   1. MyISAM类型不支持事务处理等高级处理，而InnoDB类型支持。
   2. MyISAM类型的表强调的是性能，其执行速度比InnoDB类型更快。
   
  InnoDB(默认)
    数据的物理组织形式是簇表（Cluster Table），主键索引和数据是在一起的，数据按主键的顺序物理分布。
    实现了缓冲管理，不仅能缓冲索引也能缓冲数据，并且会自动创建散列索引以加快数据的获取。
    灾难恢复性好
    支持事务
    使用行级锁
    支持外键关联
    支持热备份
    
  MyISAM
    不支持事务
    使用表级锁，并发性差
    主机宕机后，MyISAM表易损坏，灾难恢复性不佳
    可以配合锁，实现操作系统下的复制备份、迁移
    只缓存索引，数据的缓存是利用操作系统缓冲区来实现的。可能引发过多的系统调用且效率不佳
    数据紧凑存储，因此可获得更小的索引和更快的全表扫描性能
    

    覆盖索引
  select的数据列只用从索引中就能够取得，不必从数据表中读取，换句话说查询列要被所使用的索引覆盖。
    非聚集组合索引的一种形式，它包括在查询里的Select、Join和Where子句用到的所有列
    （即建立索引的字段正好是覆盖查询语句[select子句]与查询条件[Where子句]中所涉及的字段，索引包含了查询正在查找的所有数据）。
    
    
  mysql索引类别
    单列：普通索引(create index)，唯一索引（unique index），主键索引(primary key)，主键索引比普通索引快。
    多列：联合索引（多列，也称之为组合索引），比如：联合主键索引、联合唯一索引、联合普通索引，组合索引查询速度 优于 多个单索引合并查询速度。
    
    索引的种类
  hash索引和BTree索引
  1. hash类型的索引：查询单条快，范围查询慢(> < like)，因为hash索引生成hash值的是无序的，所以不能使用排序。
  2. btree类型的索引：b+树，层数越多，数据量指数级增长（innodb默认支持它），二分查找查询（推荐使用自增主键）。
  
    组合索引最左前缀
  create index idx_name_email on userinfo(name, email);
  select * from userinfo where name = 'zhangsan'; 使用索引
  select * from userinfo where name = 'zhangsan' and email = 'zhangsan@qq.com'; 使用索引
  select * from userinfo where email = 'zhangsan@qq.com'; 不使用索引
  
    索引的注意事项
  1)避免使用select *
  2)count(1)或count(列) 代替count(*) (目前mysql5.7版本，没有区别。)
  3)创建表时尽量使用char代替varchar
  4)表的字段声明顺序固定长度的字段优先
  5)组合索引代替多个单列索引（经常使用多个条件查询时），组合索引要注意最左前缀问题
  6)尽量使用短索引  create index idx_title on tb(title(16)) (title 的数据类型 text类型)
  7)使用连接 join 来代替子查询 (目前mysql5.7版本，没有区别。它和子查询速度是一样的。)
  8)使用连接 join 时注意on 条件类型需一致
  9)索引散列（重复多）不适用于建索引，例如：性别不合适
  
    
    查询性能
    查询时的访问方式，性能：all < index < range < index_merge < ref_or_null < ref < eq_ref < system/const
  ALL：全表扫描，对于数据表从头到尾找一遍
  select * from userinfo;
  - 如果有limit限制，则找到之后就不在继续向下扫描
  select * from userinfo where email = 'zhangsan112@qq.com' limit 1;
    
  INDEX： 全索引扫描，对索引从头到尾找一遍
  select id from userinfo;
    
  RANGE： 对索引列进行范围查找(between and、in、 not in、>  >=  <  <=  <>  !=)
  select *  from userinfo where id > 1000;
    
  INDEX_MERGE： 合并索引，使用多个单列索引搜索
  select *  from userinfo where name = 'zhangsan' or id in (1001, 1002, 1003);
    
  REF： 根据索引查找一个或多个值
  select *  from userinfo where id = 1000;
    
  EQ_REF： 连接时使用primary key 或 unique类型
  select u.name, d.deptname from userinfo u left join dept d on u.deptid = d.id;
    
  CONST：常量，表最多有一个匹配行,因为仅有一行,在这行的列值可被优化器剩余部分认为是常数,const表很快,因为它们只读取一次。
  select id from userinfo where id = 1002;
  
    
  mysql性能优化：
  - 减少io次数，数据库操作中超过90%的时间都是 IO 操作所占用的，减少 IO 次数是 SQL 优化中需要第一优先考虑
  - 降低 CPU 计算， order by, group by, distinct等都是消耗 CPU 的大户，尽量少使用
  - 为经常查询的字段建立索引
  - 开启慢查询日志，可以让MySQL记录下查询超过指定时间的语句，通过定位分析性能的瓶颈，才能更好的优化数据库系统的性能。
  - 查询是否开了慢查询（slow_query_log 慢查询开启状态、slow_query_log_file 慢查询日志存放的位置）
    show variables like 'slow_query%';
  - 开启慢日志（1表示开启，0表示关闭，也可以在my.cnf 文件中设置）
    set global slow_query_log=1;
  - 查看慢查询超时时间（long_query_time默认10秒）
    show variables like 'long_query%';
  - explain 分析sql语句
    explain select * from userinfo where id=1001
  - 静态表速度更快，定长类型（char）和变长类型（varchar），效率上char更高，varchar更省空间
  - 当只需要一条数据的时候，使用limit 1
  - exists，那么以外层表为驱动表，先被访问，如果是IN，那么先执行子查询。所以IN适合于外表大而内表小的情况；EXISTS适合于外表小而内表大的情况
  - 复杂多表尽量少用join，MySQL 优化器效率高，但是由于其统计信息的量有限，优化器工作过程出现偏差的可能性也就更多
  - 尽量用join代替子查询
  - 尽量少or，使用 union all 或者是union(必要的时候)的方式来代替“or”会得到更好的效果。
  - 尽量用 union all 代替 union，union需要将两个结果集合并后再进行唯一性过滤操作，这就会涉及到排序，增加大量的 CPU 运算，加大资源消耗及延迟。
  - 尽量早过滤以减少io操作
  - 避免类型转换，如果我们传入的数据类型和字段类型不一致，同时我们又没有做任何类型转换处理，MySQL 可能会自己对我们的数据进行类型转换操作
  - 尽量避免where子句中对字段进行null值的判断，会导致引擎放弃索引，进而进行全表扫描。
  - 避免在where中使用!=, >, < ，否则引擎放弃使用索引，进行全表扫描。
  - in和not in关键词慎用，容易导致全表扫面，对连续的数值尽量用between
  - like '%'通配符查询也容易导致全表扫描
  - 不要where子句的‘=’左边进行函数、算术运算或其他表达式运算
  - 索引不是越多越好, 一个表的索引数最好不要超过6个
  - 避免频繁创建和删除临时表，减少系统表资源消耗
  - 使用select into代替create table，避免造成大量log，以提高速度。
  - 拆分大的DELETE和INSERT语句, 因为这两个操作是会锁表的，对于高访问量的站点来说，锁表时间内积累的访问数、数据库连接、打开的文件数等等，可能让WEB服务崩溃。
  
  
    分页性能相关方案
  - 第1页：
    select * from userinfo limit 0,10;
  - 第n页：
    select * from userinfo limit 10000,10;
    ......
  - 越往后查询，需要的时间约长，是因为越往后查，全文扫描查询，会去数据表中扫描查询
    
  a. 一个简单的方法就是尽可能地使用索引覆盖扫描，而不是查询所有的列，然后根据需要做一次关联操作再返回所需的列。
    select * from userinfo u join (select id from userinfo limit 10000,10) m on u.id=m.id;
  b. 最优的解决方案
  - 下一页：
    select * from userinfo where id>max_id limit 10;
  - 上一页：
    select * from userinfo where id<min_id order by id desc limit 10;
    
    
  - join 关联查询：
    SELECT
        t1.column1,
        t2.column2
    FROM
        tb1 t1
    INNER JOIN tb2 t2 ON t1.column3 = t2.column3
    WHERE
        t1.column1 IN (4, 6);
  - 伪代码:
    outer_iter = iterator over t1 WHERE column1 IN (4, 6)
    outer_row = outer_iter.next
    WHILE outer_row
        inner_iter = iterator over t2 WHERE column3 = outer_row.column3
        inner_row = inner_iter.next
        WHILE inner_row
            output [ outer_row.column1,inner_row.column2 ]
            inner_row = inner_iter.next
        END
        outer_row = outer_iter.next
    END
    
  MySQL是只支持一种JOIN算法Nested-Loop Join（嵌套循环链接），不像其他商业数据库可以支持哈希链接和合并连接。
  MySQL的Nested-Loop Join（嵌套循环链接）有很多变种，能够帮助MySQL更高效的执行JOIN操作：
  a. Simple Nested-Loop Join
    select * from R join S on r.id=s.id
  - 伪代码
    For each row r in R do
        For each row s in S do
            If r and s satisfy the join condition
            Then output the tuple
            
  b. Index Nested-Loop Join
  - SNLJ算法虽然简单明了，但是也是相当的粗暴。因此，在Join优化的时候，通常都会在内表建立索引，以此降低算法的开销，MySQL数据库中使用较多的就是这种算法。
  - 外表中的每条记录通过内表的索引进行访问，因为索引查询的成本是比较固定的（每次index lookup的成本就是b+树的高度），故优化器都倾向于使用记录数少的表作为外表。
  - 如果是通过表的主键索引进行Join，即使是大数据量的情况下，INLJ的效率亦是相当不错的。
  - 大部分时候MySQL的INLJ慢，是因为在进行Join的时候用到的索引并不是主键的聚集索引，而是辅助索引，这时INLJ的过程又需要多一步Fetch的过程，而且这个过程开销会相当的大。
  - 由于访问的是辅助索引，如果查询需要访问聚集索引上的列，那么需要进行回表取数据，看似每条记录只是多了一次回表操作，但这才是INLJ算法最大的弊端。
    For each row r in R do
        lookup r in S index
        if found s == r
        Then output the tuple

  c. Block Nested-Loop Join
  - BNLJ算法较Simple Nested-Loop Join的改进就在于可以减少内表的扫描次数，甚至可以和Hash Join算法一样，仅需扫描内表一次。
  - BNLJ算法多了一个Join Buffer用以缓存链接需要的列，然后以Join Buffer批量的形式和内表中的数据进行链接比较。
    For each tuple r in R do
        store used columns as p from R in join buffer
        For each tuple s in S do
            If p and s satisfy the join condition
            Then output the tuple

  d. Batched Key Access Join (需要MRR支持)
  - 开启BKAJ(默认关闭): SET optimizer_switch='mrr=on,mrr_cost_based=off,batched_key_access=on';


  Mysql5.6 新特性multi range read
  - MRR 根据rowid顺序地，批量地读取记录，从而提升数据库的整体性能。
  - MRR 的优化在于，并不是每次通过辅助索引读取到数据就回表去取记录，而是将其rowid给缓存起来，然后对rowid进行排序后，再去访问记录，
  - 这样就能将随机I/O转化为顺序I/O，从而大幅地提升性能。
  - MRR buffer大小由参数read_rnd_buffer_size控制，默认256K。
  - 开启MRR: set optimizer_switch='mrr=on,mrr_cost_based=off'; 


  left/right join VS inner join
  - 数据库在通过连接两张或多张表来返回记录时，都会生成一张中间的临时表，然后再将这张临时表返回给用户。
  A left/right join B on A.id=B.id where B.name='zhangsan'
  - on条件是在生成临时表时使用的条件，用来决定如何从 B 表中检索数据行。如果 B 表中没有任何一行数据匹配 ON 的条件, 将会额外生成一行所有列为 NULL 的数据。
  - where条件是在临时表生成好后，再对临时表进行过滤的条件。这时已经没有left join的含义（必须返回左边表的记录）了，条件不为真的就全部过滤掉。
  - left/right join时将过滤条件放在on和where中得到的结果集可能不一样的。
  - inner jion没这个特殊性，过滤条件放在on中和where中，返回的结果集是相同的。
  
    
  mysql数据量多大的时候需要分表：当mysql单表的数据量大于1000万行时，建议进行水平分拆。
  

  mysql如何优化查询
  1)索引没起作用的情况
  2)优化数据库结构
  3)分解关联查询
  4)优化LIMIT分页
    
  mysql中InnoDB表为什么要建议用自增列做主键:
  InnoDB表的数据写入顺序能和B+树索引的叶子节点顺序一致的话，这时候存取效率是最高的  
  
  
  mysql for update 表锁/行锁
  a. 未获取到数据的时候，mysql没有锁 （no lock）
  b. 获取到数据的时候，对约束字段进行判断，存在有索引的字段则进行row lock, 否则进行 table lock
  c. mysql进行row lock还是table lock只取决于是否能使用索引，而 使用'<>','like'等操作时，索引会失效，进行的是table lock
  
  Mysql索引会失效:
  1. 负向条件查询不能使用索引, 负向条件有：!=、<>、not in、not exists、not like 等。
  2. 索引列不允许为null
  3. 避免使用or来连接条件
  4. 模糊查询
  
 * 
 * @author chenzq  
 * @date 2019年5月12日 下午1:11:10
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
package com.asiainfo.database;