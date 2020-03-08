package Assignment2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ProblemH {
    public static void main(String[] args) throws Exception {
        ProblemH problem = new ProblemH();
        problem.problem();
    }

    private void problem() throws Exception {
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
        if (m < 0) return 0;
        if (m == 0) return 0;
        if (m == 1) return 13;
        long[] arrange = {0,1,2,4,5,8,9,10,16,17,18,20,21};
        int[] nexts = new int[arrange.length];
        long[] current = new long[arrange.length];
        Arrays.fill(current,1);

        for (int i = 0; i < arrange.length; i++) {
            int poss = 0;
            for (int j = 0; j < arrange.length;j++) {
                if (( arrange[i] & arrange[j]) == 0) {
                    poss++;
                }
            }
            nexts[i] = poss;
        }
        long[][] ne = new long[arrange.length][1];
        for (int i = 0 ; i < arrange.length; i++) ne[i][0] = nexts[i];

        long[][] t = {
                {1,1,1,1,1,1,1,1,1,1,1,1,1}, //A
                {1,0,1,1,0,1,0,1,1,0,1,1,0}, //B
                {1,1,0,1,1,1,1,0,1,1,0,1,1},//c
                {1,1,1,0,0,1,1,1,1,1,1,0,0},//d
                {1,0,1,0,0,1,0,1,1,0,1,0,0}, //e
                {1,1,1,1,1,0,0,0,1,1,1,1,1},//f
                {1,0,1,1,0,0,0,0,1,0,1,1,0}, //g
                {1,1,0,1,1,0,0,0,1,1,0,1,1},//h
                {1,1,1,1,1,1,1,1,0,0,0,0,0},//i
                {1,0,1,1,0,1,0,1,0,0,0,0,0}, //j
                {1,1,0,1,1,1,1,0,0,0,0,0,0},  //k
                {1,1,1,0,0,1,1,1,0,0,0,0,0}, //l
                {1,0,1,0,0,1,0,1,0,0,0,0,0}}; //m

        long div = (long) Math.pow(10,9) + 7;

        long[][]a= fast_pow(t,m-1);

        long result = 0;
        for (int i = 0; i < arrange.length; i++) {
            result += a[i][0]%div * nexts[i]%div;
        }

        return result%div;
    }

    long[][] fast_pow(long m[][],long n) {
        long[][] a = identity(m.length);
        double npow = Math.log(n)/Math.log(2);
        for (long k = 0; k <= npow; k++) {
            if ((n & (long)Math.pow(2,k)) != 0) {
                a = multiplyByMatrix(a,m);
            }
            m = multiplyByMatrix(m,m);
        }

        return a;
    }

    public long[][] multiplyByMatrix(long[][] m1, long[][] m2) {
        long div = (long) Math.pow(10,9) + 7;
        int m1ColLength = m1[0].length; // m1 columns length
        int m2RowLength = m2.length;    // m2 rows length
        if(m1ColLength != m2RowLength) return null; // matrix multiplication is not possible
        int mRRowLength = m1.length;    // m result rows length
        int mRColLength = m2[0].length; // m result columns length
        long[][] mResult = new long[mRRowLength][mRColLength];
        for(int i = 0; i < mRRowLength; i++) {         // rows from m1
            for(int j = 0; j < mRColLength; j++) {     // columns from m2
                mResult[i][j] = 0;
                for(int k = 0; k < m1ColLength; k++) { // columns from m1
                    mResult[i][j] += (m1[i][k]%div * m2[k][j]%div) %div;
                    mResult[i][j] %= div;
                }
            }
        }
        return mResult;
    }

    long[][] identity(int N) {
        long[][] I = new long[N][N];
        for (int i = 0; i < N; i++)
            I[i][i] = 1;
        return I;
    }
}
