## 0x00 前言
之前说了怎么把数据从数据库里提取出来，然而，我们提取出来的是json串，对于想要进一步处理这些数据的孩纸们而言，还是喜欢用'\t'分割的数据来作训练集吧？（当然会用json.loads()然后当成dict来计算的孩纸们我为你们鼓掌）
最近学校的导师给了这么一个任务，大概就是要做类似的这么一件事吧，写好了所以来这里记录一下~
那么，扩展开来一点说，对于一个json串格式的数据集，我们需要提取其中的一部分，在懒得用awk来拆分拾取所需情报的情况下，应该怎么做呢？
此外，对于DataMining和MachineLearing的孩子们，还想分个词，啊啊啊是不是好烦的感觉？


----------


## 0x01 环境准备
+ 结巴分词
	+ [Github Source](https://github.com/fxsjy/jieba)
	+ Python 2.x 下的安装
		+ 全自动安装：easy_install jieba 或者 pip install jieba
		+ 半自动安装：先下载 http://pypi.python.org/pypi/jieba/ ，解压后运行`python setup.py install`
		+ 手动安装：将jieba目录放置于当前目录或者site-packages目录
		+ 通过import jieba 来引用 （第一次import时需要构建Trie树，需要几秒时间）
	+ Python 3.x 下的安装
		+ https://github.com/fxsjy/jieba/tree/jieba3k
		+ Git方式如下：
```sh
$ git clone https://github.com/fxsjy/jieba.git
$ git checkout jieba3k
$ python setup.py install
```
+ 【Update】 ***代码对 Python 2/3 均兼容***

全自动安装：`easy_install jieba` 或者 `pip install jieba` / `pip3 install jieba`
半自动安装：先下载 http://pypi.python.org/pypi/jieba/ ，解压后运行 `python setup.py install`
手动安装：将 `jieba` 目录放置于当前目录或者 `site-packages` 目录
通过 `import jieba` 来引用

+ Sumup:
仔细想想……似乎就算准备好了（啊当然你要解析json你肯定得有json库对不对，然后别跟我说你没装好python啊……这些理所当然的东西我都不会算在环境配置里的哦）


----------


## 0x02 代码及使用说明
### 分词类：Wordseg.py

``` py
import os
import sys
import jieba

def Path_make_corpus(dirname):
    corpus = ""
    if os.path.isdir(dirname):
        filenames = os.listdir(dirname)
        for filename in filenames:
            f = open(dirname + '/' + filename, 'r')
            f_content = f.read()
            f_content = ' '.join(f_content.split())
            if f_content != ' ' and f_content != '\n' and f_content != '':
                 words_seg = jieba.lcut(f_content)
                 for i in range(len(words_seg)):
                     words_seg[i] = words_seg[i].encode('utf-8')
                 corpus = ' '.join(words_seg)
            f.close()
    return corpus


def File_make_corpus(filename):
    corpus = ""
    if os.path.isfile(filename):
        f = open(filename,'r')
        contents = f.readlines()
        for i in range(len(contents)):
            f_content = contents[i]
            if f_content != ' ' or f_content != '\n' or f_content != '':
                words_seg = jieba.lcut(f_content)
                for j in range(len(words_seg)):
                    words_seg[j] = words_seg[j].encode('utf-8')
                corpus = ' '.join(words_seg)
        f.close()
    return corpus


def String_make_corpus(text):
    corpus = ""
    if isinstance(text, basestring):
        words_seg = jieba.lcut(text)
        for i in range(len(words_seg)):
            words_seg[i] = words_seg[i].encode('utf-8')
        corpus = ' '.join(words_seg)
    return corpus
```
> `Path_make_corpus(dirname)`：传入包含所有文件名的文本路径进行分词
> `File_make_corpus(filename)`：传入待分词文件的名称进行分词
> `String_make_corpus(text)`：传入字符串进行分词


### 主函数：Solve.py

```
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
```
> filename = "`输入文件名`"
> WriteX = open("`输出文件名`", "a")

两个文件放于同一文件夹中，python运行主函数即可。
这只是一个简单的例子，其他延伸和衍生的用法可以自行挖掘，祝数据挖掘愉快。