// 从一单精度浮点数串中找出乘积最大的连续的 4 个浮点数。
// 例如在下列浮点数串[1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 5.0, 
// 3.0, 3.0, 2.0, 9.0, 1.0, 2.0]找到[4.0，5.0，6.0，5.0].
// 输入文件格式：
// 含有浮点数的文件, 浮点数数量大概在若干千万。
// 输出结果:
// 打印该连续4 个单元的首单元序号（以零为起始），以及该连续4 个单元的乘积。

#include "stdafx.h" 
#include <stdio.h> 
#include <stdlib.h> 
#include <conio.h> 
#include <windows.h> 
#include <sys/stat.h> 
#include "omp.h"

#define NUM_THREADS 2 
HANDLE *threadHandles; 
int num,index=0;  
float farray[100086]={0.0}, product=0.0; 
CRITICAL_SECTION gCS;  

DWORD WINAPI comp (LPVOID pArg) 
{  
   float product1,productmax; 
   int i,part,indexfirst; 
   part =  *((int*)pArg); 
   indexfirst=part;  
   for ( i=part;i<num-3;i=i+NUM_THREADS) {  
         product1=farray[i]*farray[i+1]*farray[i+2]*farray[i+3]; 
         if(i==part)productmax=product1; 
		 else{  
           if(product1>productmax) 
              {  
                   productmax=product1; 
                   indexfirst=i; 
              }                         
         }                                                               
      }           
	EnterCriticalSection(&gCS); 
    if(product<productmax) 
	{  
		product=productmax; 
        index=indexfirst; 
	}  
	LeaveCriticalSection(&gCS); 
	return 0; 
}  
int  main(int argc, char *argv[]){ 
    struct stat buf;  
    threadHandles = new HANDLE[NUM_THREADS]; 
    int i;  
    int arraynum[NUM_THREADS];  

/*读取文件内容*/
	if(stat(argv[1],&buf))exit(0);
    num = buf.st_size/sizeof(float);

	freopen(argv[1],"rb",stdin);
	for(i=0;i<num;i++) scanf("%f",&farray[i]);
	fclose(stdin);

/******************************************/ 

InitializeCriticalSection(&gCS); 
#pragma omp parallel for
     for(i=0;i<NUM_THREADS;i++)
	 {
		 arraynum[i]=i;  
		 printf("Now Thread No.%d is calculating...\n",omp_get_thread_num());
		 threadHandles[i]=CreateThread(NULL, 0, comp, &arraynum[i], 0, NULL); 
     }  
    WaitForMultipleObjects(NUM_THREADS,threadHandles,TRUE ,INFINITE); 
    DeleteCriticalSection(&gCS); 

/********************************/  
//printf("num=%d\n",num);
//for(i=0;i<num;i++)printf("%f\n",farray[i]);
 printf("乘积=% f 首数的序号=%d\n", product, index); 
 for(int j=index;j<index+4;j++)  
 { printf("farray[%d]=%f,\n",j, farray[j]);} 
 free(farray);
/********************************/ 
return 0; 
}  
