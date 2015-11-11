// ThreadEvent.cpp : �������̨Ӧ�ó������ڵ㡣
// ��ʵ���������ض�������������ln(1 + x), -1 < x <= 1��ֵ��
// �̱߳�����ʱ�ǹ����״̬����Щ�̱߳�һ��"master"�̻߳��ѡ�
// �߳̽��ֱ�ִ���Լ�������"master"�߳̽��ȴ����е��߳�ִ
// ����Ϻ󣬻���ÿһ���̵߳�ִ�н����

#include "stdafx.h"
#include<conio.h>
#include<windows.h>
#include<math.h>
#include<time.h>
#include<iostream>

#define NUMTHREADS 4
#define SERIES_MEMBER_COUNT 100000
HANDLE *threadHandles, masterThreadHandle,*eventHandles;
CRITICAL_SECTION countCS;
double *sums;
double x = 1.0, res = 0.0;
int threadCount = 0;

double getMember(int n, double x)
{
	double numerator = 1;
	for (int i = 0; i<n; i++)	numerator = numerator * x;
	if (n % 2 == 0) return(-numerator / n);
	return numerator / n;
}

DWORD WINAPI threadProc(LPVOID par)
{
	int threadIndex = *((int *)par);
	sums[threadIndex] = 0;
	for (int i = threadIndex; i<SERIES_MEMBER_COUNT; i += NUMTHREADS)
		sums[threadIndex] += getMember(i + 1, x);
	SetEvent(eventHandles[threadIndex]);
	//EnterCriticalSection(&countCS);
	//threadCount++;
	//LeaveCriticalSection(&countCS);
	delete par;
	return 0;
}

DWORD WINAPI masterthreadProc(LPVOID par)
{
	for (int i = 0; i<NUMTHREADS; i++)	ResumeThread(threadHandles[i]);
	//while (threadCount != NUMTHREADS){}
	WaitForMultipleObjects(NUMTHREADS, eventHandles, TRUE, INFINITE);
	res = 0;
	for (int i = 0; i<NUMTHREADS; i++)	res += sums[i];
	return 0;
}

int main(int argc, CHAR *argv[])
{
	clock_t start, stop;
	threadHandles = new HANDLE[NUMTHREADS + 1];
	//InitializeCriticalSection(&countCS);
	eventHandles = new HANDLE[NUMTHREADS + 1];
	sums = new double[NUMTHREADS];
	start = clock();
	for (int i = 0; i<NUMTHREADS; i++)
	{
		int * threadIdPtr = new int;
		*threadIdPtr = i;           
		threadHandles[i] = CreateThread(NULL, 0, threadProc, threadIdPtr, CREATE_SUSPENDED, NULL);
		eventHandles[i] = CreateEvent(NULL, TRUE, FALSE, NULL);
	}

	threadHandles[NUMTHREADS] = CreateThread(NULL, 0, masterthreadProc, NULL, 0, NULL);
	printf("Count of ln(1+x) Mercator's series members is %d\n", SERIES_MEMBER_COUNT);
	printf("Argument value of x is %f\n", (double)x);
	WaitForMultipleObjects(NUMTHREADS + 1, threadHandles, TRUE, INFINITE);
	stop = clock();
	for (int i = 0; i<NUMTHREADS + 1; i++)	CloseHandle(threadHandles[i]);
	delete threadHandles;
	delete eventHandles;
	//DeleteCriticalSection(&countCS);
	delete sums;

	printf("Result is %10.8f\n", res);
	printf("By function call ln(1+%f)=%10.8f\n", x, log(1 + x));
	printf("The time of calculation was %f seconds\n", ((double)(stop - start) / 1000.0));
	printf("Press any key ...");
	system("pause");
	return 0;
}
