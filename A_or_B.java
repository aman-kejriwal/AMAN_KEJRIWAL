
/* package codechef; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class A_or_B {
    public static void main(String[] args) throws java.lang.Exception {
        Scanner s = new Scanner(System.in);
        int t = s.nextInt();
        while (t-- > 0) {
            int n = s.nextInt();
            Vector<Integer> v = new Vector<>();
            for (int i = 0; i < n; i++)
                v.add(s.nextInt());
            boolean haveNeighbour = true;
            int count = 0;
            for (int i = 0; i < n;) {
                if (v.size() == 1) {
                    break;
                }
                // if(i>0&&v.get(i)==v.get(i-1)){
                // v.remove(i);count++;}
                if (v.size() > i + 1 && v.get(i) == v.get(i + 1)) {
                    v.remove(i);
                    count++;
                } else
                    i++;
            }
            System.out.println(n - count);
        }
    }
}