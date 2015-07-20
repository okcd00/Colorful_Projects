#include <cmath> 
#include <cctype>
#include <cstdio>
#include <string>
#include <cstdlib>
#include <cstring>
#include <iostream>
#include <algorithm>
using namespace std;

#define Max(a,b) ((a)>(b)?(a):(b))
#define Min(a,b) ((a)<(b)?(a):(b))

bool cmp(const int a, const int b)
{
	return a > b;
}

int main()
{
	freopen("Effective-freq.txt","r",stdin);
	int n=0,cnt=0;
	while(scanf("%d",&n)!=EOF)cnt++;
	fclose(stdin);
	cout<<cnt<<endl;
	return 0;
}
