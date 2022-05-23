#include<iostream>
using namespace std;
class A{
public:
int a;
};
class B : public A{
public:
int b;
};
class C : public B{
public:
int c;
C(int a_, int b_, int c_) {
a = a_; //LINE-1
b = b_;
c = c_;
}
};
int main(){
C cObj(10, 20, 30);
cout << cObj.a << " "; //LINE-2
cout << cObj.b << " "; //LINE-3
cout << cObj.c;
return 0;
}