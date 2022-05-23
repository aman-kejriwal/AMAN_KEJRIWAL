#include <iostream>
using namespace std;
int main()
{
	int a = 10;
	char c = 'a';
	// pass at compile time, may fail at run time
char* q =&c;
	// int* p = static_cast<int*>(&c);
    cout<<*q<<endl;
    return 0;
}