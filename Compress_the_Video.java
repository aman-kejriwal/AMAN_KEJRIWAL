
/* package codechef; // don't place package name! */
import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class Compress_the_Video {
	public static void main(String[] args) throws java.lang.Exception {
		Scanner s = new Scanner(System.in);
		int t = s.nextInt();
		while (t-- > 0) {
			int n = s.nextlong();
			Vector<Long> v = new Vector<>();
			for (i = 0; i < n; i++)
				v.add(s.nextLong());
			boolean haveNeighbour = true;
			int aman = 1;
			int count = 0;
			search: {
				while (haveNeighbour) {
					haveNeighbour = false;

					for (int i = 0; i < v.size();) {
						if (v.size() == 1) {
							break search;
						}
						if ((v.size() > i + 1 && v.get(i) == v.get(i + 1)) || (i != 0 && v.get(i) == v.get(i - 1)))
							haveNeighbour = true;
						// if(i!=0&&v.get(i)==v.get(i-1)){
						// v.remove(i);count++;haveNeighbour=true;}
						if (v.size() > i + 1 && v.get(i) == v.get(i + 1)) {
							v.remove(i);
							count++;
						} else
							i++;
					}
				}
			}
			System.out.println(n - count);
		}
	}
}