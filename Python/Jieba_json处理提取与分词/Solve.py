#-*- coding: gbk -*-
import os
import sys
import json
import Wordseg

filename = "ReportList"
page = [line.strip() for line in file(filename)]
WriteX = open("Data_Label","a")
for each in page:
    contents = json.loads(each)
    url = contents['url'].encode('utf-8')
    label = contents['rank'].encode('utf-8')
    title = contents['title'].encode('utf-8')
    corpuX = Wordseg.String_make_corpus(contents['Maintext'].encode('utf-8'))
    WriteX.write(url + '\t' + label + '\t' + title + '\t' + corpuX + '\n')