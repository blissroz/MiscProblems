package Assignment6;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

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

            ArrayList<Balloon> bs = new ArrayList<>();
            int ni = 0;
            while (ni < n) {
                tokenizer = new StringTokenizer(reader.readLine());
                int x = Integer.parseInt(tokenizer.nextToken());
                int y = Integer.parseInt(tokenizer.nextToken());
                Balloon b = new Balloon(x, y);
                if (y >= Math.abs(x)) bs.add(b);
                ni++;
            }

            if (bs.size() > 0) {
                MaxBipartite g = new MaxBipartite(bs.size());
                boolean bpg[][] = new boolean[bs.size()][bs.size()];

                for (int i = 0; i < bs.size(); i++) {
                    for (int j = 0; j < bs.size(); j++) {
                        if (i != j) {
                            int timediff = bs.get(j).y - bs.get(i).y;
                            int distance = Math.abs(bs.get(j).x - bs.get(i).x);
                            if (distance <= timediff) bpg[i][j] = true;
                        }
                    }
                }
                int M = g.maxBPM(bpg);
                printWriter.println(bs.size() - M);
            } else printWriter.println(0);

            T--;
        }
        printWriter.close(); reader.close();
    }

    class Balloon {
        int x, y;
        Balloon(int x, int y) {
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
