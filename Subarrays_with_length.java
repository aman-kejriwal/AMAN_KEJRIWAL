import java.util.*;

class Subarrays_with_length {
    public static void main(String[] args) throws java.lang.Exception {
        Scanner s = new Scanner(System.in);
        int t = s.nextInt();
        while (t-- > 0) {
            int n = s.nextInt();
            long arr[] = new long[n];
            for (int i = 0; i < n; i++) {
                arr[i] = s.nextLong();
            }
            int count = 0;
            HashMap<Long, HashSet<Long>> m = new HashMap<>();
            HashMap<Long, Integer> p = new HashMap<>();

            for (int i = 0; i < n; i++) {
                long ind = 0;
                if (p.containsKey(arr[i])) {
                    ind = p.get(arr[i]) + 1;
                    ind = Math.max(ind, i - (arr[i] - 1));
                }
                // m.put(arr[i],i);
                else {
                    ind = i - (arr[i] - 1);
                    if (ind < 0) {
                        ind = 0;
                    }
                }
                long lind = i + (arr[i] - 1);
                if (lind >= n) {
                    lind = n - 1;
                }
                long b = (lind - ind + 1) - (arr[i] - 1);
                if (b < 0)
                    b = 0;
                // System.out.println("ind = " + ind + "lind = " + lind + "b = " + b);
                count += b;
                // System.out.println("count = " + count);
                p.put(arr[i], i);
                // for (int j = ind; j <= i; j++) {
                // if ((n - j) - arr[i] < 0) {
                // break;
                // }

                // if (m.get(arr[i]) == null) {
                // m.put(arr[i], new HashSet<Integer>());
                // } else if (m.get(arr[i]).contains(j)) {
                // continue;
                // }
                // count++;
                // HashSet<Integer> v = m.get(arr[i]);
                // v.add(j);
                // m.put(arr[i], v);
                // }

            }
            System.out.println(count);

        }
    }
}