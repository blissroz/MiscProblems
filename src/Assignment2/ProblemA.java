package Assignment2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ProblemA {
    public static void main(String[] args) throws Exception {
        ProblemA problem = new ProblemA();
        problem.problem_a();
    }

    private void problem_a() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        long T = Integer.parseInt(tokenizer.nextToken());
        PrintWriter printWriter = new PrintWriter(System.out);

        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            long m = Long.parseLong(tokenizer.nextToken());
            printWriter.println(solveM(m));
            T--;
        }
        printWriter.close(); reader.close();
    }

    private long solveM(long m) {
        if (m == 0) return 0;
        if (m == 1) return 13;

        long div = (long) Math.pow(10,9) + 7;

        long[] arrange = {0,1,2,4,5,8,9,10,16,17,18,20,21};
        long[] nexts = new long[arrange.length];
        long[] current = new long[arrange.length];
        Arrays.fill(current,1);

        for (int i = 0; i < arrange.length; i++) {
            long poss = 0;
            for (int j = 0; j < arrange.length;j++) {
                if (( arrange[i] & arrange[j]) == 0) {
                    poss++;
                    //current[j]++;
                }
            }
            nexts[i] = poss;
        }

        long[] temp = new long[current.length];
        while (m > 2) {
            temp[0] = (current[0] + current[1] + current[2] + current[3] + current[4] + current[5] + current[6]
                    + current[7] + current[8] + current[9] + current[10] + current[11] + current[12]) % div;
            temp[1] = (current[0] + current[2] + current[3] + current[5] + current[7] + current[8] + current[10]
                    + current[11]) % div;
            temp[2] = (current[0] + current[1] + current[3] + current[4] + current[5] + current[6] + current[8]
                    + current[9] + current[11] + current[12]) % div;
            temp[3] = (current[0] + current[1] + current[2] + current[5] + current[6] + current[7] + current[8]
                    + current[9] + current[10]) % div;
            temp[4] = (current[0] + current[2] + current[5] + current[7] + current[8] + current[10]) % div;
            temp[5] = (current[0] + current[1] + current[2] + current[3] + current[4] + current[8] + current[9]
                    + current[10] + current[11] + current[12]) % div;
            temp[6] = (current[0] + current[2] + current[3] + current[8] + current[10] + current[11]) % div;
            temp[7] = (current[0] + current[1] + current[3] + current[4] + current[8] + current[9] + current[11]
                    + current[12]) % div;
            temp[8] = (current[0] + current[1] + current[2] + current[3] + current[4] + current[5] + current[6]
                    + current[7]) % div;
            temp[9] = (current[0] + current[2] + current[3] + current[5] + current[7]) % div;
            temp[10] = (current[0] + current[1] + current[3] + current[4] + current[5] + current[6]) % div;
            temp[11] = (current[0] + current[1] + current[2] + current[5] + current[6] + current[7]) % div;
            temp[12] = (current[0] + current[2] + current[5] + current[7]) % div;
            current = Arrays.copyOf(temp, temp.length);
            m--;
        }

        //once got all current endings, multiply to find total poss for next one
        long sum = 0;
        for (int i = 0; i < arrange.length; i++) {
            sum += (current[i] * nexts[i]) % div;
        }
        return sum % div;
    }
}
