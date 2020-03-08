package Assignment1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class ProblemF {
    public static void main(String[] args) throws Exception {
        ProblemF problem = new ProblemF();
        problem.problem_f();
    }

    public void problem_f() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(System.out);

        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        int T = Integer.parseInt(tokenizer.nextToken());
        List<String> results = new LinkedList<>();

        HashMap<String,Integer> pizzas;
        String[] pizzas2;
        int n, m;
        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            n = Integer.parseInt(tokenizer.nextToken());
            pizzas = new HashMap<>();
            pizzas2 = new String[n];
            while (n > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                String pizza = tokenizer.nextToken();
                pizzas.put(pizza,n-1);
                pizzas2[n-1] = pizza;
                n--;
            }

            tokenizer = new StringTokenizer(reader.readLine());
            m = Integer.parseInt(tokenizer.nextToken());

            Graph graph = new Graph(pizzas2.length, pizzas2, pizzas);
            while (m > 0) {
                tokenizer = new StringTokenizer(reader.readLine(), "() ");
                String a = tokenizer.nextToken();
                String relation = tokenizer.nextToken();
                String b = tokenizer.nextToken();

                int aVal, bVal;
                if (a.contains("-"))
                    aVal = graph.inverseNode(graph.pizzaToNum(a.substring(1)));
                else
                    aVal = graph.pizzaToNum(a);
                if (b.contains("-"))
                    bVal = graph.inverseNode(graph.pizzaToNum(b.substring(1)));
                else
                    bVal = graph.pizzaToNum(b);

                graph.addEdge(aVal, bVal, relation);
                m--;
            }

            LinkedList<String> res = graph.results();
            if (res.size() == 0)
                writer.println("-1");
            else {
                writer.println(res.size());
                for (String s : res) writer.println(s);
            }

            T--;
        }

        for (String r : results) {
            writer.println(r);
        }
        reader.close(); writer.close();
    }

    private class Graph {
        private int V;   // No. of vertices
        private LinkedList<Integer> adj[]; //Adjacency List
        String[] pizzas;
        HashMap<String, Integer> pizzas2;
        int add_to_neg;
        AndGraph ag; boolean andFlag;

        //Constructor
        Graph(int v, String[] pizzas, HashMap<String,Integer> pizzas2) {
            V = v*2; add_to_neg = v;
            adj = new LinkedList[V];
            for (int i = 0; i < V; ++i) {
                adj[i] = new LinkedList<>();
            }
            this.pizzas = pizzas;
            this.pizzas2 = pizzas2;

            ag =new AndGraph();
            andFlag = false;
        }

        Integer pizzaToNum(String a) {
            return this.pizzas2.get(a);
        }

        String numToPizza(int a) {
            return this.pizzas[a];
        }

        int[] visited;
        int[] scomp;
        int[] low;
        int scompNum, I;
        boolean truth[];
        Stack<Integer> verts;

        boolean TWOSAT(int n) {
            low = new int[V];
            truth = new boolean[V/2];
            get_scc(n);
            for (int i = 0; i < n; i += 2) {
                if (scomp[i] == scomp[i^1]) return false;
                truth[i/2] = (scomp[i] < scomp[i^1]);
            }
            return true;
        }

        void get_scc(int n) {
            visited =new int[V];
            for (int i = 0; i < V; i++) visited[i] = -1;
            scomp = new int[V];
            scompNum = 0; I = 0;
            verts = new Stack<>();
            for (int i = 0; i<n; ++i) if (visited[i] == -1) scc(i);
        }

        void scc(int u) {
            low[u] = visited[u] = ++I; verts.push(u);
            for (int v : adj[u]) {
                if(visited[v] == -1) scc(v); // chec?
                if (scomp[v] == -1) low[u] = Math.min(low[u], low[v]);
            }
            if (visited[u] <= low[u]) {
                int v;
                do {
                    v = verts.peek(); verts.pop(); scomp[v] = scompNum;
                } while (v != u);
                ++scompNum;
            }
        }

        LinkedList<String> results() {
          /*  if (andFlag) {
                if (ag.falseflag) return new LinkedList<>();
                return ag.andGraphResult();
            }
            */
            boolean what = TWOSAT(V);

            if (what) {
                LinkedList<Integer> result = new LinkedList<>();
                LinkedList<String> results = new LinkedList<>();

                for (int i = 0; i < truth.length; i++) {
                    if (truth[i]) result.add(i);
                }
/*
            LinkedList<String> results = new LinkedList<>();
            LinkedList<Integer> result = null;
            LinkedList<Integer> components;

            for (int i=0; i < V; i++) {
                components = new LinkedList<>();
                result = getComponents(i,components);
                if (result != null) {
                    if (!result.contains(i))
                        result.add(i);
                    break;
                }
            }*/

                if (result.size() != 0) {
                    for (int i : result) {
                        //if (i < add_to_neg)
                        results.add(numToPizza(i));
                    }
                }

                return results;
            } else return new LinkedList<>();
        }

        LinkedList<Integer> getComponents(int i, LinkedList<Integer> components) {
            for (int j : adj[i]) {
                if (components == null) return null;
                if (components.contains(inverseNode(j))) return null;
                if (components.contains(j)) return components;

                components.add(j);
                components = getComponents(j, components);
            }
            return components;
        }

        void addEdge(int v, int w, String relation) {
            switch(relation) {
                case "|":
                    //if (v == w) {
                        //adj[v].add(w); adj[w].add(v);
                        //addEdge(v, w, "=>");
                        //addEdge(w, v, "=>");
                    //} else {
                        addEdge(inverseNode(w), v, "=>");
                        addEdge(inverseNode(v),w,"=>");
                    //} //OR
                    break;
                case "&": addEdge(v, inverseNode(w), "=>"); addEdge(w, inverseNode(v), "=>");//ag.addAnd(v,w); andFlag = true;
                    break;
                default:
                    if (v != w)
                        adj[v].add(w); //=>, ignore if to itself
                    break;
            }
        }

        class AndGraph{
            HashSet<Integer> andResults;
            boolean falseflag;

            AndGraph() {
                andResults = new HashSet<>();
                falseflag = false;
            }

            void addAnd(int a, int b) {
                if (inverseNode(a) == b) falseflag = true;
                if (andResults.contains(inverseNode(a))) falseflag = true;
                if (andResults.contains(inverseNode(b))) falseflag = true;
                andResults.add(a);
                andResults.add(b);
            }

            LinkedList<String> andGraphResult() {
               LinkedList<String> toReturn = new LinkedList<>();
               for (int res : andResults) {
                   if (res < add_to_neg) {
                       toReturn.add(numToPizza(res));
                   }
               }
               return toReturn;
            }
        }


        int inverseNode(int node) {
            if (node >= add_to_neg) {
                return node-add_to_neg;
            }
            return node+add_to_neg;
        }
    }

}
