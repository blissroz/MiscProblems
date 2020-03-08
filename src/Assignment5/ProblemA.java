package Assignment5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class ProblemA {
    public static void main(String[] args) throws Exception {
        ProblemA problem = new ProblemA();
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
            int x1 = Integer.parseInt(tokenizer.nextToken());
            int y1 = Integer.parseInt(tokenizer.nextToken());
            int x2 = Integer.parseInt(tokenizer.nextToken());
            int y2 = Integer.parseInt(tokenizer.nextToken());
            int x3 = Integer.parseInt(tokenizer.nextToken());
            int y3 = Integer.parseInt(tokenizer.nextToken());

            fPoint v1 = new fPoint(x1,y1);
            fPoint v2 = new fPoint(x2,y2);
            fPoint v3 = new fPoint(x3,y3);
            Triangle triangle = new Triangle(v1, v2, v3);
            while (n > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                int x = Integer.parseInt(tokenizer.nextToken());
                int y = Integer.parseInt(tokenizer.nextToken());
                if (pointInTriangle(x, y, triangle)) printWriter.println("DANGER");
                else printWriter.println("SAFE");
                n--;
            }

            T--;
        }
        printWriter.close();
        reader.close();
    }

    class fPoint {
        int x, y;

        fPoint(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    class Triangle {
        fPoint a, b, c;
        int dAB, dAC, dBC;

        Triangle(fPoint x, fPoint y, fPoint z) {
            this.a = x;
            this.b = y;
            this.c = z;
        }
    }

    private boolean pointInTriangle(int x, int y, Triangle triangle) {
        fPoint point = new fPoint(x, y);

        int cABP = crossProduct(triangle.a,triangle.b,point);
        int cBCP = crossProduct(triangle.b,triangle.c,point);
        int cCAP = crossProduct(triangle.c,triangle.a,point);

        return sameSign(cABP, cBCP, cCAP);
    }

    private boolean sameSign(int a, int b, int c) {
        if (a >= 0 && b >= 0 && c >= 0) return true;
        return (a <= 0 && b <= 0 && c <= 0);
    }

    private int crossProduct(fPoint x, fPoint y, fPoint z) {
        fPoint vecA = new fPoint(x.x-y.x, x.y-y.y);
        fPoint vecB = new fPoint(z.x-y.x, z.y-y.y);
        return vecA.x*vecB.y - vecA.y*vecB.x;
    }
}
