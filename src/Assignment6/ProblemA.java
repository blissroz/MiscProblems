package Assignment6;

import java.io.*;
import java.util.StringTokenizer;

public class ProblemA {
    public static void main(String[] args) throws Exception {
        ProblemA problem = new ProblemA();
        problem.temp = new long[300001];
        problem.problem();
    }

    long mod = 1000000007;
    private long temp[];

    private void problem() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        long T = Integer.parseInt(tokenizer.nextToken());
        PrintWriter printWriter = new PrintWriter(System.out);

        for (int i = 0; i < 300001; i++) temp[i] = 0;
        temp[0] = 1;
        temp[1] = 1;
        temp[2] = 2;
        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            int r = Integer.parseInt(tokenizer.nextToken());
            int b = Integer.parseInt(tokenizer.nextToken());
            int g = Integer.parseInt(tokenizer.nextToken());

            long total = fact(r + b + g);
            long rt, bt, gt;
            rt = fact(r); bt = fact(b); gt = fact(g);
            long rti, bti, gti;
            rti = modInverse(rt, mod); bti = modInverse(bt, mod); gti = modInverse(gt, mod);
            printWriter.println(total%mod * rti%mod * gti%mod *bti%mod);
            T--;
        }
        printWriter.close(); reader.close();
    }

    private long fact(int val) {
        if (temp[val] !=  0) return temp[val];

        int i2 = 2;
        while (i2 <= val){
            temp[i2] =  (i2 % mod) * (temp[i2-1] % mod) % mod;
            i2++;
        }

        return temp[val];
    }

    private long modInverse(long a, long n) {
        long i = n, v = 0, d = 1;

        while (a>0) {
            long t = i/a, x = a;
            a = i % x;
            i = x;
            x = d;
            d = v - t*x;
            v = x;
        }

        v %= n;
        if (v<0) v = (v+n)%n;
        return v;
    }

}
