//本实验对一个串行程序并行化，以信号量的方式解决数据冲突。
//代码分析了指定文件中字符串的个数并分别对含有偶数个字符
//的字符串以及含有奇数个字符的字符串的个数。

// SemaphoreS.cpp : 定义控制台应用程序的入口点。
#pragma   warning(disable:4996)
#include "stdafx.h"
#include<iostream>
#include<windows.h>
FILE *fd;
int TotalEvenWords = 0, TotalOddWords = 0, TotalWords = 0;
HANDLE hSem1, hSem2;
const int NUMTHREADS = 4;

int GetNextLine(FILE*f, char*Line){
	if (fgets(Line, 132, f) == NULL) 
		if (feof(f)) return EOF; 
		else return 1;
	return 1;
}
int GetWordAndLetterCount(char * Line){
	int Word_Count = 0,OddWords=0,EvenWords=0, LetterCount = 0;
	for (int i = 0; i<132; i++){
		if ((Line[i] != ' ') && (Line[i] != 0) && (Line[i] != '\n'))       LetterCount++;
		else{
			if (LetterCount != 0){
				if (LetterCount % 2){
					//TotalOddWords++;
					OddWords++;
					Word_Count++;
					LetterCount = 0;
				}
				else{
					EvenWords++;
					//TotalEvenWords++;
					Word_Count++;
					LetterCount = 0;
				}
			}
			if (Line[i] == 0)	break;
		}
	}
	return (Word_Count*10000+OddWords*100+EvenWords);
}
DWORD WINAPI CountWords(LPVOID arg){
	BOOL bDone = FALSE;
	char inLine[132];
	int temp = 0;
	while (!bDone){
		WaitForSingleObject(hSem1, INFINITE);
		bDone = (GetNextLine(fd, inLine) == EOF);
		ReleaseSemaphore(hSem1, 1, NULL);
		if (!bDone){
			temp = GetWordAndLetterCount(inLine);
			WaitForSingleObject(hSem2, INFINITE);
			TotalWords += temp / 10000;
			TotalOddWords += (temp % 10000) / 100;
			TotalEvenWords += temp % 100;
			ReleaseSemaphore(hSem2, 1, NULL);
		}
	}
	return 0;
}
int main()
{
	HANDLE hThread[NUMTHREADS];
	hSem1 = CreateSemaphore(NULL, 1, 1, NULL);
	hSem2 = CreateSemaphore(NULL, 1, 1, NULL);
	fd = fopen("inFiles.txt", "r");
	for (int i = 0; i < NUMTHREADS; i++)
		hThread[i] = CreateThread(NULL, 0, CountWords, NULL, 0, NULL);
	WaitForMultipleObjects(NUMTHREADS, hThread, TRUE, INFINITE);
	fclose(fd);
	printf("Total Words=%8d\n\n", TotalWords);
	printf("Total Even Words=%7d\nTotal Odd Words=%7d\n", TotalEvenWords, TotalOddWords);
	system("pause");
	return 0;
}
