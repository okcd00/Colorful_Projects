//以数值积分的方法计算 Pi 的值

#include "stdafx.h"
#include "omp.h"
#include <stdio.h>
#include <time.h>
long long num_steps = 1000000000;
double step;
int _tmain(int argc, _TCHAR* argv[])
{
	clock_t start, stop;
	double x, pi, sum=0.0;
	int i,j,k;
	step = 1./(double)num_steps;
	start = clock();
#pragma omp parallel for reduction(+:sum),private(x)
	for (i=0; i<num_steps; i++)
	{
		x = (i + .5)*step;
		sum = sum + 4.0/(1.+ x*x);
	}
	pi = sum*step;
	stop = clock();
	printf("The value of PI is %15.12f\n",pi);
	printf("The time to calculate PI was %f seconds\n",((double)(stop - start)/1000.0));
	return 0;
}
