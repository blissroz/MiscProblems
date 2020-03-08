package Assignment4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

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

            tokenizer = new StringTokenizer(reader.readLine());
            String s = tokenizer.nextToken();
            char[] chars = s.toCharArray();
            Baskets baskets = new Baskets(chars);

            while (n > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                int k = Integer.parseInt(tokenizer.nextToken());
                String c = tokenizer.nextToken();
                printWriter.println(baskets.update(k-1,c));
                n--;
            }

            T--;
        }

        printWriter.close();
        reader.close();
    }

    class Baskets {
        int n; int actualn;
        Node[] tree;
        Node[] lazy;

        Baskets(char[] c) {
            this.actualn = c.length;
            this.n = (int)Math.pow(2,Math.ceil(Math.log(c.length)/Math.log(2)));
            this.tree = new Node[2 * this.n];
            this.lazy = new Node[2 * this.n];
            build(c);
        }

        class Node {
            String val; int open; int closed; //# hanging open or hanging closed brakcets
            Node(String val, int open, int closed) {
                this.val = val; this.open = open; this.closed = closed;
            }
        }

        void build(char[] c) {
            for (int i = 0; i < this.actualn; i++) {
                if (c[i] == '(') {
                    tree[n + i] = new Node(String.valueOf(c[i]), 1, 0);
                } else if (c[i] == ')'){
                    tree[n + i] = new Node(String.valueOf(c[i]), 0, 1);
                } else {
                    tree[n + i] = new Node(String.valueOf(c[i]), 0,0);
                }
            }
            for (int i = this.actualn; i < this.n; i++) tree[n + i] = new Node("nada", 0,0);
            for (int i = n - 1; i > 0; i--) {
                int newO, newC;
                newO = Math.max(0, tree[2 * i].open - tree[2 * i + 1].closed) + tree[2 * i + 1].open;
                newC = Math.max(0, tree[2 * i + 1].closed - tree[2 * i].open) + tree[2 * i].closed;
                tree[i] = new Node("nada", newO, newC);
            }
        }

        String update(int x, String val) {
            int v = n + x;

            if (val.equals("(")) {
                tree[v] = new Node(val, 1, 0);
            } else if (val.equals(")")){
                tree[v] = new Node(val, 0, 1);
            } else {
                tree[v] = new Node(val, 0,0);
            }

            for (int i = v / 2; i > 0; i /= 2) {
                int newO, newC;
                newO = Math.max(0, tree[2 * i].open - tree[2 * i + 1].closed) + tree[2 * i + 1].open;
                newC = Math.max(0, tree[2 * i + 1].closed - tree[2 * i].open) + tree[2 * i].closed;
                tree[i] = new Node("nada", newO, newC);
            }

            Node root = tree[1];
            if (root.open == 0 && root.closed == 0) return "Yes";
            else return "No";
        }
    }

}


