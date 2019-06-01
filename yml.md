
## 注释
和properties相同，使用#作为注释，YAML中只有行注释。
    
## 基本格式要求
- YAML大小写敏感；
- 使用缩进代表层级关系；
- 缩进只能使用空格，不能使用TAB，不要求空格个数，只需要相同层级左对齐（一般2个或4个空格）
    
## 对象
使用冒号代表，格式为key: value。冒号后面要加一个空格：    
`key: value`
    
可以使用缩进表示层级关系：
    
```
key: 
    child-key: value
    child-key2: value2
```
    
YAML中还支持流式(flow)语法表示对象：
    
```
key: {child-key: value, child-key2: value2}
```

## 数组
使用一个短横线加一个空格代表一个数组项：
    
```
language:
    - Java
    - Go
```
    
一个相对复杂的例子：
    
```
companies:
    -
        id: 1
        name: company1
        price: 200
    -
        id: 2
        name: company2
        price: 500
```
    
数组也可以使用流式(flow)的方式表示：
    
```
companies: [{id: 1,name: company1,price: 200W},{id: 2,name: company2,price: 500W}]
```
    
## 常量
YAML中提供了多种常量结构，包括：整数，浮点数，字符串，NULL，日期，布尔，时间。
    
```
boolean: 
    - TRUE  #true,True都可以
    - FALSE  #false，False都可以
float:
    - 3.14
    - 6.8523015e+5  #可以使用科学计数法
int:
    - 123
    - 0b1010_0111_0100_1010_1110    #二进制表示
null:
    nodeName: 'node'
    parent: ~  #使用~表示null
string:
    - 哈哈
    - 'Hello world'  #可以使用双引号或者单引号包裹特殊字符
    - newline
      newline2    #字符串可以拆成多行，每一行会被转化成一个空格
date:
    - 2018-02-17    #日期必须使用ISO 8601格式，即yyyy-MM-dd
datetime: 
    -  2018-02-17T15:02:31+08:00    #时间使用ISO 8601格式，时间和日期之间使用T连接，最后使用+代表时区
```
    
## 特殊符号
### ---
YAML可以在同一个文件中，使用---表示一个文档的开始；比如Springboot中profile的定义：
    
```
server:
    address: 192.168.1.100
---
spring:
    profiles: dev
    server:
        address: 127.0.0.1
---
spring:
    profiles: test
    server:
        address: 192.168.1.120
```

### ...
和---配合使用，在一个配置文件中代表一个文件的结束：
    
```
---
time: 20:03:20
player: Sammy Sosa
action: strike (miss)
...
---
time: 20:03:47
player: Sammy Sosa
action: grand slam
...
```
    
### !!
YAML中使用!!做类型强行转换：
    
```
# 把数字和布尔类型强转为字符串
string:
    - !!str 54321
    - !!str true
```
    
### > |
 >在字符串中折叠换行，| 保留换行符，这两个符号是YAML中字符串经常使用的符号：
    
```
accomplishment: >
 Mark set a major league
 home run record in 1998.
stats: |
 65 Home Runs
 0.278 Batting Average
```
    
那么结果是：
    
```
accomplishment=Mark set a major league home run record in 1998.
stats=65 Home Runs
 0.278 Batting Average,
```
    
### 引用
重复的内容在YAML中可以使用&来完成锚点定义，使用*来完成锚点引用
    
```
hr:
- Mark McGwire
- &name Sammy Sosa
rbi:
- *name 
- Ken Griffey
```
    
结果为：
    
```
{rbi=[Sammy Sosa, Ken Griffey], hr=[Mark McGwire, Sammy Sosa]}
```

