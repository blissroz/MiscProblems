package Assignment2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;

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
            tokenizer = new StringTokenizer(reader.readLine());
            int n = Integer.parseInt(tokenizer.nextToken());
            List<Box> boxes = new ArrayList<>();

            while (n > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                long l, w;
                l = Long.parseLong(tokenizer.nextToken());
                w = Long.parseLong(tokenizer.nextToken());
                boxes.add(new Box(l,w));
                n--;
            }

            order(boxes);
            LIS lis = new LIS();
            Box[] arr = boxes.toArray(new Box[boxes.size()]);
            printWriter.println(lis.LongestIncreasingSubsequenceLength(arr,boxes.size()));

            T--;
        }
        printWriter.close(); reader.close();
    }


    class Box {
        long w, l;

        Box(long l, long w) {
            this.l = l;
            this.w = w;
        }
    }

    private static void order(List<Box> boxes) {
        Collections.sort(boxes, new Comparator() {

            public int compare(Object o1, Object o2) {

                long l1 = ((Box) o1).l;
                long l2 = ((Box) o2).l;
                int sComp = Long.compare(l1,l2);

                if (sComp != 0) {
                    return sComp;
                } else {
                    Long w1 = ((Box) o1).w;
                    Long w2 = ((Box) o2).w;
                    return Long.compare(w2,w1);
                }
            }});
    }

    class LIS
    {
        // Binary search (note boundaries in the caller)
        // A[] is ceilIndex in the caller
        int CeilIndex(long A[], int l, int r, long key)
        {
            while (r - l > 1) {
                int m = l + (r - l)/2;
                if (A[m]>=key)
                    r = m;
                else
                    l = m;
            }
            return r;
        }

        long LongestIncreasingSubsequenceLength(Box A[], int size)
        {
            // Add boundary case, when array size is one

            long[] tailTable   = new long[size];
            int len; // always points empty slot

            tailTable[0] = A[0].w;
            len = 1;
            for (int i = 1; i < size; i++) {
                if (A[i].w < tailTable[0])
                    // new smallest value
                    tailTable[0] = A[i].w;
                else if (A[i].w > tailTable[len-1])
                    // A[i] wants to extend largest subsequence
                    tailTable[len++] = A[i].w;
                else
                    // A[i] wants to be current end candidate of an existing
                    // subsequence. It will replace ceil value in tailTable
                    tailTable[CeilIndex(tailTable, -1, len-1, A[i].w)] = A[i].w;
            }
            return len;
        }
    }
}
