
#include<iostream>
using namespace std;
template<class T1, class T2>
class templates{
    public:
        T1 data1;
        T2 data2;
    templates(T1 a=5,T2 b='v'){
            data1 = a;
            data2 = b;
        }
    void display(){
//std::cout<<this->data1<<" "<<this->data2;
      printf("%d %c",data1,data2);
    }
};
int main()
{
    // myClass<int, char> obj(1, 'c');
    templates<int,char> obj1;
    obj1.data1=1;
//    // myClass <char> obj2;
   obj1.data2='c';
    obj1.display();
    return 0;
}