[TOC]
## 0x00 前言
最近学了怎么用python把获取到的数据往MySQL的数据库里插入（即`MySQLmod.py`的实现，关于这个以后有空再写个文章说一说），毕竟数据库的管理数据能力是一绝，但是，我们好不容易插入的数据，如何提取出来呢？
此处使用面向`pyspider`抓取后存放数据的MySQL数据库提取数据的方式，来简要说明一下如何操作MySQL.Connector。

----------


## 0x01 配置环境
+ **six** [`Documentation`](https://pythonhosted.org/six/)
 + six - Python 2 and 3 compatibility utilities
  INSTALLED: 1.10.0 (latest)
 + Six 提供了一些简单的工具用来封装 Python 2 和 Python 3 之间的差异性
+ **mysql.connector** [`Guide`](http://dev.mysql.com/doc/connector-python/en/) [`Example`](http://dev.mysql.com/doc/connector-python/en/connector-python-examples.html) MySQL官方的纯Python驱动
 + mysql-connector-python - MySQL driver written in Python
  INSTALLED: 2.0.4 (latest)
 + 不多说，这个自然是重点，本次以此MySQL驱动为例
+ **MySQL-python** (1.2.5) 封装了MySQL C驱动的Python驱动
	+ 可以把两个都装上，使用的时候再决定用哪个


----------
## 0x02 主要函数测试与演示
本段 *(即0x02)* 摘自网络： [廖雪峰的官方网站](http://www.liaoxuefeng.com/wiki/0014316089557264a6b348958f449949df42a6d3a2e542c000/0014320107391860b39da6901ed41a296e574ed37104752000)

###安装MySQL驱动
由于MySQL服务器以独立的进程运行，并通过网络对外服务，所以，需要支持Python的MySQL驱动来连接到MySQL服务器。MySQL官方提供了`mysql-connector-python`驱动，但是安装的时候需要给pip命令加上参数`--allow-external`：

```
$ pip install mysql-connector-python --allow-external mysql-connector-python
```
演示如何连接到MySQL服务器的test数据库
```
# 导入MySQL驱动:
>>> import mysql.connector
# 注意把password设为你的root口令:
>>> conn = mysql.connector.connect(user='root', password='password', database='test')
>>> cursor = conn.cursor()
# 创建user表:
>>> cursor.execute('create table user (id varchar(20) primary key, name varchar(20))')
# 插入一行记录，注意MySQL的占位符是%s:
>>> cursor.execute('insert into user (id, name) values (%s, %s)', ['1', 'Michael'])
>>> cursor.rowcount
1
# 提交事务:
>>> conn.commit()
>>> cursor.close()
# 运行查询:
>>> cursor = conn.cursor()
>>> cursor.execute('select * from user where id = %s', ['1'])
>>> values = cursor.fetchall()
>>> values
[('1', 'Michael')]
# 关闭Cursor和Connection:
>>> cursor.close()
True
>>> conn.close()
```
执行INSERT等操作后要调用`commit()`提交事务；
MySQL的SQL占位符是`%s`。

----------


## 0x03 脚本阅读与解释

```
#-*- coding: gbk -*-

import sys
import json
import mysql.connector
import six

reload(sys)
sys.setdefaultencoding('utf-8')

userName = 'root'
passWord = '123456'
hostAddr = '127.0.0.1'
portAddr = '5200'
dataBase = 'resultdb'
char_Set = "utf8"

def process():
    # Example Below:
    # conn = mysql.connector.connect(user='root',host='127.0.0.1', port='5200',db='resultdb',charset="utf8")
    conn = mysql.connector.connect(user = userName, password = passWord, host = hostAddr, port = portAddr, db = dataBase, charset = char_Set)
    cursor = conn.cursor() # Locate the Cursor
    sql="SELECT result FROM testTable;" # SQL command
    cursor.execute(sql) # Execute the command
    #alldata = cursor.fetchall()
    """
    for f in cursor.description :
        print f[0]
    """
    fields = [f[0] for f in cursor.description]
    for row in cursor :
        for index in list(row) :
            line = json.dumps(json.loads(six.text_type(index)), ensure_ascii=False)
            print line
            # js = json.loads(line)
            # print js['Title']
    cursor.close()
    conn.close()


def old(): 
    for line in sys.stdin:
        line = line.rstrip()
        print(json.dumps(json.loads(line), ensure_ascii=False))
        #print line
        #contents = json.loads(line)
        #print json.dumps(line, ensure_ascii=False).encode('utf8')
        #print contents['Title']
        
if __name__ == '__main__' :
    process()


```
+ 自定义变量
	+ `userName` = 登陆该MySQL所使用的用户名
	+ `passWord` = 对应登陆用户名的密码
	+ `hostAddr` = 访问的MySQL所在的机器IP地址（可以使用可解析的域名）
	+ `portAddr` = 访问的机器为MySQL开放的端口
	+ `dataBase` = 访问MySQL中哪一个数据库
	+ `char_Set` = 字符编码（常使用utf8）
	+ `sql` = 需要给MySQL发送的SQL命令（获取数据通常使用SELECT语句）
+ 此处使用的json函数
	+  `json.dumps()` 编码：将Python对象编码转换成Json串
	+ `json.loads()` 解码：把Json串解码转换成Python对象 
	


----------


## 0x04 调用方法

``` sh
$ nohup python xxx.py > Filename &
```
其中`xxx.py`是脚本文件的名称，`Filename`为自定义输出文件的名称，举例说明：
`nohup` python Getdata.py > Data.txt `&`
意为 `后台挂起任务` > 使用Python > 运行获取数据 > 导出到Data.txt文件中
