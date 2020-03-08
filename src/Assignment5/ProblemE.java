package Assignment5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class ProblemE {
    public static void main(String[] args) throws Exception {
        ProblemE problem = new ProblemE();
        problem.problem();
    }

    private void problem() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        long T = Integer.parseInt(tokenizer.nextToken());
        PrintWriter printWriter = new PrintWriter(System.out);

        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            int a = Integer.parseInt(tokenizer.nextToken());
            int b = Integer.parseInt(tokenizer.nextToken());
            int d = Integer.parseInt(tokenizer.nextToken());
            int vl = Integer.parseInt(tokenizer.nextToken());
            int vw = Integer.parseInt(tokenizer.nextToken());
            printWriter.println(findDistance(a,b,d,vl,vw));
            T--;
        }
        printWriter.close(); reader.close();
    }

    private int a, b, d, vl, vw;

    double findDistance(int a, int b, int d, int vl, int vw) { //p1 is above
        this.a = a; this.b = b;
        this.d = d; this.vl = vl; this.vw = vw;

        double YMax = b;
        double YMin = 0;
        double div = YMax / 3.0;
        double p1 = YMax - div;
        double p2 = p1 - div;

        return ternarySearch(YMax, YMin, p1, p2);
    }

    double ternarySearch(double pTop, double pBot, double p1, double p2) {
        double atoTop = pointTopoint(0,0, d, pTop) / vl;
        double atoSecondHighest = pointTopoint(0,0, d, p1) / vl;
        double atoThirdHighest = pointTopoint(0,0, d, p2) / vl;
        double atoBottom = pointTopoint(0,0, d, pBot) / vl;

        double btoTop = pointTopoint(d, pTop, a, b)/ vw + atoTop;
        double btoSecondHighest = pointTopoint(d, p1, a, b)/vw + atoSecondHighest;
        double btoThirdHighest = pointTopoint(d, p2, a, b)/vw + atoThirdHighest;
        double btoBottom = pointTopoint(d, pBot, a, b)/vw + atoBottom;

        if (pTop - p1 < 0.0001) return  btoThirdHighest;

        double min = Math.min(btoBottom, Math.min(btoSecondHighest, Math.min(btoThirdHighest, btoTop)));

        if (btoTop == min) {
            double div = (pTop - p1)/3.0;
            return ternarySearch(pTop, p1, pTop - div, pTop - div - div);
        } else if (btoBottom == min) {
            double div = (p2 - pBot)/3.0;
            return ternarySearch(p2, pBot, p2 - div, p2 - div - div);
        } else if (btoSecondHighest == min) {
            double div = (pTop - p2)/3.0;
            return ternarySearch(pTop,p2, pTop - div, pTop - div - div);
        } else { //third highest is min
            double div = (p1 - pBot)/3.0;
            return ternarySearch(p1, pBot, p1 - div, p1 - div - div);
        }
    }

    double pointTopoint(double x1, double y1, double x2, double y2) {
        double p1a = Math.pow((x1 - x2), 2);
        double p1b = Math.pow((y1 - y2), 2);
        double p1 = p1a + p1b;
        return Math.sqrt(p1);
    }

}
