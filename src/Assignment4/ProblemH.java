package Assignment4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class ProblemH {
    public static void main(String[] args) throws Exception {
        ProblemH problem = new ProblemH();
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

            tokenizer = new StringTokenizer(reader.readLine());
            long[] tanks = new long[n];
            for (int i = 0; i < n; i++) tanks[i] = Long.parseLong(tokenizer.nextToken());
            Tanks t = new Tanks(n, tanks);

            while (q > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                String type = tokenizer.nextToken();

                if (type.equals("0")) {
                    int x = Integer.parseInt(tokenizer.nextToken());
                    long a = Long.parseLong(tokenizer.nextToken());
                    t.update(x-1, a);
                } else if (type.equals("1")) {
                    int l = Integer.parseInt(tokenizer.nextToken());
                    int r = Integer.parseInt(tokenizer.nextToken());
                    printWriter.println(t.query(l-1,r-1));
                }

                q--;
            }
            T--;
        }

        printWriter.close();
        reader.close();
    }

    class Tanks {
        int n; int actualn;
        Vertex[] tree; long[] initialstates;

        Tanks(int n, long[] states) {
            this.actualn = n; this.initialstates = states;
            this.n = (int)Math.pow(2,Math.ceil(Math.log(n)/Math.log(2)));
            this.tree = new Vertex[2 * this.n];
            build();
        }

        void build() {
            for (int i = 0; i < this.actualn; i++) tree[n + i] = new Vertex(this.initialstates[i]);
            for (int i = this.actualn; i < this.n; i++) tree[n + i] = new Vertex(0);
            for (int i = n - 1; i > 0; i--) tree[i] = combVertices(tree[2 * i],tree[2 * i + 1]);
        }

        Vertex combVertices(Vertex a, Vertex b) {
            Vertex n = new Vertex(0);

            if (a.state != 0 || b.state != 0) { // LEAF
                long comb = a.state + b.state;
                n.lMax = Math.max(a.state, comb);
                n.rMax = Math.max(b.state, comb);
                n.sum = a.state + b.state;
                return n;
            } else {
                n.lMax = Math.max(a.lMax, a.sum + b.lMax);
                n.rMax = Math.max(b.rMax, b.sum + a.rMax);
                n.sum = a.sum + b.sum;
                n.mMax = Math.max(a.rMax + b.lMax, Math.max(a.rMax, Math.max(b.lMax, Math.max(a.mMax, b.mMax))));
            }

            return n;
        }

        class Vertex {
            long mMax, rMax, lMax, state, sum;
            Vertex(long state) {
                this.state = state;
                this.lMax = this.rMax = this.mMax = this.sum = 0;
            }
        }

        void update(int x, long val) {
            int v = n + x;
            tree[v] = new Vertex(val);

            for (int i = v / 2; i > 0; i /= 2) tree[i] = combVertices(tree[2 * i],tree[2 * i + 1]);
        }


        long query(int x, int y) {
            Vertex ans = queryhelp(x,y,1,0,this.n-1);
            return Math.max(ans.mMax, Math.max(ans.lMax, ans.rMax));
        }

        Vertex queryhelp(int x, int y, int i, int l, int r) {
            if (x > r || y < l)
                return new Vertex(0);
            if (x <= l && r <= y)
                return tree[i];
            return combVertices(queryhelp(x, y, 2 * i, l, (l + r) / 2),queryhelp(x, y, 2 * i + 1, (l + r) / 2 + 1, r));
        }
    }

}


