package Assignment4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class ProblemF {
    public static void main(String[] args) throws Exception {
        ProblemF problem = new ProblemF();
        problem.problem();
    }

    private void problem() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        long T = Integer.parseInt(tokenizer.nextToken());
        PrintWriter printWriter = new PrintWriter(System.out);

        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            int q = Integer.parseInt(tokenizer.nextToken());
            Tanks tanks = new Tanks(100000);

            while (q > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                String type = tokenizer.nextToken();

                if (type.equals("F")) {
                    int x = Integer.parseInt(tokenizer.nextToken());
                    int w = Integer.parseInt(tokenizer.nextToken());
                    int h = Integer.parseInt(tokenizer.nextToken());
                    int start = x - 1;
                    int end = start + w - 1;
                    tanks.update(start, start + h - 1, 1);
                    tanks.update(end - (h - 2), Math.min(end + 1, tanks.tree.length - 1), -1);
                } else if (type.equals("Q")) {
                    int x = Integer.parseInt(tokenizer.nextToken());
                    printWriter.println(tanks.query(0,tanks.n - 1, 0, x-1, 1));
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
        long[] tree, lazy;

        Tanks(int n) {
            this.actualn = n;
            this.n = (int)Math.pow(2,Math.ceil(Math.log(n)/Math.log(2)));
            this.lazy = new long[2 * this.n];
            this.tree = new long[2 *this.n];
            build();
        }

        void build() { //O(n)
            for (int i = 0; i < tree.length; i++) tree[i] = 0; // TREE OF ZEROS
        }

        void update(int x, int y, int val) {
            updateRangeUtil(1,0, this.n-1, x, y, val);
        }

        void updateRangeUtil(int si, int ss, int se, int us,
                             int ue, int diff)
        {
            if (lazy[si] != 0) {
                tree[si] += (se-ss+1)*lazy[si];

                if (ss != se) {
                    lazy[si*2]   += lazy[si];
                    lazy[si*2 + 1]   += lazy[si];
                }

                lazy[si] = 0;
            }

            if (ss>se || ss>ue || se<us) return ;

            if (ss>=us && se<=ue) {
                tree[si] += (se-ss+1)*diff;

                if (ss != se) {
                    lazy[si*2]   += diff;
                    lazy[si*2 + 1]   += diff;
                }
                return;
            }

            int mid = (ss+se)/2;
            updateRangeUtil(si*2, ss, mid, us, ue, diff);
            updateRangeUtil(si*2+1, mid+1, se, us, ue, diff);

            tree[si] = tree[si*2] + tree[si*2+1];
        }

        long query(int ss, int se, int qs, int qe, int si) { //O(logn) only looking for a point butneed to pull lazy down along way
            if (lazy[si] != 0) {
                tree[si] += (se-ss+1)*lazy[si];

                if (ss != se) {
                    lazy[si*2] += lazy[si];
                    lazy[si*2+1] += lazy[si];
                }
                lazy[si] = 0;
            }

            // Out of range
            if (ss>se || ss>qe || se<qs)
                return 0;

            if (ss>=qs && se<=qe)
                return tree[si];

            int mid = (ss + se)/2;
            return query(ss, mid, qs, qe, 2*si) +
                    query(mid+1, se, qs, qe, 2*si+1);
        }
    }
}


