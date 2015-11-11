// WinPi.cpp : 定义控制台应用程序的入口点。
// 多线程求Pi的值

#include "stdafx.h"
#include <Windows.h>
#include <stdio.h>
#include <time.h>

int i = 0;
double step, pi;
clock_t start, stop, allstart, allstop;
double x, sum = 0.0;
const int numThreads = 6;	//5-threads calc
static long num_steps = 150000000;
CRITICAL_SECTION g_cs;

DWORD WINAPI calc(LPVOID p)
{
	int cnt = 0;
	printf("Now a new Thread is calc-ing\n");
	start = clock();
	EnterCriticalSection(&g_cs);
	for (; cnt<num_steps / numThreads; cnt++, i++)
	{
		x = (i + 0.5)*step;
		sum += 4.0 / (1.0 + x*x);
	}
	LeaveCriticalSection(&g_cs);
	stop = clock();
	printf("the time of calc was %f s\n", ((double)(stop - start) / 1000.0));
	return 0;
}


int _tmain(int argc, _TCHAR* argv[])
{
	HANDLE hThread[numThreads];
	int tNum[50];
	step = 1.0 / (double)num_steps;
	allstart = clock();
	InitializeCriticalSection(&g_cs);
	for (i = 0; i < numThreads; i++)
	{
		tNum[i] = i;
		hThread[i] = CreateThread(NULL, 0, calc, (LPVOID)&tNum[i], 0, NULL);
	}
	WaitForMultipleObjects(numThreads, hThread, TRUE, INFINITE);

	allstop = clock();
	printf("Now step=%f ,sum=%f \n", step, sum);
	pi = step*sum;
	printf("Pi=%12.9f\n", pi);
	printf("the time of total was %f s\n", ((double)(allstop - allstart) / 1000.0));
	DeleteCriticalSection(&g_cs);
	system("pause");
	return 0;
