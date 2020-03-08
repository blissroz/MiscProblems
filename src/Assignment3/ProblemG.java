package Assignment3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class ProblemG {
    public static void main(String[] args) throws Exception {
        ProblemG problem = new ProblemG();
        problem.problem();
    }

    private void problem() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        long T = Integer.parseInt(tokenizer.nextToken());
        PrintWriter printWriter = new PrintWriter(System.out);

        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            final int n = Integer.parseInt(tokenizer.nextToken());
            final int k = Integer.parseInt(tokenizer.nextToken());
            int[] baskets = new int[n];

            tokenizer = new StringTokenizer(reader.readLine());
            for (int i = 0; i < n; i++) {
                baskets[i] = Integer.parseInt(tokenizer.nextToken());
            }

            tokenizer = new StringTokenizer(reader.readLine());
            int q = Integer.parseInt(tokenizer.nextToken());
            LinkedList<Query> queries = new LinkedList<>();
            int i = 0;
            while (q > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                int l = Integer.parseInt(tokenizer.nextToken());
                int r = Integer.parseInt(tokenizer.nextToken());
                queries.add(new Query(l-1,r-1,n,i));
                q--; i++;
            }

            order(queries); // order queries based on starting block of left index, secondary by r value so less moving
            Solver solver = new Solver(queries, n, baskets, k);
            order2(solver.qs); // order queries by order they were asked in

            for (Query query : solver.qs) {
                printWriter.println(query.result);
            }
            T--;
        }
        printWriter.close(); reader.close();
    }

    class Query {
        int l, r, i, result, lsq;

        Query(int l, int r, int n, int i) {
            this.i = i;
            this.l = l;
            this.r = r;
            this.lsq = l/(int)Math.floor(Math.sqrt(n));
            this.result = 0;
        }
    }

    class Solver {
        LinkedList<Query> qs;
        int n; int[] counts; int[] baskets; int k;
        int end, start;

        Solver(LinkedList<Query> qs, int n, int[] baskets, int k) {
            this.qs = qs;
            this.n = n;
            this.counts = new int[(int)Math.pow(10,7)];
            this.baskets = baskets;
            this.end = qs.get(0).r;
            this.start = qs.get(0).l;
            this.k = k;

            solve();
        }

        void solve() {
            int kval = 0;
            // for query 0
            for (int i = start; i <= end; i++) {
                if (k > baskets[i]) kval += counts[k - baskets[i]];
                counts[baskets[i]]++;
            }
            qs.get(0).result = kval;

            Query first = qs.pop();

            for (Query q : qs) {
                int l = q.l;
                int r = q.r;

                while (start > l) { // move start pointer left (add items)
                    start--;
                    if (k > baskets[start]) kval += counts[k - baskets[start]];
                    counts[baskets[start]]++;
                }
                while (end < r) { // move end point right (add items)
                    end++;
                    if (k > baskets[end]) kval += counts[k - baskets[end]];
                    counts[baskets[end]]++;
                }
                while (end > r) { // move end pointer left (remove items)
                    counts[baskets[end]]--;
                    if (k > baskets[end]) kval -= counts[k - baskets[end]];
                    end--;
                }
                while (start < l) { //need to move start pointer right (remove items)
                    counts[baskets[start]]--;
                    if (k > baskets[start]) kval -= counts[k - baskets[start]];
                    start++;
                }

                q.result = kval;
            }

            qs.add(first);
        }
    }

    // first by lblock, then by l, then by r
    private void order(List<Query> leaves) { //qlogq
        Collections.sort(leaves, new Comparator() {

            public int compare(Object o1, Object o2) {
                int l1 = ((Query) o1).lsq;
                int l2 = ((Query) o2).lsq;
                int sComp = Integer.compare(l1, l2);

                if (sComp != 0) {
                    return sComp;
                } else {
                    int r1 = ((Query) o1).r;
                    int r2 = ((Query) o2).r;
                    return Integer.compare(r1,r2);
                }
            }
        });
    }

    private void order2(List<Query> leaves) { //qlogq
        Collections.sort(leaves, new Comparator() {

            public int compare(Object o1, Object o2) {
                int l1 = ((Query) o1).i;
                int l2 = ((Query) o2).i;
                return Integer.compare(l1, l2);
            }
        });
    }
}
