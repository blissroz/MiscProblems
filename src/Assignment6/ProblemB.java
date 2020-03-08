package Assignment6;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class ProblemB {
    public static void main(String[] args) throws Exception {
        ProblemB problem = new ProblemB();
        problem.problem();
    }

    private void problem() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        long T = Integer.parseInt(tokenizer.nextToken());
        PrintWriter printWriter = new PrintWriter(System.out);

        while (T > 0) {
            T--;
        }
        printWriter.close(); reader.close();
    }


}
