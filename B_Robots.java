import java.util.*;

public class B_Robots {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int t = s.nextInt();
        while (t-- > 0) {
            int r = s.nextInt();
            int c = s.nextInt();
            String arr[] = new String[r];
            for (int i = 0; i < r; i++) {
                arr[i] = s.next();
            }
            if (CountRobo(arr, r, c))
                System.out.println("NO");
            else
                System.out.println("YES");
        }
    }

    static boolean CountRobo(String arr[], int r, int c) {
        int count = 0;
        int i = 0, j = 0;
        Search: {
            for (i = 0; i < r; i++) {
                for (j = 0; j < c; j++) {
                    if (arr[i].charAt(j) == 'R')
                        break Search;
                }
            }
        }
        // System.out.println("i= " + i + " " + "j= " + j);
        for (int k = 0; k < r; k++) {
            for (int l = 0; l < c; l++) {
                if (arr[k].charAt(l) == 'R' && l < j) {
                    // System.out.println("man");
                    return true;
                }
            }
        }
        return false;
    }
}