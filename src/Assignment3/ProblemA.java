package Assignment3;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class ProblemA {
    public static void main(String[] args) throws Exception {
        ProblemA problem = new ProblemA();
        problem.problem_a();
    }

    private void problem_a() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        long T = Integer.parseInt(tokenizer.nextToken());
        PrintWriter printWriter = new PrintWriter(System.out);

        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            int n = Integer.parseInt(tokenizer.nextToken());
            int q = Integer.parseInt(tokenizer.nextToken());

            Orchard o = new Orchard(n);
            while ((n-1) > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                int a = Integer.parseInt(tokenizer.nextToken());
                int b = Integer.parseInt(tokenizer.nextToken());
                o.addPath(a, b);
                n--;
            }

            o.sortOrchard(0,1);

            while (q > 0) {
                int type;
                int a = 0, b = 0, k = 0;
                tokenizer = new StringTokenizer(reader.readLine());
                type = Integer.parseInt(tokenizer.nextToken());

                if (type == 0) a = Integer.parseInt(tokenizer.nextToken());
                b = Integer.parseInt(tokenizer.nextToken());
                if (type == 2) k = Integer.parseInt(tokenizer.nextToken());

                if (type == 0) o.addLeaf(a,b);
                else if (type == 1) o.removeLeaf(b); //type 1 ignore ?
                else if (type == 2){
                    int dest = o.walk(b, k);
                    if (dest == 0) printWriter.println("HOME");
                    else printWriter.println(dest);
                }
                q--;
            }
            T--;
        }
        printWriter.close(); reader.close();
    }

    private class Orchard {
        int n;
        HashMap<Integer, Tree> trees;
        HashSet<Integer> leaves;

        private Orchard(int trees) {
            n = trees;
            this.trees = new HashMap<>();
            this.leaves = new HashSet<>();
        }

        void addPath(int a, int b){
            if (!this.trees.containsKey(a)) this.trees.put(a, new Tree());
            if (!this.trees.containsKey(b)) this.trees.put(b, new Tree());

            this.trees.get(a).goesTo.add(b);
            this.trees.get(b).goesTo.add(a);
        }

        void removeLeaf(int b) {
            Tree bs = this.trees.get(b);
            this.trees.get(bs.path.get(0)).goesTo.remove(b);
            this.trees.remove(b);
        }

        void addLeaf(int a, int b) {
            Tree t = trees.get(a);
            t.goesTo.add(b);

            Tree cur = new Tree();

            int distance = t.distance + 1;
            int multipleof2 = (int) Math.floor(Math.log(distance)/Math.log(2));

            cur.path.add(a);
            Tree temp = t;
            for (int i = 1; i <= multipleof2; i++) {
                cur.path.add(temp.path.get(i-1));
                temp = this.trees.get(temp.path.get(i-1));
            }
            cur.distance = distance;

            this.trees.put(b, cur);
        }

        int walk(int b, int k) {
            if (k <= 0 || b <= 0) return b; //if not walking or if home already return current location
            else {
                Tree bs = this.trees.get(b);
                if (bs.distance <= k) return 0; //if k>= its distance from home it'll get home

                int multipleof2 = (int) Math.floor(Math.log(k)/Math.log(2));
                return walk(bs.path.get(multipleof2), k - (int)Math.pow(2,multipleof2)); //binary jumping
            }
        }

        void sortOrchard(int tree, int distance) {
            int multipleof2 = (int) Math.floor(Math.log(distance)/Math.log(2));
            Tree cur;
            Tree t = this.trees.get(tree);
            for (int connected : t.goesTo) {
                cur = this.trees.get(connected);
                cur.goesTo.remove(tree);
                cur.distance = distance;

                cur.path.add(tree);
                Tree temp = t;
                for (int i = 1; i <= multipleof2; i++) {
                    cur.path.add(temp.path.get(i-1));
                    temp = this.trees.get(temp.path.get(i-1));
                }

                sortOrchard(connected, distance + 1);
            }
        }
    }

    private class Tree {
        ArrayList<Integer> path;
        HashSet<Integer> goesTo;
        int distance;

        private Tree() {
            this.goesTo = new HashSet<>();
            this.path = new ArrayList<>();
            distance = 0;
        }
    }

}
