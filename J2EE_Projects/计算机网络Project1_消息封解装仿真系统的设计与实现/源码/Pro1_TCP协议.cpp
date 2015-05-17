#include <cmath> 
#include <dos.h>
#include <time.h> 
#include <cctype>
#include <cstdio>
#include <string>
#include <fstream>
#include <cstdlib>
#include <cstring>
#include <iostream>
#include <windows.h>
#include <algorithm>
using namespace std;
//物理层		0/1 
//数据链路层	译帧
//网络层		路由信息（MAC&Protocol） 
//传输层		IP报头 	（IP.addr） 
//应用层 		ICMP信息（Division） 
//自然语言		Original Message 
#define Max(a,b) ((a)>(b)?(a):(b))
#define Min(a,b) ((a)<(b)?(a):(b))

//Global
	const string ipfrom="192.168.191.001";
	const string ipto="192.168.191.002";
	const string protocal="tcp";
	const string macfrom="00-50-56-C0-00-01";
	const string macto="07-25-25-C0-D0-00";
	int buffer[7]={0};

bool cmp(const int a, const int b)
{
	return a > b;
}

char getasc(char cc[7])
{
	int c[7]={0};
	for(int i=0;i<7;i++)
	{
		c[i]=cc[i]-'0';
		cout<<"C"<<i<<"="<<c[i]<<endl;
	}
	char temp='A'-65+c[6]+2*c[5]+4*c[4]+8*c[3]
			          +16*c[2]+32*c[1]+64*c[0];
	return temp;
}

void print01(int an)
{
	int t=an;
	int cmpt=64;
	while(cmpt)
	{
		if(t>=cmpt)
		{printf("1");t-=cmpt;}
		else printf("0"); 
		cmpt/=2;
	}
}

void sen()
{
	string mes;
	cout<<"Please Input Your Message(No blanks):";	cin>>mes;
	cout<<"==Message Submitted!=="<<endl;
	string icmp="mail";
	string l1="icmp_head="+icmp+"$"+mes;
	cout<<"【应用层】:"<<endl;for(int i=0;i<l1.length();i++)cout<<l1[i];cout<<endl; 
	string l2="trans_head="+ipfrom+"<:>"+ipto+"$"+l1;
	cout<<"【传输层】:"<<endl;for(int i=0;i<l2.length();i++)cout<<l2[i];cout<<endl; 
	string l3="netw_head="+macfrom+"<"+protocal+">"+macto+"$"+l2;
	cout<<"【网络层】:"<<endl;for(int i=0;i<l3.length();i++)cout<<l3[i];cout<<endl; 
	//数据链路层东西有点太多了，稍微偷个懒就只加一个ArrivalTime好了……
	time_t timer;
	struct tm* tblock;
	timer=time(NULL);
	tblock=localtime(&timer);
	string asct=asctime(tblock);
	//Mon Dec 22 22:52:30 2014
	string l4="static_head="+asct+"$"+l3;
	cout<<"【数据链路层】:"<<endl;for(int i=0;i<l4.length();i++)cout<<l4[i];cout<<endl; 
	cout<<"【物理层开始0/1处理】"<<endl;
	freopen("Trans.txt","w",stdout);
	for(int i=0;i<l4.length();i++)
	{
		int ascnum=l4[i]-'A'+65;
		print01(ascnum);
	}	fclose(stdout);
	cout<<"【物理层0/1处理完成】"<<endl;
}

void rec()
{
	int pos=0;
	char get[7]={'0'};
	char tl4[1024];
	freopen("Trans.txt","r",stdin);
	while(scanf("%c%c%c%c%c%c%c",&get[0],&get[1],&get[2],
			       &get[3],&get[4],&get[5],&get[6])!=EOF)
	{
		system("cls");
		cout<<"Reading... "<< pos <<"letters."<<endl; 
		char now=getasc(get);
		tl4[pos++]=now;
	}	tl4[pos]='\0';
	cout<<"==Reading Operation Over=="<<endl;
	fclose(stdin);
	string l4=tl4;
	cout<<"【数据链路层】:"<<endl;for(int i=0;i<l4.length();i++)cout<<l4[i];cout<<endl;
	string l3=l4.substr(38,l4.length()-38);
	cout<<"【网络层】:"<<endl;for(int i=0;i<l3.length();i++)cout<<l3[i];cout<<endl;
	string l2=l3.substr(50,l3.length()-50);
	cout<<"【传输层】:"<<endl;for(int i=0;i<l2.length();i++)cout<<l2[i];cout<<endl;
	string l1=l2.substr(45,l2.length()-45);
	cout<<"【应用层】:"<<endl;for(int i=0;i<l1.length();i++)cout<<l1[i];cout<<endl;
	string l0=l1.substr(15,l1.length()-15);
	cout<<"【信息】:"<<endl;for(int i=0;i<l0.length();i++)cout<<l0[i];cout<<endl;
}

int main()
{
	cout<<"-------------------------------------"	<<endl
		<<"|  [NetWork_Project1] 五层协议模拟  |"	<<endl
		<<"|  ---Author_Chendian IOT 20125209  |"	<<endl
		<<"-------------------------------------"	<<endl;
	system("pause");
	system("CLS");
	cout<<"@ S> Sender"		<<endl
		<<"@ R> Receiver"	<<endl;
	while(1)
	{
		cout<<"请输入您的角色:"; 
		string choice;	cin>>choice;
		if(tolower(choice[0])=='s'){sen();break;}
		else if(tolower(choice[0])=='r'){rec();break;}
		else cout<<"Invalid Input , Please Try again"<<endl;
	}
	cout<<("Thanks for using. _Author okcd00");
	system("pause"); 
	return 0;
}

