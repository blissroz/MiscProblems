package Assignment1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

public class ProblemE {
    public static void main(String[] args) throws Exception {
        ProblemE problem = new ProblemE();
        problem.problem_e();
    }

    private void problem_e() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(System.out);

        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        int T = Integer.parseInt(tokenizer.nextToken());
        LinkedList<Integer> results = new LinkedList<>();

        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            int n = Integer.parseInt(tokenizer.nextToken());

            if (n == 1) writer.println(0);
            else {
                int[] startOrder = new int[n];
                int[] endOrder = new int[n];
                for (int i = 0; i < n; i++) {
                    startOrder[i] = Integer.parseInt(tokenizer.nextToken());
                    endOrder[i] = i;
                }
                Graph g = new Graph(n, startOrder, endOrder);
            }

            T--;
        }

        for (int res : results) {
            if (res == -1) {
                writer.println("IMPOSSIBLE");
            } else {
                writer.println(res);
            }
        }

        reader.close(); writer.close();
    }

    private class Graph {
        int n;
        int[][] positions;
        HashSet<int[][]> visited;
        int[] start;
        int[] end;
        HashMap<Node,LinkedList<Node>> adj;
        LinkedList[] adjacent;

        Graph(int n, int[] start, int[]end) {
            this.n = n; this.start = start; this.end = end;
            positions = new int[n][n];
            visited = new HashSet<>();
            adj = new HashMap<>();
            adjacent = new LinkedList[(int) Math.pow(10,n)];
            for (int d = 0; d < (int)Math.pow(10,n); d++) adjacent[d] = new LinkedList<Integer>();
            for (int i = 0; i < n; i++) for (int j = 0; j < n; j++) positions[i][j] = 0;
            for (int k : start) positions[k][0] = start[k];
        }

        public class Node {
            int[][] val;
            LinkedList<Node> adj;

            Node(int[][] value){
                val = value;
                adj = new LinkedList<>();
            }

            boolean equals(Node node) {
                for (int i = 0; i < n; i++)
                    for (int j = 0; j<n; j++)
                        if (node.val[i][j] != val[i][j]) return false;
                return true;
            }

            int nodeCode() {
                int[] pos = new int[n];
                for (int i = 0; i < n; i++) for (int j = 0; j< n; j++) {
                    if (val[i][j] != 0) {
                        pos[val[i][j]] = i;
                    }
                }
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < n; k++) sb.append(pos[k]);
                return Integer.parseInt(sb.toString());
            }
        }

        void createGraph() {
            Node start = new Node(positions);
            LinkedList<Node> queue = new LinkedList<>();
            queue.add(start);

            Node current;
            while (!queue.isEmpty()) {
                current = queue.poll();
                LinkedList<Node> adjToCurrent = new LinkedList<>();

                for (int[][] possible : nextPossibleNodes(current.val)) {
                    if (atEnd(possible)) return;

                    Node node = new Node(possible);
                    adjToCurrent.add(node);
                    if (!queue.contains(node)) queue.add(node);
                }

                adj.put(current,adjToCurrent);
            }
        }

        LinkedList<int[][]> nextPossibleNodes(int[][] currentLocation) {
            LinkedList<int[][]> nextPossibleMoves = new LinkedList<>();
            int[][] newPosition = new int[n][n];
            int[]currentcol = new int[n];
            int[]lastcol = new int[n];
            int[]nextcol = new int[n];

            for (int x = 0; x < n; x++){ //for every individual one
                for (int i = 0; i < n; i ++) for (int j = 0; j < n; j++) newPosition[i][j] = currentLocation[i][j];
                for (int j = 0; j < n; j++) currentcol[j] = currentLocation[x][j];

                if (x==0) //first one
                {
                    for (int k = 0; k < n; k++) nextcol[k] = currentLocation[1][k];
                    int a = lightest(currentcol), b = lightest(nextcol);
                    if ((a<b) && (a!=0)) { // can move top one to adjacent
                        newPosition[x][a] = 0;
                        newPosition[x+1][a] = 1;
                        nextPossibleMoves.add(newPosition);
                    }
                } else if (x==n-1) //last one
                {
                    for (int k = 0; k < n; k++) lastcol[k] = currentLocation[n-2][k];
                    if (lightest(currentcol) < lightest(lastcol)) { // can move top one to adjacent
                        newPosition[x][lightest(currentcol)] = 0;
                        newPosition[x-1][lightest(currentcol)] = 1;
                        nextPossibleMoves.add(newPosition);
                    }
                } else {
                    for (int k = 0; k < n; k++) nextcol[k] = currentLocation[x+1][k];
                    for (int k = 0; k < n; k++) lastcol[k] = currentLocation[x-1][k];
                    int a = lightest(currentcol), b = lightest(lastcol), c = lightest(nextcol);
                    if ((a < b) && (a!=0)) { // can move top one to adjacent
                        newPosition[x][a] = 0;
                        newPosition[x-1][a] = 1;
                        nextPossibleMoves.add(newPosition);
                    }
                    //reset early in case could go either way
                    for (int i = 0; i < n; i ++) for (int j = 0; j < n; j++) newPosition[i][j] = currentLocation[i][j];

                    if ((a < c) && (a!=0)) { // can move top one to adjacent
                        newPosition[x][a] = 0;
                        newPosition[x+1][a] = 1;
                        nextPossibleMoves.add(newPosition);
                    }
                }

            }

            return nextPossibleMoves;
        }

        int lightest(int[] set) {
            for (int i = 0; i < n; i++) {
                if (set[i] != 0) return i;
            }
            return 0;
        }

        boolean atEnd(int[][] possible) {
            int check[] = new int[n];
            for (int i = 0; i < n; i++) check[i] = possible[i][0];
            return Arrays.equals(check,end);
        }
    }

    private int shortestPath(Graph g, int start, int end) {
        return 0;
    }

}
