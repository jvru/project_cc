#include <iostream>
#include <fstream>
#include <ctime>
using namespace std;

int main()
{
	ofstream ofile("PageRankDatasets.txt", ios::app);
	for (int i = 0; i < 1300000; i++)
	{
		ofile << i << " " << rand()%(1300001) << endl;
	}
	ofile.close();

	return 0;
}