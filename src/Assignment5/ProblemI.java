package Assignment5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class ProblemI {
    public static void main(String[] args) throws Exception {
        ProblemI problem = new ProblemI();
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
            printWriter.println("Q 0 0 " + n + " " + n);
            printWriter.flush();

            tokenizer = new StringTokenizer(reader.readLine());
            int area = Integer.parseInt(tokenizer.nextToken());

            int x1, x2, y1, y2;
            x1 = y1 = 0;
            x2 = y2 = n;

            int newArea = area;

            int below = 1; int above = n;
            x2 /= 2;

            while (below != above) {
                if (x2 == 0) x2 = 1;
                printWriter.println("Q 0 0 " + x2 + " " + n);
                printWriter.flush();
                tokenizer = new StringTokenizer(reader.readLine());
                newArea = Integer.parseInt(tokenizer.nextToken());

                if (newArea < area) {
                    below = x2 + 1;
                    x2 += Math.ceil((above - below) / 2.0);
                    //below = temp + 1;
                } else {
                    above = x2;
                    x2 -= Math.ceil((above - below) / 2.0);
                    if (x2 == above) break;
                }
            }
            x2 = above;

            below = 1; above = n;
            y2 /= 2;
            while (below != above) {
                if (y2 == 0) y2 = 1;
                printWriter.println("Q 0 0 " + x2 + " " + y2);
                printWriter.flush();
                tokenizer = new StringTokenizer(reader.readLine());
                newArea = Integer.parseInt(tokenizer.nextToken());

                if (newArea < area) {
                    below = y2 + 1;
                    y2 += Math.ceil((above - below) / 2.0);
                    //below = temp + 1;
                } else {
                    above = y2;
                    y2 -= Math.ceil((above - below) / 2.0);
                    if (y2 == above) break;
                }
            }
            y2 = above;

            below = 0; above = x2;
            while (below != above) {
                printWriter.println("Q " + x1 + " 0 " + x2 + " " + y2);
                printWriter.flush();
                tokenizer = new StringTokenizer(reader.readLine());
                newArea = Integer.parseInt(tokenizer.nextToken());

                if (newArea < area) {
                    int temp = x1;
                    x1 -= (above - below) / 2;
                    above = temp;
                } else {
                    below = x1;
                    x1 += (above - below) /2;
                    if (x1 == below) break;
                }
            }
            x1 = below;

            below = 0; above = y2;
            while (below != above) {
                printWriter.println("Q " + x1 + " " + y1 + " " + x2 + " " + y2);
                printWriter.flush();
                tokenizer = new StringTokenizer(reader.readLine());
                newArea = Integer.parseInt(tokenizer.nextToken());

                if (newArea < area) {
                    int temp = y1;
                    y1 -= (above - below) / 2;
                    above = temp;
                } else {
                    below = y1;
                    y1 += (above - below) /2;
                    if (y1 == below) break;
                }
            }
            y1 = below;

            printWriter.println("A " + x1 + " " + y1 + " " + x2 + " " + y2);
            printWriter.flush();

            T--;
        }
        printWriter.close();
        reader.close();
    }
}
