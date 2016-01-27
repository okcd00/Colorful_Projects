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