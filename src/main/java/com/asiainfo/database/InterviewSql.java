package com.asiainfo.database;

/**   
    列转行：
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
    
    行转列：
    SELECT userid,'语文' AS course,cn_score AS score FROM tb_score1
    UNION
    SELECT userid,'数学' AS course,math_score AS score FROM tb_score1
    UNION
    SELECT userid,'英语' AS course,en_score AS score FROM tb_score1
    UNION
    SELECT userid,'政治' AS course,po_score AS score FROM tb_score1
    ORDER BY userid
    
    
    查询有重复得记录：
    select id,name
    from student
    where name in (
        select name from student group by name having(count(*) > 1)
    ) order by name;
    
    
    查询每门课成绩都不低于80的学生：
    select distinct sid
    from student_course
    where sid not in (select sid from student_course where score < 80);
    
    
    查询每个学生的总成绩：
    select name,sum(score)
    from student left join student_course
    on student.id=student_course.sid
    group by sid;
    
    
    总成绩最高的3位学生：
    select sid,sum(score) as sum_score
    from student_course 
    group by sid
    order by sum_score desc 
    limit 3;
    
    
    课程1成绩第2高的学生：
    select score from student_course 
    where cid = 1 
    group by score 
    order by score desc limit 1,1
 * 
 * @author chenzq  
 * @date 2019年5月12日 下午8:23:10
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class InterviewSql {

}
