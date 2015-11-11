#include <cmath> 
#include <time.h>
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
	time_t timer;
	struct tm* tblock;
	timer=time(NULL);
	tblock=localtime(&timer);
	printf("Localtimeis:%s",asctime(tblock));
return 0;
}

