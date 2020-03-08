package Assignment4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class ProblemE {
    public static void main(String[] args) throws Exception {
        ProblemE problem = new ProblemE();
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
            String states = tokenizer.nextToken();
            int[] tankStates = new int[n];
            for (int i = 0; i < n; i++) {
                if (i < n - 1) tankStates[i] = Integer.parseInt(states.substring(i, i + 1));
                else tankStates[i] = Integer.parseInt(states.substring(i));
            }
            Tanks tanks = new Tanks(n, tankStates);

            while (q > 0) {
                tokenizer = new StringTokenizer(reader.readLine());

                String query = tokenizer.nextToken();
                if (query.equals("S")) {
                    int x = Integer.parseInt(tokenizer.nextToken());
                    tanks.update(x - 1, 0);
                } else if (query.equals("R")) {
                    int x = Integer.parseInt(tokenizer.nextToken());
                    tanks.update(x - 1, 1);
                } else if (query.equals("Q")) {
                    int a = Integer.parseInt(tokenizer.nextToken());
                    int b = Integer.parseInt(tokenizer.nextToken());
                    printWriter.println(tanks.query(a - 1, b - 1));
                }

                q--;
            }
            T--;
        }

        printWriter.close();
        reader.close();
    }

    class Tanks {
        int n;
        int actualn;
        Vertex[] tree;

        Tanks(int n, int[] states) {
            this.actualn = n;
            this.n = (int) Math.pow(2, Math.ceil(Math.log(n) / Math.log(2)));
            this.tree = new Vertex[2 * this.n];
            build(states);
        }

        void build(int[] initialstates) {
            for (int i = 0; i < this.actualn; i++) tree[n + i] = new Vertex(initialstates[i]);
            for (int i = this.actualn; i < this.n; i++) tree[n + i] = new Vertex(0);
            for (int i = n - 1; i > 0; i--) tree[i] = combVertices(tree[2 * i], tree[2 * i + 1]);
        }

        Vertex combVertices(Vertex a, Vertex b) {
            Vertex n = new Vertex(0);

            if (a.state == 1 || b.state == 1) { // LEAF
                if (a.state == 1) {
                    if (b.state == 1) {
                        n.lMax = 2;
                        n.rMax = 2;
                        n.continuous = true;
                        return n;
                    } else { // b.state == 0
                        n.lMax = 1;
                        return n;
                    }
                } else { // a.state == 0 (b.state == 1)
                    n.rMax = 1;
                    return n;
                }
            } else {
                if (!a.continuous && !b.continuous) { // neither is continuous
                    int comb = a.rMax + b.lMax;
                    int res = Math.max(a.mMax, b.mMax);
                    n.mMax = Math.max(res, comb);

                    n.lMax = a.lMax;
                    n.rMax = b.rMax;
                } else if (a.continuous && b.continuous) { // both are continuous
                    n.continuous = true;
                    n.lMax = a.lMax + b.lMax;
                    n.rMax = a.rMax + b.rMax;
                    n.mMax = 0;
                } else if (a.continuous) { // a is continuous
                    n.lMax = a.lMax + b.lMax;
                    n.rMax = b.rMax;
                    n.mMax = b.mMax;
                } else { // b is continuous
                    n.rMax = b.rMax + a.rMax;
                    n.lMax = a.lMax;
                    n.mMax = a.mMax;
                }

            }

            return n;
        }

        class Vertex {
            int mMax, rMax, lMax, state;
            boolean continuous;

            Vertex(int state) {
                this.state = state;
                if (state == 1){
                    continuous = true;
                    this.lMax = this.rMax = 1;
                } else {
                    continuous = false;
                    this.lMax = this.rMax = 0;
                }
                this.mMax = 0;
            }
        }

        void update(int x, int val) {
            int v = n + x;
            tree[v] = new Vertex(val);

            for (int i = v / 2; i > 0; i /= 2) tree[i] = combVertices(tree[2 * i], tree[2 * i + 1]);
        }

        int query(int x, int y) {
            Vertex ans = queryhelp(x, y, 1, 0, this.n - 1);
            return Math.max(ans.mMax, Math.max(ans.lMax, ans.rMax));
        }

        Vertex queryhelp(int x, int y, int i, int l, int r) {
            if (x > r || y < l)
                return new Vertex(0);
            if (x <= l && r <= y)
                return tree[i];
            return combVertices(queryhelp(x, y, 2 * i, l, (l + r) / 2), queryhelp(x, y, 2 * i + 1, (l + r) / 2 + 1, r));
        }
    }
}


