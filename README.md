
1.引用以大于号开头后面跟内容，二级引用以两个大于号开头  
> 本文由 [jaesonchen](https://github.com/jaesonchen/java-study/edit/master/README.md) 完成。  


2.换行（建议直接在前一行后面补两个空格）  
直接回车不能换行，可以在上一行文本后面补两个空格，这样下一行的文本就换行了。  
或者就是在两行文本直接加一个空行。也能实现换行效果，不过这个行间距有点大。  


3.标题使用不同数量的"#"来标识是什么层级，可以对应于HTML里面的H1-H6，#后面需要跟一个空格  
# 标题1  
## 标题2

###### 标题6  


4.图片定义使用惊叹号开头，中括号里是替代文字，小括号里是图片的地址。链接不带惊叹号  
![hello world](https://user-gold-cdn.xitu.io/2018/5/20/1637b08b98619455?w=312&h=305&f=png&s=22430)  


5.强调 使用多个星号/下划线  
*强调* 或者 _强调_ (示例：斜体)  
**加重强调** 或者 __加重强调__ (示例：粗体)  
***特别强调*** 或者 ___特别强调___ (示例：粗斜体)  


6.表格 （建议在表格前空一行，否则可能影响表格无法显示）  

| 隔离级别 | 脏读 | 不可重复读 | 幻影读 |
| :---: | :---: | :---:| :---: |
| READ-UNCOMMITTED | √ | √ | √ |
| READ-COMMITTED | × | √ | √ |
| REPEATABLE-READ | × | × | √ |
| SERIALIZABLE | × | × | × |

  
7.代码块 由两个反单引号包围，代码高亮由3个反单引号包围  
```
public String hello(String name) {
    return "hello " + name;
}
```
  
8.分隔线 可以在单独一行里输入3个或以上的短横线、星号或者下划线实现。短横线和星号之间可以输入任意空格。 

---  
* * *  
___  

9.列表 以"1. 2.", "*", "-" 开头，二级列表以4个空格开头，符号后面必须有一个空格
- [事务隔离级别(图文详解)](#事务隔离级别图文详解)
    - [什么是事务?](#什么是事务)
    - [事物的特性(ACID)](#事物的特性acid)
    - [并发事务带来的问题](#并发事务带来的问题)
    - [事务隔离级别](#事务隔离级别)
    - [实际情况演示](#实际情况演示)
        - [脏读(读未提交)](#脏读读未提交)
        - [避免脏读(读已提交)](#避免脏读读已提交)
        - [不可重复读](#不可重复读)
        - [可重复读](#可重复读)
        - [防止幻读(可重复读)](#防止幻读可重复读)

