package Assignment4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class ProblemB {
    public static void main(String[] args) throws Exception {
        ProblemB problem = new ProblemB();
        problem.problem();
    }

    private void problem() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        long T = Integer.parseInt(tokenizer.nextToken());
        PrintWriter printWriter = new PrintWriter(System.out);

        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            int n = Integer.parseInt(tokenizer.nextToken());
            int q = Integer.parseInt(tokenizer.nextToken());

            Baskets baskets = new Baskets(n);

            while (q > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                String type = tokenizer.nextToken();
                int x = Integer.parseInt(tokenizer.nextToken());
                int y = Integer.parseInt(tokenizer.nextToken());

                if (type.equals("D")) {
                    long d = Long.parseLong(tokenizer.nextToken());
                    baskets.update(x-1, y-1, -d);
                } else if (type.equals("H")) {
                    long h = Long.parseLong(tokenizer.nextToken());
                    baskets.update(x-1, y-1, h);
                } else if (type.equals("Q")) {
                    printWriter.println(baskets.query(x-1,y-1,1,0,baskets.n-1));
                }

                q--;
            }
            T--;
        }

        printWriter.close();
        reader.close();
    }

    class Baskets {
        int n; int actualn;
        Node[] tree;

        class Node {
            long val; long lazy;
            Node (long n) {
                this.val = n; this.lazy = 0;
            }
        }

        Baskets(int n) {
            this.actualn = n;
            this.n = (int)Math.pow(2,Math.ceil(Math.log(n)/Math.log(2)));
            this.tree = new Node[2 * this.n];
            build();
        }

        void build() {
            for (int i = 0; i < this.actualn; i++) tree[n + i] = new Node(0);
            for (int i = this.actualn; i < this.n; i++) tree[n + i] = new Node(Long.MAX_VALUE);
            for (int i = n - 1; i > 0; i--) tree[i] = new Node(Math.min(tree[2 * i].val, tree[2 * i + 1].val));
        }

        void update(int x, int y, long val) {
            updateRangeUtil(1,0, this.n-1, x, y, val);
        }

        void updateRangeUtil(int i, int iStart, int iEnd, int x, int y, long diff) {
            if (tree[i].lazy != 0) {
                tree[i].val += tree[i].lazy;
                if (iStart != iEnd) { // not a leaf
                    tree[i * 2].lazy += tree[i].lazy;
                    tree[i * 2 + 1].lazy += tree[i].lazy;
                }
                tree[i].lazy = 0;
            }

            if (iStart > iEnd || iStart > y || iEnd < x) return;

            if (iStart >= x && iEnd <= y) {
                tree[i].val += diff;

                if (iStart != iEnd) { // not a leaf
                    tree[i * 2].lazy += diff;
                    tree[i * 2 + 1].lazy += diff;
                }
                return;
            }

            int mid = (iStart + iEnd) / 2;
            updateRangeUtil(i * 2, iStart, mid, x, y, diff);
            updateRangeUtil(i * 2 + 1, mid + 1, iEnd, x, y, diff);

            tree[i].val = Math.min(tree[i * 2].val,tree[i * 2 + 1].val);
        }

        long query(int x, int y, int i, int l, int r) {
            if (tree[i].lazy != 0) {
                tree[i].val += tree[i].lazy;

                if (i < this.n) { // not a leaf
                    tree[i * 2].lazy += tree[i].lazy;
                    tree[i * 2 + 1].lazy += tree[i].lazy;
                }
                tree[i].lazy = 0;
            }

            if (x > r || y < l) return Long.MAX_VALUE; // because looking for minimum
            if (x <= l && r <= y) return tree[i].val;
            return Math.min(query(x, y, 2 * i, l, (l + r) / 2), query(x, y, 2 * i + 1, (l + r) / 2 + 1, r));
        }
    }

}


