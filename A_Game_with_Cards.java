
import java.util.*;

// import java.util.Scanner;
class A_Game_with_Cards {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);
        int t = s.nextInt();
        while (t-- > 0) {
            int n = s.nextInt();
            // int alice[]=new int [n];
            HashSet<Integer> set1 = new HashSet<>();

            for (int i = 0; i < n; i++) {
                set1.add(s.nextInt());
            }
            int n2 = s.nextInt();
            HashSet<Integer> set2 = new HashSet<>();
            // int bob[]=new int[n2];
            for (int i = 0; i < n2; i++) {
                set2.add(s.nextInt());
            }
            Vector<Integer> v1 = new Vector<>();
            for (int x : set1)
                v1.add(x);
            Vector<Integer> v2 = new Vector<>();
            for (int x : set2)
                v2.add(x);
            Collections.sort(v1);
            Collections.sort(v2);
            int alice = 0;
            int bob = 0;
            int k = 0;
            while (!v1.isEmpty() && !v2.isEmpty()) {
                if (k % 2 == 0) {
                    int aliceturn = v1.remove(0);
                    if (v2.isEmpty() || v2.get(v2.size() - 1) <= aliceturn) {
                        System.out.println("Alice");
                        break;
                    } else {
                        int i = 0;
                        for (i = 0; i < v2.size(); i++) {
                            if (v2.get(i) > aliceturn) {
                                break;
                            }
                        }

                        for (int j = 0; j <= i; j++) {
                            v2.remove(0);
                            if (v2.isEmpty()) {
                                System.out.println("Alice");
                                break;
                            }
                        }

                    }
                    if (v1.isEmpty()) {
                        System.out.println("Bob");
                        break;
                    }
                } else {
                    int bobturn = v2.remove(0);
                    if (v1.isEmpty() || v1.get(v1.size() - 1) <= bobturn) {
                        System.out.println("Bob");
                        break;
                    } else {
                        int i;
                        for (i = 0; i < v1.size(); i++) {
                            if (v1.get(i) > bobturn) {
                                break;
                            }
                        }

                        for (int j = 0; j <= i; j++) {
                            v1.remove(0);
                            if (v1.isEmpty()) {
                                System.out.println("Bob");
                                break;
                            }
                        }
                    }
                    if (v2.isEmpty()) {
                        System.out.println("Alice");
                        break;
                    }
                }
                k++;
            }
            v1.clear();
            v2.clear();
            for (int x : set1)
                v1.add(x);
            for (int x : set2)
                v2.add(x);
            k = 0;
            Collections.sort(v1);
            Collections.sort(v2);
            while (!v1.isEmpty() && !v2.isEmpty()) {
                if (k % 2 == 0) {
                    int bobturn = v2.remove(0);
                    if (v1.isEmpty() || v1.get(v1.size() - 1) <= bobturn) {
                        System.out.println("Bob");
                        break;
                    } else {
                        int i;
                        for (i = 0; i < v1.size(); i++) {
                            if (v1.get(i) > bobturn) {
                                break;
                            }
                        }
                        if (!v1.isEmpty())
                            for (int j = 0; j <= i; j++) {
                                v1.remove(0);
                                if (v1.isEmpty()) {
                                    System.out.println("Bob");
                                    break;
                                }
                            }

                    }
                    if (v2.isEmpty()) {
                        System.out.println("Alice");
                        break;
                    }
                } else {
                    int aliceturn = v1.remove(0);
                    if (v2.isEmpty() || v2.get(v2.size() - 1) <= aliceturn) {
                        System.out.println("Alice");
                        break;
                    } else {
                        int i;
                        for (i = 0; i < v2.size(); i++) {
                            if (v2.get(i) > aliceturn) {
                                break;
                            }
                        }
                        if (!v2.isEmpty())
                            for (int j = 0; j <= i; j++) {
                                v2.remove(0);
                                if (v2.isEmpty()) {
                                    System.out.println("Alice");
                                    break;
                                }
                            }
                    }
                    if (v1.isEmpty()) {
                        System.out.println("Bob");
                        break;
                    }
                }
                k++;
            }
        }
    }
}