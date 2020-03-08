package Assignment4;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class ProblemG {
    public static void main(String[] args) throws Exception {
        ProblemG problem = new ProblemG();
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
            int[] tanks = new int[n];
            for (int i = 0; i < n; i++) tanks[i] = Integer.parseInt(tokenizer.nextToken());
            CountingInversions countingInversions = new CountingInversions();
            countingInversions.divideAndConquer(tanks);
            printWriter.println(countingInversions.total);

            T--;
        }

        printWriter.close();
        reader.close();
    }

    class CountingInversions {
        long total;

        int[] divideAndConquer(int[] inputArray) {
            int n = inputArray.length;
            if(n == 1) {
                return inputArray;
            }
            int mid = n/2;
            int[] leftArray = new int[mid];
            int[] rightArray = new int[n - mid];
            System.arraycopy(inputArray, 0, leftArray, 0, leftArray.length);
            System.arraycopy(inputArray, leftArray.length, rightArray, 0, rightArray.length);
            divideAndConquer(leftArray);
            divideAndConquer(rightArray);
            merge(leftArray, rightArray, inputArray);
            return inputArray;
        }

        void merge(int[] leftArray,
                          int[] rightArray,
                          int[] sortedArray) {
            int leftArrayLength = leftArray.length;
            int rightArrayLength = rightArray.length;
            int i = 0;
            int j = 0;
            int k = 0;
            while(i < leftArrayLength && j < rightArrayLength) {
                if(leftArray[i] < rightArray[j]) {
                    sortedArray[k] = leftArray[i];
                    i++;
                } else {
                    sortedArray[k] = rightArray[j];
                    j++;
                    total += (leftArray.length - i);
                }
                k++;
            }
            while(i < leftArrayLength) {
                sortedArray[k] = leftArray[i];
                i++;
                k++;
            }
            while(j < rightArrayLength) {
                sortedArray[k] = rightArray[j];
                j++;
                k++;
            }
        }

        private CountingInversions() { total = 0;}
    }
}


