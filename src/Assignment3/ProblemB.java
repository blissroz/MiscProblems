package Assignment3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

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
            Orchard orchard = new Orchard(n);

            while ((n-1) > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                int a = Integer.parseInt(tokenizer.nextToken());
                int b = Integer.parseInt(tokenizer.nextToken());
                orchard.addPath(a, b);
                n--;
            }

            orchard.sortOrchard(0,1);
            orchard.addLeaves();

            tokenizer = new StringTokenizer(reader.readLine());
            int q = Integer.parseInt(tokenizer.nextToken());

            while (q > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                int x = Integer.parseInt(tokenizer.nextToken());
                printWriter.println(orchard.query2(x));
                q--;
            }

            T--;
        }
        printWriter.close(); reader.close();
    }

    private class Orchard {
        ArrayList<Tree> trees;
        HashSet<Integer> leafset;

        private Orchard(int n) {
            this.trees = new ArrayList<>();
            for (int i = 0; i < n; i++) {
                trees.add(new Tree(i));
            }
            this.leafset = new HashSet<>();
        }

        void addPath(int a, int b){
            this.trees.get(a).goesTo.add(b);
            this.trees.get(b).goesTo.add(a);
        }

        void sortOrchard(int tree, int d) { //O(n)
            for (int adj : this.trees.get(tree).goesTo) {
                Tree adjT = this.trees.get(adj);
                adjT.goesTo.remove(tree);
                adjT.parent = tree;

                if (adjT.goesTo.size() == 0) {
                    leafset.add(adj);
                }

                sortOrchard(adj, d + 1);
            }
        }

        void addLeaves() { //O(nlogn)
            for (int i : this.leafset) {
                Tree p = this.trees.get(i);
                int d = 0;
                int from = i;
                while (p.number != 0) {
                    if (d > p.first) {
                        if (p.firstleaf != from) {
                            p.sec = p.first;
                            p.secleaf = p.firstleaf;
                        }
                        p.first = d; p.firstleaf = from;
                    } else if ((d > p.sec) && (p.firstleaf != from)) { p.sec = d; p.secleaf = from;
                    }

                    from = p.number;
                    p = this.trees.get(p.parent);
                    d++;
                }
                if (d > p.first) {
                    if (p.firstleaf != from) {
                        p.sec = p.first;
                        p.secleaf = p.firstleaf;
                    }
                    p.first = d; p.firstleaf = from;
                } else if ((d > p.sec) && (p.firstleaf != from)) { p.sec = d; p.secleaf = from;
                }
            }
            addPDistance(0);
        }

        void addPDistance(int s) {  //O(n)
            Tree start = trees.get(s); //start at root and go downwards

            for (int i : start.goesTo) {
                Tree cur = trees.get(i);
                if (start.firstleaf != i) cur.parentdistance = start.first + 1;
                else cur.parentdistance = start.sec + 1;
                if (start.parentdistance + 1 > cur.parentdistance) cur.parentdistance = start.parentdistance + 1;
                addPDistance(i);
            }
        }

        int query2(int t) {  //O(1)
            Tree tree = this.trees.get(t);
            if (tree.parentdistance > tree.sec) return tree.parentdistance + tree.first;
            else return tree.first + tree.sec;
        }
    }

    private class Tree {
        int number;
        int parent;
        int parentdistance;
        HashSet<Integer> goesTo;
        int first, firstleaf, sec, secleaf;

        private Tree(int n) {
            goesTo = new HashSet<>();
            number = n;
            first = firstleaf = sec = secleaf = parentdistance = parent =0;
        }
    }
}
