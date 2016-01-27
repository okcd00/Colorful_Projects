## 0x00 前言
之前就听说过百度开源了一个数据可视化的项目，叫做Echarts，觉得甚是开心但一直没想起来用过，恰好前几日导师需要做一个数据可视化的东西，简单来说就是做一个热力图，自然就想到了这个，于是现学现卖做好了展示和数据处理，特此撰文记录一下。
哦对了，效果图：
![Heatmap Example](http://img.blog.csdn.net/20160125112653982)

## 0x01 简介
+ 一些传送门
	+ [Homepage](http://echarts.baidu.com/index.html) 
	最新的Echarts3.0的主页，旧版的2.0主页传送门在[这里](http://echarts.baidu.com/echarts2/)
	+ [Github](https://github.com/ecomfe/echarts)
	毕竟是开源项目，源码自然在Github上有，自己改变一下也是不错的
	+ [Tutorial](http://echarts.baidu.com/tutorial.html#ECharts)
	教程，我也是从这里先开始看的，比较有用的部分嘛，就是那个“[5 分钟上手 ECharts](http://echarts.baidu.com/tutorial.html#5%20%E5%88%86%E9%92%9F%E4%B8%8A%E6%89%8B%20ECharts)”了，主要是给了一个模版html，这个很关键，没有这个就算做好了js都不知道往哪里放（关于这里，我自己又稍稍“改进”了一下，后面会放出更傻瓜一点的一个模版html）。
	+ [Examples](http://echarts.baidu.com/examples.html)
	实例，这个可不是普通的实例那么简单哦，这是一个类似Markdown撰文一样连敲代码都可以可视化的实时编程，之后讲操作步骤的时候会详细说明。
	+ [API](http://echarts.baidu.com/api.html)
	还没有具体研究过，不过给出API毕竟是好的~
	+ [ConfigBook](http://echarts.baidu.com/option.html#title)
	特别重要！在页面即时编程的时候基本上就得把这个配置项手册一直开着了，想要个什么功能或者想拿个参数改个参数都在这里翻吧，我就是不停地在这个手册里找，一个一个完成被交代的任务和功能的。
	+ [js_Download](http://echarts.baidu.com/download.html)
	通常来说，我们各取所需，需要什么图就下个什么图的js下来就好了，但是看作者都精简成这么小了，加上我们也不知道什么时候可能又要用个新的图，直接下载个完整版的好了，半个M都没到。

## 0x02 Echarts的使用
### option.js的编写
关于调试与使用方面，Echarts是线上线下都可以的，线上的话，就是可视化即时编程，如下图，在左边编辑的代码在右边就会实时显示出来效果，初始代码是原先就给好的，依葫芦画瓢是我们这样的新手需要做的，毕竟，数据和各种参数都那么明显的写出来了，我们就到处改改看效果也能对功能猜个八九不离十吧：

![可视化即时编程](http://img.blog.csdn.net/20160125113329351)

然后，在这其中我们能看到很多像dict的数据类型，Key-Value对的感觉，没错这些就是参数了，那么除了每个Example里给出的例子，我们想增加其他的参数该如何是好呢？
没错，去[配置项手册](http://echarts.baidu.com/option.html#title)里找吧。
比如说我们想让输出的浮点数有个几位的保留小数，然后我们通过查表可以发现‘precision’这个变量，于是在对应的地方加上这个键，并赋值即可，如下：

``` js
visualMap: {
		min: 0.0000,
		max: 0.3333,
		calculable: true,
		precision: 4, #NEW#
		orient: 'horizontal',
		left: 'center',
		bottom: '5%'
	},
```
之前我遇到的问题就是，我想自定义点击/移动事件时出现的提示框中，文本的内容，于是我就找到了这个，如下图：
![fomatter](http://img.blog.csdn.net/20160125134321105)
然后就一点点尝试下怎么传参呀，输出看看这些abcd都是写啥呀之类的，写了个这样的：

``` js
tooltip: {
		position: 'right',
		triggerOn: 'click',
		enterable: true,
		formatter:function (params,ticket,callback) {
		    var str = params.value;
		    var x = str[0];
		    var y = str[1];
		    var v = str[2];
		    var onstr = "";
        var res = '[Information]'+'<br/>';
        res+='Position:('+ x + ',' + y + ')' + ' Value: ' + v +'<br/>';
        res+='online: '+ online[y] +'<br/>';
        res+='offline: '+ offline[x] +'<br/>';
        setTimeout(function (){callback(ticket, res);}, 500);
        return 'loading...';
        }
	},
```

哈哈，是不是感觉很不错呀，反正就是在提供的示例里自己尝试着改一改，当觉得不错的时候就复制粘贴下来自己用文本新建一个js文件。这就是你自己的option.js代码啦~

### option.js的使用与html的形成
js毕竟不能直接用，它是要为html服务的，那么我们写好了自己的option.js之后，该怎么使用它呢？怎么在自己的网页上使用显示出来呢？
####官方的样例
那么我们从官方给的例子入手吧——
在官方的[教程](http://echarts.baidu.com/tutorial.html#5%20%E5%88%86%E9%92%9F%E4%B8%8A%E6%89%8B%20ECharts)中，给出了这样一个用来展现这样一个柱状图的样例：

```
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>ECharts</title>
    <!-- 引入 echarts.js -->
    <script src="echarts.js"></script>
</head>
<body>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width: 600px;height:400px;"></div>
    <script type="text/javascript">
        // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));

        // 指定图表的配置项和数据
        var option = {
            title: {
                text: 'ECharts 入门示例'
            },
            tooltip: {},
            legend: {
                data:['销量']
            },
            xAxis: {
                data: ["衬衫","羊毛衫","雪纺衫","裤子","高跟鞋","袜子"]
            },
            yAxis: {},
            series: [{
                name: '销量',
                type: 'bar',
                data: [5, 20, 36, 10, 10, 20]
            }]
        };

        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    </script>
</body>
</html>
```

我们新建一个html文件（用记事本新建改后缀就行），然后把上面的贴进去，之后把下载下来的`echarts.min.js` / `echarts.common.min.js` 还是什么其他的名字，都改成echarts.js，放在这个html同一个目录下，双击html文件就可以看到这个柱状图了。

#### 自己的图表
我们可以从刚刚那个样例中提取出这样一个信息：
```
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>ECharts</title>
    <script src="echarts.js"></script>
</head>
<body>
    <div id="main" style="width: 600px;height:400px;"></div>
    <script type="text/javascript">
        var myChart = echarts.init(document.getElementById('main'));

        // OPTIONS.JS START HERE
        var option = {...}
        // OPTIONS.JS ENDS HERE
        myChart.setOption(option);
    </script>
</body>
</html>
```

我们之前在官网的example里自己实现的例子，只要替换掉这里的`var option = {...}`就可以了对不对？
然而，option.js其实每次还是挺长的，要想替换掉得整个选中删除再粘贴，也挺麻烦的不是吗？
于是我写了个这样的html模版，不用复制进来，只要把option.js也和html放在同一个目录下，html会自己去找js文件来用的：
```
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>ECharts</title>
    <!-- 引入 echarts.js -->
    <script src="echarts.js"></script>
</head>
<body>
    <div id="main" style="width: 800px;height:600px;"></div> 
    <script type="text/javascript"> // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
	</script>
	<script type="text/javascript" src = "option.js"> // 指定图表的配置项
	</script>
	<script type="text/javascript">	// 使用刚指定的配置项显示图表。
        myChart.setOption(option);
    </script>
</body>
</html>
```

#### 再优化试试
啊当然，你们觉得这样就结束了么？
1. 使用超链接的时候不觉得超链接的颜色太暗了吗，背景下根本看不清啊
2. 数据啊，问题是数据很多，维护起js代码文件来讨厌呀不是吗？
哦……
为什么不把数据文件单独列出来再做个新的js文件呢？

##### 一个简单的C++制作data.js的例子：

``` C++
#include <string>
#include <cstdio>
#include <cstring>
#include <iostream>
using namespace std;

//请在此处按顺序输入三个文件名 
const char* dataFile = "data";
const char* onlineFile = "online";
const char* offlineFile = "offline";

//此处为输出javascript文件名，通常不需改动
const char* outputFile = "data.js";

void getData()
{
	printf("\nvar data = [");
	freopen(dataFile,"r",stdin);
	for(int i=1;i<=30;i++)
	{
		for(int j=1;j<=30;j++)
		{
			double t = 0.0;
			cin>>t;
			printf("[%d,%d,%f],",i-1,j-1,t);
		}
		printf("\n");
	}
	printf("];\n");
	fclose(stdin);
}

void getOnline()
{
	printf("\nvar online = [");
	freopen(onlineFile,"r",stdin);
	for(int i=1;i<=30;i++)
	{
		printf("[");
		string buff="";
		int pos=0,cnt=0;
		getline(cin,buff);
		while(buff.length())
		{
			pos = buff.find("\t");
			if (pos != std::string::npos)
			{
				string t = "\""+ buff.substr(0,pos) +"\",";
				buff = buff.substr(pos+1);
				if(cnt) cout<<t;
				else cnt++;
			}
			else break;
		}
		printf("],\n");
	}
	printf("];\n");
}

void getOffline()
{
	printf("\nvar offline = [");
	freopen(offlineFile,"r",stdin);
	for(int i=1;i<=30;i++)
	{
		printf("[");
		string buff="";
		int pos=0,cnt=0;
		getline(cin,buff);
		while(buff.length())
		{
			pos = buff.find("\t");
			if (pos != std::string::npos)
			{
				string t = "\""+ buff.substr(0,pos) +"\",";
				buff = buff.substr(pos+1);
				if(cnt) cout<<t;
				else cnt++;
			}
			else break;
		}
		printf("],\n");
	}
	printf("];\n");
}

int main()
{
	freopen(outputFile,"w",stdout);
	getData();
	getOnline();
	getOffline();
	fclose(stdout);
	return 0;
}
```

制作出来之后就是这种感觉：

``` js
var data = [[0,0,1],[0,1,1],];
var online = [['a','b'],['c','d'],];
var offline = [['e','f'],['g','h'],];
```
这样的文件是可以直接被 <...text/javascript...>读取的，所以我们来改进一下html模版如下：
> 代码简介：
> 1、超链接默认颜色天依蓝(66CCFF)，鼠标移动上去时候变白
> 2、配置项为option.js，数据项为data.js，从大家自己的模型输出数据变为数据项的可读代码文件，可以依照我上面的C++写法，当然各显神通起来大家肯定比我厉害多了~
> 3、HTML文件会自己去找这个文件名所对应的文件读入，自己整合成一个完整的数据可视化的html页面，这一部分，大家也可以放在自己的网页里使用，毕竟也都是一个echarts实例化的DOM块嘛~搬来搬去也挺方便的

```
<!DOCTYPE html>
<html>
<style type="text/css">
.color{color:#66CCFF; text-decoration:none;font-weight:bold;}/*链接设置*/
.color:visited{color:#3399CC; text-decoration:none;font-weight:bold;}/*访问过的链接设置*/
.color:hover{color:#FFFFFF; text-decoration:underline;font-weight:bold;}/*鼠标放上的链接设置*/
/*
取消下划线只要把text-decoration:underline修改成text-decoration:none
文字加粗font-weight:bold 如不需要加粗显示，那么删除font-weight:bold;就可以了
*/

</style>
<head>
    <meta charset="utf-8">
    <title>ECharts</title>
    <!-- 引入 echarts.js -->
    <script src="echarts.js"></script>
</head>
<body>
    <!-- 为ECharts准备一个具备大小（宽高）的Dom -->
    <div id="main" style="width: 800px;height:600px;"></div> 
	<script type="text/javascript" src = "data.js"> // 指定图表的数据项
	</script>
    <script type="text/javascript"> // 基于准备好的dom，初始化echarts实例
        var myChart = echarts.init(document.getElementById('main'));
	</script>
	<script type="text/javascript" src = "option.js"> // 指定图表的配置项
	</script>
	<script type="text/javascript">	// 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    </script>
</body>
</html>
```