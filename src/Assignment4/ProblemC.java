package Assignment4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class ProblemC {
    public static void main(String[] args) throws Exception {
        ProblemC problem = new ProblemC();
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
            int m = Integer.parseInt(tokenizer.nextToken());
            int[] initial = new int[n];

            tokenizer = new StringTokenizer(reader.readLine());
            for (int i = 0;i < n; i++) initial[i]= Integer.parseInt(tokenizer.nextToken());

            Meh meh = new Meh(initial, n);

            while (n-1 > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                int a = Integer.parseInt(tokenizer.nextToken());
                int b = Integer.parseInt(tokenizer.nextToken());
                meh.tree[a-1].goesTo.add(b-1);
                meh.tree[b-1].goesTo.add(a-1);
                n--;
            }
            meh.setupTree(0,0);
            meh.doTanks();

            while (m > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                String does = tokenizer.nextToken();
                String A = tokenizer.nextToken();

                if (does.equals("1")) {
                    String R = tokenizer.nextToken();
                    meh.update(Integer.parseInt(R),Integer.parseInt(A)-1);
                } else if (does.equals("2")) {
                    printWriter.println(meh.query(Integer.parseInt(A)));
                }
            }

            T--;
        }

        printWriter.close();
        reader.close();
    }

    class Meh {
        Node[] tree;
        int n; int[] tour;
        int[] start; int[] end;

        Meh(int[]initial, int n) {
            this.n = n;
            tree = new Node[n];
            for(int i=0; i < n; i++) tree[i] = new Node(initial[i]);
            tour = new int[2 * n];
            start = new int[n];
            end = new int[n];
        }

        int setupTree(int loc, int tloc) {
            Node t = tree[loc];
            tour[tloc] = loc; tloc++;
            start[loc] = tloc - 1;
            for(int i : t.goesTo) {
                Node node = tree[i]; node.goesTo.remove(loc);
                tloc = setupTree(i,tloc);
            }

            tour[tloc] = loc;
            end[loc] = tloc;
            tloc++;
            return tloc;
        }

        Tanks t;
        void doTanks() {
            t = new Tanks(2*n, tour);
        }

        void update(int val,int x) {
            t.updateRangeUtil(1,0, t.n-1, start[x] + t.n, end[x] + t.n, val);
        }

        long query(int qs) {
            return t.query(0, t.n-1,start[qs], end[qs], 0).valsBelow.size();
        }


        class Node {
            HashSet<Integer> goesTo;
            int val;
            Node(int val) {
                this.val = val;
                goesTo = new HashSet<>();
            }
        }

        class Tanks {
            int n; int actualn;
            tNode[] tree2, lazy;

            Tanks(int n, int[]t ) {
                this.actualn = n;
                this.n = (int)Math.pow(2,Math.ceil(Math.log(n)/Math.log(2)));
                this.lazy = new tNode[2 * this.n];
                this.tree2 = new tNode[2 *this.n];
                build(t);
            }

            class tNode {
                HashSet<Integer> valsBelow; int val;
                tNode(HashSet<Integer> set, int val) {
                    valsBelow =set;
                    this.val =val;
                    set.add(val);
                    set.remove(0);
                }
            }

            void build(int[] t) {
                for (int i = 0; i < this.actualn; i++) tree2[n + i] = new tNode(new HashSet<>(),tree[t[i]].val);
                for (int i = this.actualn; i < this.n; i++) tree2[n + i] = new tNode(new HashSet<>(),0);
                for (int i = n - 1; i > 0; i--) {
                    HashSet<Integer> set = new HashSet<>();
                    set.addAll(tree2[2* i].valsBelow);
                    set.addAll(tree2[2*i+1].valsBelow);
                    tree2[i] = new tNode(set, 0);
                }
                for (int i  = 0;i < 2*n; i++) lazy[i] = new tNode(new HashSet<>(),0);
            }

            void updateRangeUtil(int si, int ss, int se, int us,
                                 int ue, int diff)
            {
                if (lazy[si].valsBelow.size() != 0) {
                    tree2[si].valsBelow.addAll(lazy[si].valsBelow);

                    if (ss != se) {
                        lazy[si*2].valsBelow.addAll(lazy[si].valsBelow);
                        lazy[si*2 + 1].valsBelow.addAll(lazy[si].valsBelow);
                    }

                    lazy[si].valsBelow = new HashSet<>();
                }

                if (ss>se || ss>ue || se<us) return ;

                if (ss>=us && se<=ue) {
                    tree2[si].valsBelow.add(diff);

                    if (ss != se) {
                        lazy[si*2].valsBelow.add(diff);
                        lazy[si*2 + 1].valsBelow.add(diff);
                    }
                    return;
                }

                int mid = (ss+se)/2;
                updateRangeUtil(si*2, ss, mid, us, ue, diff);
                updateRangeUtil(si*2+1, mid+1, se, us, ue, diff);

                tree2[si].valsBelow.addAll(tree2[si*2].valsBelow);
                tree2[si].valsBelow.addAll(tree2[si*2+1].valsBelow);
            }

            tNode query(int ss, int se, int qs, int qe, int si) { //O(logn) only looking for a point butneed to pull lazy down along way
                if (lazy[si].valsBelow.size() != 0) {
                    tree2[si].valsBelow.addAll(lazy[si].valsBelow);

                    if (ss != se) {
                        lazy[si*2].valsBelow.addAll(lazy[si].valsBelow);
                        lazy[si*2 + 1].valsBelow.addAll(lazy[si].valsBelow);
                    }

                    lazy[si].valsBelow = new HashSet<>();
                }

                // Out of range
                if (ss>se || ss>qe || se<qs)
                    return new tNode(new HashSet<>(),0);

                if (ss>=qs && se<=qe)
                    return tree2[si];

                int mid = (ss + se)/2;
                HashSet<Integer> a = query(ss, mid, qs, qe, 2*si).valsBelow;
                HashSet<Integer> b = query(mid+1, se, qs, qe, 2*si+1).valsBelow;

                HashSet<Integer>newone = new HashSet<>();
                newone.addAll(a);newone.addAll(b);
                return new tNode(newone, 0);
            }
        }
    }



}


