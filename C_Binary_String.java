import java.util.*;
public class C_Binary_String {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int t = s.nextInt();
        while (t-- > 0) {
            String S = s.next();
            int count0Left = countZero(S);
            int count1Removed = 0;
            while (!S.isEmpty() && count0Left > count1Removed) {
                if (count0Left == 0)
                    break;
                if (!S.isEmpty() && S.charAt(0) == '0') {
                    S = S.substring(1);
                    count0Left--;
                } else if (!S.isEmpty() && S.charAt(S.length() - 1) == '0') {
                    S = S.substring(0, S.length() - 1);
                    count0Left--;
                } else if (!S.isEmpty() && S.charAt(S.length() - 1) == '1' && S.charAt(0) == '1') {
                    if (count0(S, 0, S.length() / 2) < count0(S, (S.length() / 2) + 1, S.length() - 1)) {
                        S = S.substring(0, S.length() - 1);
                    } else {
                        S = S.substring(1);
                    }
                    count1Removed++;
                }
            }
            System.out.println(count0Left);
        }
    }

    static int count0(String S, int i, int j) {
        int c1 = 0;
        for (int k = i; k <= j; k++) {
            if (S.charAt(k) == '0')
                c1++;
        }
        return c1;
    }

    static int countZero(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '0')
                count++;
        }
        return count;
    }
}