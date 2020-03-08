package Assignment1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class ProblemA {
    public static void main(String[] args) throws Exception {
        ProblemA problem = new ProblemA();
        problem.problem_a();
    }

    private void problem_a() throws Exception {
        long n; //num pizzas from papa john
        long i;
        double sum;
        //double epsilon = Math.exp(-8);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        long T = Integer.parseInt(tokenizer.nextToken());
        PrintWriter printWriter = new PrintWriter(System.out);

        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            n = Integer.parseInt(tokenizer.nextToken());
            i = 0;
            sum = 0;

            while (++i <= n) {
                sum += Double.parseDouble(tokenizer.nextToken());
            }

            printWriter.printf("%.2f\n", Math.round(sum/n * 100000.0) / 100000.0);
            T--;
        }
        printWriter.close(); reader.close();
    }
}
