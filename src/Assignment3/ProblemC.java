package Assignment3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
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
            Orchard orchard = new Orchard(n);

            int i = 1;
            while (i < n) {
                tokenizer = new StringTokenizer(reader.readLine());
                orchard.addPath(i,
                        Integer.parseInt(tokenizer.nextToken()),
                        Long.parseLong(tokenizer.nextToken()));
                i++;
            }

            orchard.getDepth(0,1);
            orchard.sqn = (int) Math.floor(Math.sqrt(orchard.depth));
            orchard.doLayer(0,0,0,1);

            tokenizer = new StringTokenizer(reader.readLine());
            int q = Integer.parseInt(tokenizer.nextToken());
            while (q > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                printWriter.print(orchard.getDistance
                        (Integer.parseInt(tokenizer.nextToken()),
                         Integer.parseInt(tokenizer.nextToken())));
                if (q > 1) printWriter.print(" ");
                q--;
            }
             printWriter.println();
            T--;
        }
        printWriter.close(); reader.close();
    }

    private class Orchard {
        int n;
        int sqn;
        Tree[] trees;
        int depth;

        private Orchard(int trees) {
            this.n = trees;
            this.trees = new Tree[n];
            for (int i = 0; i < trees; i++) this.trees[i] = new Tree(i);
            this.depth = 0;
        }

        void addPath(int a, int b, long d){
            this.trees[a].goesTo.put(b,d);
            this.trees[b].goesTo.put(a,d);
        }

        void getDepth(int tree, int d) {
            Iterator it =  this.trees[tree].goesTo.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                int adj = (int) e.getKey();
                Tree adjT = this.trees[adj];
                adjT.goesTo.remove(tree); //so we dont go in circles

                if (d > this.depth) depth = d;
                getDepth(adj,d + 1);
            }
        }

        void doLayer(int sqparent, int tree, int i, int nfromroot) {
            Iterator it =  this.trees[tree].goesTo.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry e = (Map.Entry) it.next();
                int adj = (int) e.getKey(); long distance = (long) e.getValue();
                Tree adjT = this.trees[adj];
                Tree t = this.trees[tree];

                adjT.nfromroot = nfromroot;
                adjT.sqParent = sqparent; adjT.parent = tree;
                if (sqparent != tree) adjT.sqParentDistance = t.sqParentDistance.add(BigInteger.valueOf(distance));
                else adjT.sqParentDistance = BigInteger.valueOf(distance);

                if (i < sqn) doLayer(sqparent, adj, i + 1, nfromroot + 1);
                else doLayer(adj, adj, 1, nfromroot + 1);
            }
        }

        BigInteger getDistance(int x, int y) {
            Tree tx = this.trees[x]; Tree ty = this.trees[y];
            BigInteger distance = BigInteger.valueOf(0);

            while (tx.sqParent != ty.sqParent) {
                if (tx.nfromroot > ty.nfromroot) { // x is farther
                    distance = distance.add(tx.sqParentDistance);
                    tx = this.trees[tx.sqParent];
                } else if (ty.nfromroot > tx.nfromroot) { // y is farther
                    distance = distance.add(ty.sqParentDistance);
                    ty = this.trees[ty.sqParent];
                } else { // they both need to be raised
                    distance = distance.add(tx.sqParentDistance);
                    tx = this.trees[tx.sqParent];
                    distance = distance.add(ty.sqParentDistance);
                    ty = this.trees[ty.sqParent];
                }
            }

            while (tx.parent != ty.parent) {
                int t;
                if (tx.nfromroot > ty.nfromroot) {
                    t = tx.num;
                    tx = this.trees[tx.parent];
                    distance = distance.add(BigInteger.valueOf(tx.goesTo.get(t)));
                } else if (ty.nfromroot > tx.nfromroot) {
                    t = ty.num;
                    ty = this.trees[ty.parent];
                    distance = distance.add(BigInteger.valueOf(ty.goesTo.get(t)));
                } else {
                    t = tx.num;
                    tx = this.trees[tx.parent];
                    distance = distance.add(BigInteger.valueOf(tx.goesTo.get(t)));
                    t = ty.num;
                    ty = this.trees[ty.parent];
                    distance = distance.add(BigInteger.valueOf(ty.goesTo.get(t)));
                }
            }

            if (ty.num != tx.num) {
                if (ty.num != 0) distance = distance.add(BigInteger.valueOf(trees[ty.parent].goesTo.get(ty.num)));
                if (tx.num != 0) distance = distance.add(BigInteger.valueOf(trees[tx.parent].goesTo.get(tx.num)));
            }

            return distance;
        }
    }

    private class Tree {
        BigInteger sqParentDistance;
        HashMap<Integer,Long> goesTo;
        int parent, sqParent, nfromroot, num;

        private Tree(int n) {
            goesTo = new HashMap<>();
            sqParentDistance = BigInteger.valueOf(0);
            parent = sqParent = nfromroot = 0;
            num = n;
        }
    }
}
