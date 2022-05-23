// package cpp and aman;

#include <iostream>
using namespace std;
int data=10;
class aman{
    protected:
    int data;
    public: 
    aman():data(20){}
    ~aman(){}
};
class derived:aman{
    protected:
    int data;
    public:
    derived():data(30){}
    ~derived(){}
    void print(){ cout<< data<<" " <<aman::data <<" "<<::data<<endl;}
};
int main()
{ derived d;
d.print();
    return 0;
}