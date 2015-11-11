#include <map>
#include <cmath> 
#include <vector>
#include <cctype>
#include <cstdio>
#include <string>
#include <cstdlib>
#include <cstring>
#include <fstream>
#include <iostream>
#include <algorithm>
using namespace std;

#define Max(a,b) ((a)>(b)?(a):(b))
#define Min(a,b) ((a)<(b)?(a):(b))

bool cmp(const int a, const int b)
{
	return a > b;
}

map<string,string> mark;
vector<int> freq;

void get_CNpair()	//get <Code-Name> pair into Cache
{
	string c,n; 
	mark.clear();
	freopen("CN-pair","r",stdin);
	while(cin>>c>>n) mark[c]=n; 
	fclose(stdin);
}

int IncludeChinese(char *str)  //0: English  1:Chinese
{
   char c;
   while(1)
   {
       c=*str++;
       if (c==0) break;
	   if (c&0x80)
       	if (*str & 0x80) return 1;
   }
   return 0;
}

int main()
{
	get_CNpair(); 
	freq.clear();
	char filename[30],line[1024],t[1024];
	ifstream File[3333];
	ofstream File2;  
	File2.open("Sum.txt",ios::out);
	int i=1;
	for(map<string,string>::iterator mit=mark.begin();mit!=mark.end();++mit,++i)
	{
		//cout<<mit->first<<":"<<mit->second<<endl;
		char addr[6];
		for(int j=0;j<6;j++) addr[j]=(mit->first)[j];
		sprintf(filename,"%s",addr);
		File[i].open(filename,ios::in);
		//File2<<"#"<<mit->first<<"#"<<endl;
		while(!File[i].eof())
		{
			File[i].getline(line,1023);
			string anti=line;
			int flag=0,rj=0,temp=0;
			for(int ri=0;ri<strlen(line);ri++)
			{
				if(flag==0 && line[ri]=='\t') {flag=1;continue;}
				if(flag==0) continue;
				if(line[ri]=='\t')
				{
					temp=0; 
					ri++;
					while(line[ri]!='.') 
					{
						temp=temp*10+(int)(line[ri]-'0');
						ri++;
					}	break;
				}
				else
				{
					if(line[ri]!=32 && line[ri]>0 && line[ri]<127);
					else if(isdigit(line[ri]));
					else t[rj++]=line[ri];	
				}
			}
			t[rj]='\0';
			if(strlen(t)>0 && IncludeChinese(t)) 
			{
				string 	t_str=t,
						rpls1=mit->first,
						rpls2=mit->second;
				//cout<<rpls1<<rpls2<<endl;
				if(t_str.find(rpls1)!=t_str.npos)
				t_str=t_str.replace(t_str.find(rpls1),rpls1.length(),"");
				//cout<<"rpls1:"<<t_str<<":"<<rpls1<<endl;
				if(t_str.find(rpls2)!=t_str.npos)
				t_str=t_str.replace(t_str.find(rpls2),rpls2.length(),"");
				//cout<<"rpls2:"<<t_str<<":"<<rpls2<<endl;

				if(t_str.length()>0)
				{
					bool blank_flag=1;
					for(int bi=0;bi<t_str.length();bi++)
					{
						if(t_str[bi]!=' ')
						{
							blank_flag=0;
							break;
						}
					}
					if(blank_flag)
					{
						File2<<line<<endl;
						freq.push_back(temp);
					}
				}
			}
		}
		File[i].close();
	}
	File2.close();
	freopen("freq.txt","w",stdout);
	//for(vector<int>::iterator vd=freq.begin();vd!=freq.end();++vd)
	for(int i=0;i<freq.size();i++)
	cout<<freq[i]<<endl;
	fclose(stdout);
	return 0;
}
