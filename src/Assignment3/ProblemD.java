package Assignment3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.util.*;

public class ProblemD {
    public static void main(String[] args) throws Exception {
        ProblemD problem = new ProblemD();
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
            int k = Integer.parseInt(tokenizer.nextToken());

            BigInteger totalT = new BigInteger("0");
            Orchard orchard = new Orchard(n);

            while ((n - 1) > 0) {
                int a, b;
                long t;

                tokenizer = new StringTokenizer(reader.readLine());
                a = Integer.parseInt(tokenizer.nextToken());
                b = Integer.parseInt(tokenizer.nextToken());
                t = Long.parseLong(tokenizer.nextToken());

                orchard.addPath(a, b, t);
                totalT = totalT.add(BigInteger.valueOf(t));
                n--;
            }

            printWriter.println(totalT.subtract(BigInteger.valueOf(orchard.toRemove(k))).multiply(BigInteger.valueOf(2)));
            T--;
        }
        printWriter.close();
        reader.close();
    }

    private class Orchard {
        ArrayList<Tree> trees;
        HashSet<Tree> leaves;

        private Orchard(int n) {
            this.trees = new ArrayList<>();
            this.leaves = new HashSet<>();

            for (int i = 0; i < n; i++) trees.add(new Tree(i)); //o(n)
        }

        void addPath(int a, int b, long t) {
            trees.get(a).addPath(b, t);
            trees.get(b).addPath(a, t);
        }

        long toRemove(int k) {
            long removed = 0;

            while (k > 0) {
                List<Tree> leaves = new ArrayList<>(this.leaves);
                order(leaves);

                if (leaves.size() > 0) {
                    Tree toRemove = leaves.get(0);
                    removed += toRemove.weight;

                    trees.get(toRemove.parent).removePath(toRemove.number);
                    this.leaves.remove(toRemove);
                }
                k--;
            }

            return removed;
        }

        private class Tree {
            HashMap<Integer, Long> goesTo; //node and edge weight
            long weight;
            int number;
            int parent;
            Integer connections;

            private Tree(int n) {
                goesTo = new HashMap<>();
                connections = 0;
                weight = 0;
                number = n;
            }

            void addPath(int b, long t) { //o(1)
                goesTo.put(b, t);
                connections++;

                if (connections <= 1 && this.number != 0) {
                    leaves.add(this);
                    weight = t;
                    parent = b;
                } else {
                    if (leaves.contains(this)) leaves.remove(this);
                    weight = 0;
                }
            }

            void removePath(int b) { //o(1)
                goesTo.remove(b);
                connections--;

                if (connections <= 1 && this.number != 0) {
                    leaves.add(this);

                    Iterator i = goesTo.entrySet().iterator();
                    if (i.hasNext()) { //only happens once
                        Map.Entry e = (Map.Entry) i.next();
                        weight = (long) e.getValue();
                        parent = (int) e.getKey();
                    }
                }
            }
        }

        private void order(List<Tree> leaves) {
            Collections.sort(leaves, new Comparator() {

                public int compare(Object o1, Object o2) {
                    long l1 = ((Tree) o1).weight;
                    long l2 = ((Tree) o2).weight;
                    int sComp = Long.compare(l2, l1);

                    return sComp;
                }
            });
        }
    }


}