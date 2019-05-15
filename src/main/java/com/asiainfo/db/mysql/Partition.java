package com.asiainfo.db.mysql;

/**   
 * COLUMN分区是5.5开始引入的分区功能，只有RANGE COLUMN和LIST COLUMN这两种分区。
 * 整形支持：        tinyint,smallint,mediumint,int,bigint; 不支持decimal和float
 * 时间类型支持：date,datetime
 * 字符类型支持：char,varchar,binary,varbinary; 不支持text,blob
 * 
  1. RANGE 分区
  - 当往分区列中插入null值RANG 分区会将其当作最小值来处理即插入最小的分区中
    CREATE TABLE members (
        id INT,
        birthday DATE NOT NULL
    )
    PARTITION BY RANGE COLUMNS(birthday) (
        PARTITION p0 VALUES LESS THAN ('1960-01-01'),
        PARTITION p1 VALUES LESS THAN ('1970-01-01'),
        PARTITION p2 VALUES LESS THAN ('1980-01-01'),
        PARTITION p3 VALUES LESS THAN ('1990-01-01'),
        PARTITION p4 VALUES LESS THAN MAXVALUE
    );

  2. LIST 分区
  - LIST分区和RANGE分区非常的相似，主要区别在于LIST是枚举值列表的集合，RANGE是连续的区间值的集合。
  - 当往分区中插入不在枚举列表中的值是会插入失败，插入null值如果null值不在枚举列表中也同样失败。
    CREATE TABLE members (
        id INT,
        zipcode INT NOT NULL
    )
    PARTITION BY LIST COLUMNS(birthday) (
        PARTITION p0 VALUES IN (100001, 100002, 100003),
        PARTITION p1 VALUES IN (200001, 200002, 200003),
        PARTITION p2 VALUES IN (300001, 300002, 300003),
        PARTITION p3 VALUES IN (400001, 400002, 400003)
    );
    
  - 可以使用表达式字段转换
    CREATE TABLE listdate (
        id INT NOT NULL,
        hired DATETIME NOT NULL
    )
    PARTITION BY LIST( YEAR(hired) ) 
    (
        PARTITION a VALUES IN (1990),
        PARTITION b VALUES IN (1991),
        PARTITION c VALUES IN (1992),
        PARTITION d VALUES IN (1993)
    );
    
  3. 分区管理
  - alter table employees add PARTITION (PARTITION p4 VALUES LESS THAN MAXVALUE);
  - alter table employees drop  PARTITION p4;
  
  
    
 * HASH分区
 * MYSQL支持两种HASH分区，常规HASH(HASH)和线性HASH(LINEAR HASH)。
 * 基于给定的分区个数，将数据分配到不同的分区，HASH分区只能针对整数进行HASH，对于非整形的字段只能通过表达式将其转换成整数。
  1. 常规HASH
  - 常规hash是基于分区个数的取模（%）运算。根据余数插入到指定的分区。
    CREATE TABLE tbhash (
        id INT NOT NULL,
        store_id INT
    )
    PARTITION BY HASH(store_id)
    PARTITIONS 4;
  
  2. 线性HASH(LINEAR HASH)
  - LINEAR HASH和HASH的唯一区别就是PARTITION BY LINEAR HASH
    CREATE TABLE tbhash (
        id INT NOT NULL,
        hired DATE NOT NULL DEFAULT '1970-01-01'
    )
    PARTITION BY LINEAR HASH( YEAR(hired) )
    PARTITIONS 4;
    
  3. 分区管理
  - ALTER TABLE tblinhash add PARTITION partitions 4;
  - ALTER TABLE tablename REMOVE PARTITIONING ;


 * KEY分区
 * KEY分区和HASH分区相似，但是KEY分区支持除text和BLOB之外的所有数据类型的分区，而HASH分区只支持数字分区，
 * KEY分区不允许使用用户自定义的表达式进行分区，KEY分区使用系统提供的HASH函数进行分区。
  1. key 分区
    CREATE TABLE tb_key (
        id INT ,
        var CHAR(32) 
    )
    PARTITION BY KEY(var)
    PARTITIONS 10;

    CREATE TABLE tb_keyline (
        id INT NOT NULL,
        var CHAR(5)
    )
    PARTITION BY LINEAR KEY (var)
    PARTITIONS 3;

  2. 分区管理
  - ALTER TABLE tb_key add PARTITION partitions 3;
  - ALTER TABLE tb_key REMOVE PARTITIONING ;


 * @author chenzq  
 * @date 2019年5月12日 下午9:00:15
 * @version V1.0
 * @Copyright: Copyright(c) 2019 jaesonchen.com Inc. All rights reserved. 
 */
public class Partition {

}
