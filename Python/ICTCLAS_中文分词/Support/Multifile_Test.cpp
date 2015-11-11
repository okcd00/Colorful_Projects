#include <iostream>
#include <fstream>
using namespace std;
int main()
{
	int i;
	char filename[30],line[1001];
	ifstream File[5];
	ofstream File2;  
	File2.open("5.txt",ios::out);
	for(i=1;i<5;i++)
	{
		sprintf(filename,"%d.txt",i);
		File[i].open(filename,ios::in);
		while(!File[i].eof())
		{
			File[i].getline(line,1000);
			File2<<line<<endl;
		}
		File[i].close();
	}
	File2.close();
	system("pause");
	return 0;
}
