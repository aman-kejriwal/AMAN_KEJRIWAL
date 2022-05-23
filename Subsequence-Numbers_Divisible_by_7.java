/* package codechef; // don't place package name! */

import java.util.*;
import java.lang.*;
import java.io.*;

/* Name of the class has to be "Main" only if the class is public. */
class Subsequence-Numbers_Divisible_by_7
{

	public static void main(String[] args) throws java.lang.Exception {
		Scanner s = new Scanner(System.in);
		int t = s.nextInt();
		while (t-- > 0) {
			long n = s.nextLong();
			long arr[] = new long[n];
			String S = "";
			for (int i = 0; i < n; i++) {
				arr[i] = s.nextLong();
				S += arr[i];
			}

		}
	}
	// Java program to generate power set in
	// lexicographic order.

	// str : Stores input string
	// n : Length of str.
	// curr : Stores current permutation
	// index : Index in current permutation, curr
	static void printSubSeqRec(String str, int n, int index,
			String curr) {
		// base case
		if (index == n) {
			return;
		}
		if (curr != null && !curr.trim().isEmpty()) {
			System.out.println(curr);
		}
		for (int i = index + 1; i < n; i++) {
			curr += str.charAt(i);
			printSubSeqRec(str, n, i, curr);

			// backtracking
			curr = curr.substring(0, curr.length() - 1);
		}
	}

	// Generates power set in
	// lexicographic order.
	static void printSubSeq(String str) {
		int index = -1;
		String curr = "";

		printSubSeqRec(str, str.length(), index, curr);
	}

	// This code is contributed by PrinciRaj1992

}
