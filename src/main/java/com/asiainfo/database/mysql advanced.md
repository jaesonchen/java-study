# 开启慢查询
开启慢查询日志，可以让MySQL记录下查询超过指定时间的语句，通过定位分析性能的瓶颈，才能更好的优化数据库系统的性能。  
查询是否开了慢查询（slow_query_log 慢查询开启状态、slow_query_log_file 慢查询日志存放的位置）  
`show variables like 'slow_query%';`  
开启慢日志（1表示开启，0表示关闭，也可以在my.cnf 文件中设置）  
`set global slow_query_log=1;`  
查看慢查询超时时间（long_query_time默认10秒）  
`show variables like 'long_query%';`  

# 查询缓存
my.cnf加入以下配置，重启Mysql开启查询缓存  
```
query_cache_type=1
query_cache_size=600000
```
Mysql执行以下命令也可以开启查询缓存  
```
set global  query_cache_type=1;
set global  query_cache_size=600000;
```  
开启查询缓存后在同样的查询条件以及数据情况下，会直接在缓存中返回结果。这里的查询条件包括查询本身、当前要查询的数据库、客户端协议版本号等一些可能影响结果的信息。因此任何两个查询在任何字符上的不同都会导致缓存不命中。此外，如果查询中包含任何用户自定义函数、存储函数、用户变量、临时表、Mysql库中的系统表，其查询结果也不会被缓存。   

缓存建立之后，Mysql的查询缓存系统会跟踪查询中涉及的每张表，如果这些表（数据或结构）发生变化，那么和这张表相关的所有缓存数据都将失效。   

缓存虽然能够提升数据库的查询性能，但是缓存同时也带来了额外的开销，每次查询后都要做一次缓存操作，失效后还要销毁。 因此，开启缓存查询要谨慎，尤其对于写密集的应用来说更是如此。如果开启，要注意合理控制缓存空间大小，一般来说其大小设置为几十MB比较合适。此外，还可以通过sql_cache和sql_no_cache来控制某个查询语句是否需要缓存：  
`select sql_no_cache count(*) from usr;`


# 常用的sql
## 列转行
```
SELECT userid,
    SUM(CASE `subject` WHEN '语文' THEN score ELSE 0 END) as '语文',
    SUM(CASE `subject` WHEN '数学' THEN score ELSE 0 END) as '数学',
    SUM(CASE `subject` WHEN '英语' THEN score ELSE 0 END) as '英语',
    SUM(CASE `subject` WHEN '政治' THEN score ELSE 0 END) as '政治',
    SUM(score) as total
FROM tb_score 
GROUP BY userid
    
SELECT userid,
    SUM(IF(`subject`='语文',score,0)) as '语文',
    SUM(IF(`subject`='数学',score,0)) as '数学',
    SUM(IF(`subject`='英语',score,0)) as '英语',
    SUM(IF(`subject`='政治',score,0)) as '政治',
    SUM(score) as total
FROM tb_score 
GROUP BY userid
```

## 行转列
```
SELECT userid,'语文' AS course,cn_score AS score FROM tb_score1
UNION
SELECT userid,'数学' AS course,math_score AS score FROM tb_score1
UNION
SELECT userid,'英语' AS course,en_score AS score FROM tb_score1
UNION
SELECT userid,'政治' AS course,po_score AS score FROM tb_score1
ORDER BY userid
``` 
    
## 查询名字重复的学生
```
select id, name
from student
where name in (select name from student group by name having(count(*) > 1));
```
    
## 查询每门课成绩都不低于80的学生
```
select distinct sid
from student_course
where sid not in (select sid from student_course where score < 80);


select sid from student_course
group by sid
having min(score) >= 80;
```
    
## 查询每个学生的总成绩：
```
select id, sum(score)
from student 
left join student_course on student.id=student_course.sid
group by id;
```
    
## 总成绩最高的3位学生
```
select sid, sum(score) as sum_score
from student_course 
group by sid
order by sum_score desc 
limit 3;
```    
    
## 课程1成绩第2高的学生
```
select score 
from student_course 
where cid = 1 
group by score 
order by score desc limit 1,1
```
