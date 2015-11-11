//OpenMP���г��򡣷ֱ�Ϊ��
//��1���ĸ��̸߳���ִ��6�ε�����
//��2���ĸ��߳�Эͬ���6�ε�����


#include "stdafx.h"
#include <stdio.h>
#include "omp.h"

int _tmain(int argc, _TCHAR* argv[])
{
	printf("Hello from serial.\n");
	printf("Thread number = %d\n",omp_get_thread_num());//chuan xing
	#pragma omp parallel for	//bing xing
		for(int i=0;i<4;i++)
		{
			printf("Hello from parallel. Thread number=%d\n",omp_get_thread_num());	
		}
	printf("Hello from serial again.\n");
	return 0;
}
