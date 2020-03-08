package Assignment1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class ProblemC {
    public static void main(String[] args) throws Exception {
        ProblemC problem = new ProblemC();
        problem.problem_c();
    }

    private void problem_c() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(System.out);

        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        int T = Integer.parseInt(tokenizer.nextToken());

        int n, m, a, b, t;
        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            n = Integer.parseInt(tokenizer.nextToken());
            m = Integer.parseInt(tokenizer.nextToken());

            Graph graph = new Graph(n,m);
            while (m > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                a = Integer.parseInt(tokenizer.nextToken());
                b = Integer.parseInt(tokenizer.nextToken());
                t = Integer.parseInt(tokenizer.nextToken());

                m--;

                graph.edge[m].src = a;
                graph.edge[m].dest = b;
                graph.edge[m].weight = t;
            }
            writer.println(graph.KruskalMST());
            T--;
        }
        reader.close(); writer.close();
    }

    // borrowed from https://www.geeksforgeeks.org/greedy-algorithms-set-2-kruskals-minimum-spanning-tree-mst/

    class Graph {
        // A class to represent a graph edge
        class Edge implements Comparable<Edge> {
            int src, dest, weight;

            // Comparator function used for sorting edges
            // based on their weight
            public int compareTo(Edge compareEdge) {
                return this.weight - compareEdge.weight;
            }
        }

        // A class to represent a subset for union-find
        class subset {
            int parent, rank;
        }

        int V, E;    // V-> no. of vertices & E->no.of edges
        Edge edge[]; // collection of all edges

        // Creates a graph with V vertices and E edges
        Graph(int v, int e) {
            V = v;
            E = e;
            edge = new Edge[E];
            for (int i = 0; i < e; ++i)
                edge[i] = new Edge();
        }

        // A utility function to find set of an element i
        // (uses path compression technique)
        int find(subset subsets[], int i) {
            // find root and make root as parent of i (path compression)
            if (subsets[i].parent != i)
                subsets[i].parent = find(subsets, subsets[i].parent);

            return subsets[i].parent;
        }

        // A function that does union of two sets of x and y
        // (uses union by rank)
        void Union(subset subsets[], int x, int y) {
            int xroot = find(subsets, x);
            int yroot = find(subsets, y);

            // Attach smaller rank tree under root of high rank tree
            // (Union by Rank)
            if (subsets[xroot].rank < subsets[yroot].rank)
                subsets[xroot].parent = yroot;
            else if (subsets[xroot].rank > subsets[yroot].rank)
                subsets[yroot].parent = xroot;

                // If ranks are same, then make one as root and increment
                // its rank by one
            else {
                subsets[yroot].parent = xroot;
                subsets[xroot].rank++;
            }
        }

        // The main function to construct MST using Kruskal's algorithm
        int KruskalMST() {
            if (this.E < 3) return -1;

            int e = 0;  // An index variable, used for result[]
            int i = 0;  // An index variable, used for sorted edges

            // Step 1:  Sort all the edges in non-decreasing order of their
            // weight.  If we are not allowed to change the given graph, we
            // can create a copy of array of edges
            Arrays.sort(edge);

            // Allocate memory for creating V ssubsets
            subset subsets[] = new subset[V+1];
            for (i = 0; i <= V; ++i)
                subsets[i] = new subset();

            // Create V subsets with single elements
            for (int v = 0; v <= V; ++v) {
                subsets[v].parent = v;
                subsets[v].rank = 0;
            }

            i = 0;  // Index used to pick next edge

            // Number of edges to be taken is equal to V-1
            while (e < V - 1) {
                // Step 2: Pick the smallest edge. And increment
                // the index for next iteration
                Edge next_edge;
                try {
                    next_edge = edge[i++];
                } catch (Exception exc) {
                    return -1;
                }

                int x = find(subsets, next_edge.src);
                int y = find(subsets, next_edge.dest);

                // If including this edge does't cause cycle,
                // include it in result and increment the index
                // of result for next edge
                if (x != y) {
                    Union(subsets, x, y);
                } else {
                    return next_edge.weight;
                }
                // Else discard the next_edge
            }

            return -1;
        }
    }
}
