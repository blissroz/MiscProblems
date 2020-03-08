package Assignment2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
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
            String s1 = tokenizer.nextToken();
            tokenizer = new StringTokenizer(reader.readLine());
            String s2 = tokenizer.nextToken();
            tokenizer = new StringTokenizer(reader.readLine());
            String sReject = tokenizer.nextToken();

            printWriter.println(LCS(stringToIntArray(s1), stringToIntArray(s2), getDontUseArray(sReject)));
            //String res = intListtoString(LCS(stringToIntArray(s1), stringToIntArray(s2), getDontUseArray(sReject)));
            //List<Integer> ughh = new ArrayList<>();
            //for (int ugh : getDontUseArray(sReject)) {
            //    ughh.add(ugh);
            //}
            //String reject = intListtoString(ughh);

          // KMP_String_Matching kmp = new KMP_String_Matching();
           //printWriter.println(res.length() - kmp.KMPSearch( reject, res));
            T--;
        }

        printWriter.close();
        reader.close();
    }

    private int[] getDontUseArray(String s) {
        int[] res = new int[s.length()];
        int j = 0;
        for (int i = s.length() - 1; i >= 0; i--, j++) {
            res[j] = (Character.getNumericValue(s.charAt(i)) + 55);
        }

        return res;
    }

    private int[] stringToIntArray(String s) {
        int[] result = new int[s.length()];

        for (int i = 0; i < result.length; i++) {
            result[i] = Character.getNumericValue(s.charAt(i)) + 55;
        }

        return result;
    }

    private String intListtoString(List<Integer> a) {
        StringBuilder sb = new StringBuilder();

        for (int i : a) {
            sb.append(Character.toString((char) i));
        }

        return sb.toString();
    }

// modified from https://www.hackerrank.com/rest/contests/master/challenges/dynamic-programming-classics-the-longest-common-subsequence/hackers/Jisan/download_solution
    private int LCS(int[] A, int[] B, int[] R) {
        int[][] m = lcsTable(A, B);
        return actualLCS(m, A, B, R);
    }

    private int actualLCS(int[][] m, int[] A, int[] B, int[] R) {
        KMP kmp = new KMP(R, 150);

        int RPos = 0;
        List<Integer> result = new ArrayList<>();
        int x = A.length, y = B.length;
        while (x > 0 && y > 0) {
            if (A[x - 1] == B[y - 1] && (RPos != R.length)) {
                if (RPos == (R.length - 1) && R[RPos] == A[x-1]) {
                   //reached end of matching so just skip this one
                  //  RPos = kmp.dfa[A[x-1]][RPos] - 1;
                    //if (result.size() > 0) {
                   //     if (result.size() > 1) RPos = kmp.dfa[result.get(result.size()-1)][RPos-1];
                  //      else RPos = 0;
                  //      result.remove(result.size() - 1);
                  //  }
                    int a;
               } else if (R[RPos] == A[x-1]) {
                    result.add(A[x - 1]);
                    RPos++;
                } else {
                    result.add(A[x - 1]);
                    RPos = kmp.dfa[A[x-1]][RPos];
                }
                //result.add(A[x - 1]);
                x--;
                y--;
            } else if (m[x][y - 1] > m[x - 1][y]) {
                y--;
            } else {
                x--;
            }
        }
        return result.size();
    }

    private int[][] lcsTable(int[] A, int[] B) {
        int[][] m = new int[A.length + 1][B.length + 1];
        for (int x = 1; x <= A.length; x++) {
            for (int y = 1; y <= B.length; y++) {
                if (A[x - 1] == B[y - 1]) {
                    m[x][y] = 1 + m[x - 1][y - 1];
                } else {
                    m[x][y] = Math.max(m[x][y - 1], m[x - 1][y]);
                }
            }
        }
        return m;
    }

    private int[][][] newTable(int[] a, int[] b, int[] r) {
        int[][][] m = new int[a.length + 1][b.length + 1][r.length + 1];
        for (int x = 1; x <= a.length; x++) {
            for (int y = 1; y <= b.length; y++) {
                for (int z = 1; z <= r.length; z++) {

                }
            }
        }
        return m;
    }
    private class KMP {
        private int[][] dfa;       // the KMP automoton

        private KMP(int[] pattern, int R) {
            // build DFA from pattern
            int m = pattern.length;
            dfa = new int[R][m];
            dfa[pattern[0]][0] = 1;
            for (int x = 0, j = 1; j < m; j++) {
                for (int c = 0; c < R; c++)
                    dfa[c][j] = dfa[c][x];     // Copy mismatch cases.
                dfa[pattern[j]][j] = j+1;      // Set match case.
                x = dfa[pattern[j]][x];        // Update restart state.
            }
        }
    }

    class KMP_String_Matching
    {
        int KMPSearch(String pat, String txt)
        {
            int M = pat.length();
            int N = txt.length();
            int w = 0;

            // create lps[] that will hold the longest
            // prefix suffix values for pattern
            int lps[] = new int[M];
            int j = 0;  // index for pat[]

            // Preprocess the pattern (calculate lps[]
            // array)
            computeLPSArray(pat,M,lps);

            int i = 0;  // index for txt[]
            while (i < N)
            {
                if (pat.charAt(j) == txt.charAt(i))
                {
                    j++;
                    i++;
                }
                if (j == M)
                {
                    w++;
                    j = lps[j-1];
                }

                // mismatch after j matches
                else if (i < N && pat.charAt(j) != txt.charAt(i))
                {
                    // Do not match lps[0..lps[j-1]] characters,
                    // they will match anyway
                    if (j != 0)
                        j = lps[j-1];
                    else
                        i = i+1;
                }
            }

            return w;
        }

        void computeLPSArray(String pat, int M, int lps[])
        {
            // length of the previous longest prefix suffix
            int len = 0;
            int i = 1;
            lps[0] = 0;  // lps[0] is always 0

            // the loop calculates lps[i] for i = 1 to M-1
            while (i < M)
            {
                if (pat.charAt(i) == pat.charAt(len))
                {
                    len++;
                    lps[i] = len;
                    i++;
                }
                else  // (pat[i] != pat[len])
                {
                    // This is tricky. Consider the example.
                    // AAACAAAA and i = 7. The idea is similar
                    // to search step.
                    if (len != 0)
                    {
                        len = lps[len-1];

                        // Also, note that we do not increment
                        // i here
                    }
                    else  // if (len == 0)
                    {
                        lps[i] = len;
                        i++;
                    }
                }
            }
        }
    }
}


