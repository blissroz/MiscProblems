package Assignment6;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class ProblemD {
    public static void main(String[] args) throws Exception {


        ProblemD problem = new ProblemD();
        problem.deleteNode(problem.list());
        //problem.problem();


    }

    private String ipAddress(final String input) {
        return input.replace(".", "[.]");
    }

    private ListNode list() {
        ListNode a = new ListNode(1);
        ListNode b = new ListNode(2);
        ListNode c = new ListNode(3);
        ListNode d = new ListNode(4);
        a.next = b;
        b.next = c;
        c.next = d;
        return b;
    }
    public void tree() {
        TreeNode a = new TreeNode(1);
        TreeNode b = new TreeNode(2);
        TreeNode c = new TreeNode(3);
        TreeNode d = new TreeNode(4);
        TreeNode e = new TreeNode(5);

        a.left = b;
        a.right = c;
        c.right = d;
        c.left = e;
        int ans = maxDepth(a);
        boolean ys;
    }
    public int maxDepth(TreeNode root) {
        return getMax(root, 1);
    }

    public class ListNode {
        int val;
      ListNode next;
      ListNode(int x) { val = x; }
    }
    private void deleteNode(ListNode node) {
        System.out.println(node.val + " " + node.next.val);
        node = node.next;
        System.out.println(node.val + " " + node.next.val);
    }
    private int getMax(TreeNode root, int depth) {
        if (root.left != null && root.right != null) {
            depth++;
            return Math.max(getMax(root.left, depth), getMax(root.right, depth));
        } else if (root.left != null) {
            return getMax(root.left, ++depth);
        } else if (root.right != null) {
            return getMax(root.right, ++depth);
        }
        return depth;
    }

    public class TreeNode {
        TreeNode left;
        TreeNode right;
        int val;
        TreeNode(int val) { this.val = val; }
    }
    public void reverseString(char[] s) {
        String st = new String(s);
        String rev = new StringBuilder(st).reverse().toString();
        s = rev.toCharArray();

        int l = s.length - 1;
        for (int i = 0; i < l; i++) {
            char temp = s[i];
            s[i] = s[l - i];
            s[l - i] = temp;
        }
    }
    private void problem() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        long T = Integer.parseInt(tokenizer.nextToken());
        PrintWriter printWriter = new PrintWriter(System.out);

        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            int n = Integer.parseInt(tokenizer.nextToken());
            int m = Integer.parseInt(tokenizer.nextToken());
            MaxBipartite gr = new MaxBipartite(n);

            Balloon[] b = new Balloon[m];
            int i = 0;
            while (i < m) {
                tokenizer = new StringTokenizer(reader.readLine());
                int r = Integer.parseInt(tokenizer.nextToken());
                int c = Integer.parseInt(tokenizer.nextToken());
                b[i] = new Balloon(r,c);
                i++;
            }

            boolean[][] g = new boolean[n][n];
            for (int j = 0; j < m; j++) {
                g[b[j].x - 1][b[j].y - 1] = true;
            }

            printWriter.println(gr.maxBPM(g));
            T--;
        }
        printWriter.close(); reader.close();
    }

    class Balloon {
        int x, y;
        Balloon(int x, int y) {
            this.x = x; this.y = y;
        }
    }

    class MaxBipartite {
        int M;

        MaxBipartite (int M) {
            this.M = M;
        }

        boolean bpm(boolean bpGraph[][], int u, boolean seen[], int matchR[]) {
            for (int v = 0; v < M; v++) {
                if (bpGraph[u][v] && !seen[v]) {
                    seen[v] = true;
                    if (matchR[v] < 0 || bpm(bpGraph, matchR[v], seen, matchR)) {
                        matchR[v] = u;
                        return true;
                    }
                }
            }
            return false;
        }

        int maxBPM(boolean bpGraph[][]) {
            int matchR[] = new int[M];

            for(int i=0; i<M; ++i) matchR[i] = -1;

            int result = 0;
            for (int u = 0; u < M; u++) {
                boolean seen[] = new boolean[M] ;
                for(int i=0; i<M; ++i) seen[i] = false;

                if (bpm(bpGraph, u, seen, matchR)) result++;
            }
            return result;
        }
    }
}
