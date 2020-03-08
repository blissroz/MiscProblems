package Assignment6;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class ProblemF {
    public static void main(String[] args) throws Exception {
        ProblemF problem = new ProblemF();
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

            ArrayList<Rook> p_rooks = new ArrayList<>();
            boolean board[][] = new boolean[n][n];
            int i = 0;
            while (i < n) {
                tokenizer = new StringTokenizer(reader.readLine());
                int j = 0;
                String[] token = tokenizer.nextToken().split("");
                for (String tok : token) {
                    if (tok.equals(".")) {
                        p_rooks.add(new Rook(i, j));
                        board[i][j] = true;
                    }
                    j++;
                }
                i++;
            }

            MaxBipartite gr = new MaxBipartite(p_rooks.size());
            boolean[][] g = new boolean[p_rooks.size()][p_rooks.size()];
            int it = 0;
            for (Rook pr : p_rooks) {
                int jt = 0;
                for (Rook sr : p_rooks) {
                    if (it == jt) g[it][jt] = true;
                    else {
                        if (canGetTo(pr, sr, board))
                            g[it][jt] = true;
                    }
                    jt++;
                }
                it++;
            }
            if (p_rooks.size() > 0) printWriter.println(gr.maxBPM(g));
            else  printWriter.println(0);
            T--;
        }
        printWriter.close(); reader.close();
    }

    private boolean canGetTo(Rook a, Rook b, boolean[][] area) {
        if(a.x == b.x && a.y == b.y) return true;
        if(a.x != b.x && a.y != b.y) return false;

        if (a.x == b.x) {
            int yp = a.y, yq = b.y;
            if (a.y > b.y) {
                while (yp != yq) {
                    yp--;
                    if (!area[a.x][yp]) return false;
                }
            } else { // b.y > a.y
                while (yp != yq) {
                    yq--;
                    if (!area[a.x][yq]) return false;
                }
            }
        } else {
            int yp = a.x, yq = b.x;
            if (a.x > b.x) {
                while (yp != yq) {
                    yp--;
                    if (!area[yp][a.y]) return false;
                }
            } else { // b.x > a.x
                while (yp != yq) {
                    yq--;
                    if (!area[yq][a.y]) return false;
                }
            }
        }

        return true;
    }

    class Rook {
        int x, y;
        Rook(int x, int y) {
            this.x = x; this.y = y;
        }
    }

    class MaxBipartite {
        int M;

        MaxBipartite (int M) {
            this.M = M;
        }

        boolean bpm(boolean bpGraph[][], int u, boolean seen[], int matchR[]) {
            for (int v = 0; v < M; v++) {
                if (bpGraph[u][v] && !seen[v]) {
                    seen[v] = true;
                    if (matchR[v] < 0 || bpm(bpGraph, matchR[v], seen, matchR)) {
                        matchR[v] = u;
                        return true;
                    }
                }
            }
            return false;
        }

        int maxBPM(boolean bpGraph[][]) {
            int matchR[] = new int[M];

            for(int i=0; i<M; ++i) matchR[i] = -1;

            int result = 0;
            for (int u = 0; u < M; u++) {
                boolean seen[] = new boolean[M] ;
                for(int i=0; i<M; ++i) seen[i] = false;

                if (bpm(bpGraph, u, seen, matchR)) result++;
            }
            return result;
        }
    }
}
