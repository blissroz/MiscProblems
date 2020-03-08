package Assignment3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

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
            final int n = Integer.parseInt(tokenizer.nextToken());
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
                int k = Integer.parseInt(tokenizer.nextToken());
                queries.add(new Query(l-1,r-1,k,n,i));
                q--; i++;
            }

            order(queries); // order queries based on starting block of left index, secondary by r value so less moving
            Solver solver = new Solver(queries, n, baskets);
            order2(solver.qs); // order queries by order they were asked in

            for (Query query : solver.qs) {
                printWriter.println(query.result);
            }
            T--;
        }
        printWriter.close(); reader.close();
    }

    class Query {
        int l, r, k, i, result, lsq;

        Query(int l, int r, int k, int n, int i) {
            this.i = i;
            this.l = l;
            this.r = r;
            this.k = k;
            this.lsq = l/(int)Math.floor(Math.sqrt(n));
            if (k > n) this.result = 0; //don't need since this will never occur with assignment input
            else this.result = -1;
        }
    }

    class Solver {
        LinkedList<Query> qs;
        int n; int[] counts; int[] k_counts; int[] baskets;
        int end, start;

        Solver(LinkedList<Query> qs, int n, int[] baskets) {
            this.qs = qs;
            this.n = n;
            this.counts = new int[(int)Math.pow(10,7)];
            this.k_counts = new int[n+1];
            this.baskets = baskets;
            this.end = qs.get(0).r;
            this.start = qs.get(0).l;

            solve();
        }

        void solve() {
            // for query 0
            for (int i = start; i <= end; i++) {
                if (k_counts[counts[baskets[i]]] > 0) k_counts[counts[baskets[i]]]--;
                counts[baskets[i]]++;
                k_counts[counts[baskets[i]]]++;
            }
            if (qs.get(0).result == -1) qs.get(0).result = k_counts[qs.get(0).k];

            Query first = qs.pop();

            for (Query q : qs) {
                int l = q.l;
                int r = q.r;

                while (start > l) { // move start pointer left (add items)
                    start--;
                    if (k_counts[counts[baskets[start]]] > 0) k_counts[counts[baskets[start]]]--;
                    counts[baskets[start]]++;
                    k_counts[counts[baskets[start]]]++;
                }
                while (end < r) { // move end point right (add items)
                    end++;
                    if (k_counts[counts[baskets[end]]] > 0) k_counts[counts[baskets[end]]]--;
                    counts[baskets[end]]++;
                    k_counts[counts[baskets[end]]]++;
                }
                while (start < l) { //need to move start pointer right (remove items)
                    if (k_counts[counts[baskets[start]]] > 0) k_counts[counts[baskets[start]]]--;
                    counts[baskets[start]]--;
                    k_counts[counts[baskets[start]]]++;
                    start++;
                }
                while (end > r) { // move end pointer left (remove items)
                    if (k_counts[counts[baskets[end]]] > 0) k_counts[counts[baskets[end]]]--;
                    counts[baskets[end]]--;
                    k_counts[counts[baskets[end]]]++;
                    end--;
                }

                if (q.result == -1) q.result = k_counts[q.k];
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

