package Assignment4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class ProblemA {
    public static void main(String[] args) throws Exception {
        ProblemA problem = new ProblemA();
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

                if (type.equals("D")) {
                    long d = Long.parseLong(tokenizer.nextToken());
                    baskets.update(x-1, -d);
                } else if (type.equals("H")) {
                    long h = Long.parseLong(tokenizer.nextToken());
                    baskets.update(x-1, h);
                } else if (type.equals("Q")) {
                    int y = Integer.parseInt(tokenizer.nextToken());
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
        long[] tree;

        Baskets(int n) {
            this.actualn = n;
            this.n = (int)Math.pow(2,Math.ceil(Math.log(n)/Math.log(2)));
            this.tree = new long[2 * this.n];
            build();
        }

        void build() {
            for (int i = 0; i < this.actualn; i++) tree[n + i] = 10000;
            for (int i = this.actualn; i < this.n; i++) tree[n + i] = Long.MAX_VALUE;
            for (int i = n - 1; i > 0; i--) tree[i] = Math.min(tree[2 * i], tree[2 * i + 1]);
        }

        void update(int x, long val) {
            int v = n + x;
            tree[v] = tree[v] + val;

            for (int i = v / 2; i > 0; i /= 2) tree[i] = Math.min(tree[2 * i],tree[2 * i + 1]);
        }

        long query(int x, int y, int i, int l, int r) {
            if (x > r || y < l) return Long.MAX_VALUE;
            if (x <= l && r <= y) return tree[i];
            return Math.min(query(x, y, 2 * i, l, (l + r) / 2), query(x, y, 2 * i + 1, (l + r) / 2 + 1, r));
        }
    }

}
