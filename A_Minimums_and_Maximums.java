import java.util.*;

class A_Minimums_and_Maximums {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int t = s.nextInt();
        while (t-- > 0) {
            int l1 = s.nextInt();
            int r1 = s.nextInt();
            int l2 = s.nextInt();
            int r2 = s.nextInt();
            if (HaveAnyCommon(l1, r1, l2, r2)) {
                System.out.println(Math.max(l1, l2));
            } else {
                System.out.println(l1 + l2);
            }
        }
    }

    static boolean HaveAnyCommon(int l1, int r1, int l2, int r2) {
        if (l1 < l2) {
            if (r1 < l2)
                return false;
            else
                return true;
        } else if (l1 == l2)
            return true;
        else {
            if (l1 > r2)
                return false;

            else
                return true;
        }

    }
}
