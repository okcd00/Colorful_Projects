#include <sys/types.h>
#include <dirent.h>
#include <stdio.h>
#include<iostream>
#include<vector>
using namespace std;
int main(int argc, char *argv[])
{
	DIR *dp;
	struct dirent *dirp;
	vector<std::string> filename;
	if( (dp=opendir("F:\\directory_name") )==NULL )
	perror("open dir error");
	
	while( (dirp=readdir(dp) )!=NULL )
	filename.push_back(dirp->d_name);
	for(int i=0;i<filename.size();i++)
	cout<<filename[i]<<endl;
	closedir(dp);
	return 0;
}
