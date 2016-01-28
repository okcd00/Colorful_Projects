#include <math.h>
#include <time.h> 
#include <string>
#include <cstdio>
#include <cstring>
#include <cstdlib>
#include <fstream>
#include <iostream>
#include <algorithm>
#include <windows.h>
using namespace std;

#define Maxn 100
#define INF 233.333

//var initial
int idx=0, n=0, m=0, w[Maxn][Maxn]={0};
double p[Maxn][Maxn],lift[Maxn][Maxn],v[Maxn][Maxn],x[Maxn],y[Maxn],z[Maxn],range[Maxn],zero = 0.0000;
char index[2],enter,line[Maxn*Maxn];

ifstream chk,File[Maxn];
ofstream Filo[Maxn];
string dataFile,_dataFile = "./input/data";
string onlineFile,_onlineFile = "./input/online";
string offlineFile,_offlineFile = "./input/offline";
string outputFile,_outputFile = "data";
string htmlFile = "echarts";

void getOnline()
{
	cout<<"\nvar online = [";
	File[idx*3].open(onlineFile.c_str(),ios::in);
	//cout<<"Start Analysis "<<onlineFile<<endl;
	File[idx*3]>>n>>enter;
	string buff="";
	for(int i=1;i<=n;i++)
	{
		cout<<"[";
		int pos=0,cnt=0;
		File[idx*3].getline(line,Maxn*Maxn-1); 
		buff = line;
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
		cout<<"],\n";
	}
	cout<<"];\n";
	File[idx*3].getline(line,Maxn*Maxn-1); 
	buff = line;
	cout << endl <<"var notice = \"" << buff << "\";" << endl;
	File[idx*3].close();
}


void getOffline()
{
	cout<<"\nvar offline = [";
	File[idx*3+1].open(offlineFile.c_str(),ios::in);
	//cout<<"Start Analysis "<<offlineFile<<endl;
	File[idx*3+1]>>m>>enter;
	for(int i=1;i<=m;i++)
	{
		cout<<"[";
		string buff="";
		int pos=0,cnt=0;
		File[idx*3+1].getline(line,Maxn*Maxn-1); 
		buff = line;
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
		cout<<"],"<<endl;
	}
	cout<<"];"<<endl;
	File[idx*3+1].close();
}


void getData()
{
	cout<<"\nvar Arr_size = ["<<n<<","<<m<<"];"<<endl;
	cout<<"\nvar w = [";
	File[idx*3+2].open(dataFile.c_str(),ios::in);
	//cout<<"Start Analysis "<<dataFile<<endl;
	for(int i=0;i<n;i++)
	{
		for(int j=0;j<m;j++)
		{
			File[idx*3+2]>>w[i][j];
			cout<<"["<<i<<","<<j<<","<<w[i][j]<<"],";
		}
		cout<<endl;
	}
	cout<<"];"<<endl;
	File[idx*3+2].close();
}

void getPij()
{
	cout<<"\nvar p = [";
	for(int i=0;i<n;i++)
	{
		for(int j=0;j<m;j++)
		{
			int tmp = 0;
			for(int k=0;k<m;k++) if(w[i][k]==w[i][k]) tmp += w[i][k];
			if(tmp==0) p[i][j] = zero;
			else p[i][j] = (double)w[i][j] / (double)tmp;
			cout<<"["<<i<<","<<j<<","<<p[i][j]<<"],";
		}
		cout<<endl;
	}
	cout<<"];"<<endl;
}

void getLift()
{
	cout<<"\nvar lift = [";
	for(int i=0;i<n;i++)
	{
		for(int j=0;j<m;j++)
		{
			int tmpu = 0, tmpd = 0; // up&down
			for(int k=0;k<n;k++)
			{
				if(k!=i)
				tmpu += w[k][j];	
			} 
			for(int k=0;k<n;k++)
			{
				if(k!=i)
				for(int l=0;l<m;l++)
					tmpd += w[k][l];
			}
			if(tmpu==0||tmpd==0) lift[i][j] = zero;
			else lift[i][j] = (double)p[i][j] / ((double)tmpu/(double)tmpd);
			cout<<"["<<i<<","<<j<<","<<lift[i][j]<<"],";
		}
		cout<<endl;
	}
	cout<<"];"<<endl;
}

void getVij()
{
	cout<<"\nvar v = [";
	for(int i=0;i<n;i++)
	{
		for(int j=0;j<m;j++)
		{
			v[i][j] = p[i][j] * log(lift[i][j]);
			if(v[i][j]==v[i][j]) cout<<"["<<i<<","<<j<<","<<v[i][j]<<"],";
			else cout<<"["<<i<<","<<j<<","<<zero<<"],";
		}
		cout<<endl;
	}
	cout<<"];"<<endl;
}

void getXi()
{
	cout<<"\nvar xValue = [";
	for(int i=0;i<n;i++)
	{
		x[i]=zero;
		for(int j=0;j<m;j++)
		{
			double buf_data = p[i][j] * (log(p[i][j])) * (-1.0);
			if(buf_data==buf_data) x[i] += buf_data;
		}
		if(x[i]==x[i]) 	
			cout<<"["<<i<<","<<0<<","<<x[i]<<"],";
		else 
			cout<<"["<<i<<","<<0<<","<<zero<<"],";
		cout<<endl;
	}
	cout<<"];"<<endl;
}

void getYi()
{
	cout<<"\nvar yValue = [";
	for(int i=0;i<n;i++)
	{
		y[i]=zero;
		for(int j=0;j<m;j++)
		{
			double buf_data = p[i][j] * log(lift[i][j]);
			if(buf_data==buf_data) y[i] += buf_data;
		}
		if(y[i]==y[i]) 
			cout<<"["<<i<<","<<0<<","<<y[i]<<"],";
		else
			cout<<"["<<i<<","<<0<<","<<zero<<"],";
		cout<<endl;
	}
	cout<<"];"<<endl;
}

void getZi()
{
	cout<<"\nvar zValue = [";
	for(int i=0;i<n;i++)
	{
		int jtmp=0;
		z[i]=zero;
		while(!v[i][jtmp]==v[i][jtmp]) jtmp++;
		z[i]= v[i][jtmp];
		for(int j=0;j<m;j++)
			if(v[i][j]==v[i][j])
				z[i] = (z[i] - v[i][j] > zero ? z[i] : v[i][j]) ;
		if(z[i]==z[i]) 
			cout<<"["<<i<<","<<0<<","<<z[i]<<"],";
		else 
			cout<<"["<<i<<","<<0<<","<<zero<<"],";
		cout<<endl;
	}
	cout<<"];"<<endl;
}

void getArr()
{
	double max_x=-INF,min_y=INF,min_z=INF,
		   avg_x=zero,avg_y=zero,avg_z=zero;	   
	for(int ii=0;ii<n;ii++)
	{
		if(x[ii]==x[ii])
		{
			if(x[ii]-max_x>zero) max_x=x[ii];
			avg_x = avg_x + (double)x[ii];	
		}
		if(y[ii]==y[ii])
		{
			if(min_y-y[ii]>zero) min_y=y[ii];
			avg_y = avg_y + (double)y[ii];	
		}
		if(z[ii]==z[ii])
		{
			if(min_z-z[ii]>zero) min_z=z[ii];
			avg_z = avg_z + (double)z[ii];
		}
		// cout<<"/* Test "<<endl<<avg_x<<endl<<avg_y<<endl<<avg_z<<endl<<"*/";
	}
	cout<<"\nvar arr = [";
	avg_x = avg_x / n;
	avg_y = avg_y / n;
	avg_z = avg_z / n;
	cout<<max_x<<","<<min_y<<","<<min_z<<","
		<<avg_x<<","<<avg_y<<","<<avg_z<<"];"<<endl;
}

void update(double inp, int idx)	//0-W 1-P 2-Lift 3-V 4-Vx 5-Vy 6-Vz 
{
	//Update MIN
	range[idx*2] = range[idx*2] <= inp ? range[idx*2]:inp;
	//Update MAX
	range[idx*2+1] = range[idx*2+1] >= inp ? range[idx*2+1]:inp;
}

void getRange()
{
	const double LIMIT = 9999.9;
	for(int i=0;i<7;i++)
	{
		range[i*2] = LIMIT; 	// min
		range[i*2+1] = -LIMIT;	// max
	}
	for(int i=0;i<n;i++)
	{
		for(int j=0;j<m;j++) //0-W 1-P 2-Lift 3-V 4-Vx 5-Vy 6-Vz 
		{
			if(w[i][j]==w[i][j]) update((double)w[i][j],0);
			if(p[i][j]==p[i][j]) update((double)p[i][j],1);
			if(lift[i][j]==lift[i][j]) update((double)lift[i][j],2);
			if(v[i][j]==v[i][j]) update((double)v[i][j],3);
		}
		if(x[i]==x[i]) update((double)x[i],4);
		if(y[i]==y[i]) update((double)y[i],5);
		if(z[i]==z[i]) update((double)z[i],6);
	}
	cout<<endl<<"var range = ["<<endl;
	for(int i=0;i<7;i++)
		cout<<range[i*2]<<","<<range[i*2+1]<<",";
	cout<<"];"<<endl;
} 

int check_file(int i)
{
	itoa(i,index,10);
	dataFile = _dataFile + index;
	chk.open(dataFile.c_str(),ios::in);
	if(!chk) 
	{
		chk.close();
		cout<<dataFile<<" doesn't exist, end here."<<endl;
		return 1;
	}
	else
	{
		chk.close();
		cout<<dataFile<<" exists, start analysis."<<endl;
		onlineFile = _onlineFile + index;
		offlineFile = _offlineFile + index;
		outputFile = _outputFile + index + ".js";
		return 0;	
	}
}


void makeHTML()
{
	string dest = htmlFile + index + ".html";
	CopyFile("./Echarts.html",dest.c_str(),false);
} 

int main()
{   
	cout<< "======Solving Start======" <<endl;
	for(idx=1;;idx++)
	{
		if(check_file(idx)==1) break;
		Filo[idx].open(outputFile.c_str(),ios::out);
		streambuf *strmout_buf = cout.rdbuf(); 
		cout.rdbuf(Filo[idx].rdbuf());  
		
		getOnline();
		getOffline();
		getData();
		getPij();
		getLift();
		getVij();
		getXi();
		getYi();
		getZi(); 
		getArr();
		getRange();
		makeHTML();
		
		Filo[idx].close();
		cout.rdbuf(strmout_buf); 
		cout<<dataFile<<" analysis over."<<endl;
	}
	cout<< "=======Solving End=======" <<endl;
	return 0;
}
