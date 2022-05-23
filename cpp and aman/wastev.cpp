// #include <iostream>
// using namespace std;
// int main(){
// int a = 5;
//     cin>>a;
//     cout<<(a+1);
// }
// #include <algorithm>  
// #include <iostream>  
// #include <vector>  
  
// using namespace std;  
  
// int main() {  
//   vector<int> v = { 3,1,2,1,2 };  
  
//   replace(v.begin(), v.end(), 3, 10);  
  
//   for_each(v.begin(), v.end(),  
//     [](int x) { cout << x << ","; });  
      
//     return 0;  
// }  
#include <iostream>
using namespace std;
enum E { Monday, Tuesday=3, Wednesday, Thursday=8, Friday, Saturday, Sunday };
E operator+(const E &a, const E &b) {
unsigned int ea = a, eb = b;
cout<<Monday<<" "<< Wednesday<<" "<<Thursday<<endl;
unsigned int c = (ea + eb) % 7;
return (E)c;
}
int main() {
E a = Wednesday, b = Friday;
E day = a + b;
cout << day;
return 0;
}