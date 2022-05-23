
/* package codechef; // don't place package name! */
import java.util.*;
import java.lang.*;
import java.time.temporal.IsoFields;
import java.io.*;
import java.util.HashSet;
import java.util.Set;

/* Name of the class has to be "Main" only if the class is public. */
class Make_them_Zero {
	// public static void main(String[] args) throws java.lang.Exception {
	// Scanner s = new Scanner(System.in);
	// int t = s.nextInt();
	// while (t-- > 0) {
	// int n = s.nextInt();
	// int arr[] = new int[n];

	// for (int i = 0; i < n; i++) {
	// arr[i] = s.nextInt();
	// }
	// Arrays.sort(arr);
	// int count = 0;
	// while (arr.length != 0) {
	// int i = 0;
	// if (arr[0] == 0) {
	// for (i = 0; i < arr.length; i++) {
	// if (arr[i] != 0)
	// break;
	// }
	// int arr2[] = Arrays.copyOfRange(arr, i, arr.length);
	// arr = arr2;

	// } else {
	// int num = findNearPow(arr[0]);
	// for (int j = 0; j < arr.length; j++) {
	// arr[j] = arr[j] - num;
	// }
	// count++;
	// }
	// }
	// System.out.println(count);
	// }
	// }
	public static void main(String[] args) throws java.lang.Exception {
		Scanner s = new Scanner(System.in);
		int t = s.nextInt();
		while (t-- > 0) {
			int n = s.nextInt();
			int arr[] = new int[n];
			HashSet<Integer> set = new HashSet<>();
			HashSet<Integer> set2 = new HashSet<>();
			int count = 0;
			for (int i = 0; i < n; i++) {
				set2.add(s.nextInt());
				if (isPowOf2(arr[i])) {
					set.add(arr[i]);
					count++;
				}
			}
			int arr2[] = new int[set.size()];
			set2.toArray(arr2);
			for (int i = 0; i < n; i++) {
				if (!isPowOf2(arr2[i])) {
					while (arr2[i] != 0) {
						int num = findNearPow(arr2[i]);
						if (!set.contains(num)) {
							count++;
							set.add(num);
						}
						arr2[i] = arr2[i] - num;
					}
				}
			}
			// while (arr.length != 0) {
			// int i = 0;
			// if (arr[0] == 0) {
			// for (i = 0; i < arr.length; i++) {
			// if (arr[i] != 0)
			// break;
			// }
			// int arr2[] = Arrays.copyOfRange(arr, i, arr.length);
			// arr = arr2;

			// } else {
			// int num = findNearPow(arr[0]);
			// for (int j = 0; j < arr.length; j++) {
			// arr[j] = arr[j] - num;
			// }
			// count++;
			// }
			// }
			System.out.println(count);
		}
	}

	static boolean isPowOf2(int num) {
		int a = findNearPow(num);
		if (a == num)
			return true;
		return false;
	}

	static int findNearPow(int num) {
		int sqrt = (int) (Math.log(num) / Math.log(2));
		// System.out.println(sqrt);
		int nearPow = (int) Math.pow(2, sqrt);
		return nearPow;
	}
}