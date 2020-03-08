package Assignment1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class ProblemG {
    public static void main(String[] args) throws Exception {
        ProblemG problem = new ProblemG();
        problem.problem_g();
    }

    /* test */
    private void problem_g() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(System.out);

        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        long T = Integer.parseInt(tokenizer.nextToken());
        int n, x, y, p, q;
        int m;

        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            n = Integer.parseInt(tokenizer.nextToken());
            m = Integer.parseInt(tokenizer.nextToken());
            Graph graph = new Graph(n);

            while (m > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                x = Integer.parseInt(tokenizer.nextToken());
                y = Integer.parseInt(tokenizer.nextToken());

                graph.addEdge(x,y);
                m--;
            }

            tokenizer = new StringTokenizer(reader.readLine());
            p = Integer.parseInt(tokenizer.nextToken());
            q = Integer.parseInt(tokenizer.nextToken());

            if (graph.g_connected(p,q))
                writer.println("yes");
            else {
                if (graph.g_connected(q,p))
                    writer.println("no");
                else writer.println("unknown");
            }

            T--;
        }

        reader.close(); writer.close();
    }
    // set up for BFS - looked at https://www.geeksforgeeks.org/breadth-first-traversal-for-a-graph/ while making
    private class Graph {
        LinkedList[] adjacencyList;
        int nodes;

        Graph(int n) {
            nodes = n;
            adjacencyList = new LinkedList[n];
            for (int i = 0; i < n; i ++) adjacencyList[i] = new LinkedList<Integer>();
        }


        void addEdge(int a, int b) {
            adjacencyList[a-1].add(b);
        }

        boolean g_connected(int a, int b) {
            boolean visited[] = new boolean[nodes];
            LinkedList<Integer> q = new LinkedList<>();

            visited[a-1] = true;
            q.add(a);
            int current;

            while (q.size() > 0) {
                current = q.poll();
                if (current == b) return true;

                LinkedList<Integer> current2= adjacencyList[current -1];
                for (int n : current2) {
                    if (n == b) return true;
                    if (!visited[n-1]) {
                        visited[n-1] = true;
                        q.add(n);
                    }
                }
            }
            return false;
        }


    }
}
