package Assignment1;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.StringTokenizer;

public class ProblemD {
    public static void main(String[] args) throws Exception {
        ProblemD problem = new ProblemD();
        problem.problem_d();
    }


    private void problem_d() throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        PrintWriter writer = new PrintWriter(System.out);

        StringTokenizer tokenizer = new StringTokenizer(reader.readLine());
        int T = Integer.parseInt(tokenizer.nextToken());

        while (T > 0) {
            tokenizer = new StringTokenizer(reader.readLine());
            int n = Integer.parseInt(tokenizer.nextToken());

            int temp = 0;
            String table[] = new String[n + 2];
            while (temp < n+2) {
                tokenizer = new StringTokenizer(reader.readLine());
                table[temp] = tokenizer.nextToken();
                temp++;
            }

            PizzaSolver pizzaSolver =new PizzaSolver(table);
            if (!pizzaSolver.dfs(pizzaSolver.table, 0, writer)) writer.println("no");
            writer.println();
            T--;
        }
        reader.close(); writer.close();
    }

    private class PizzaSolver {
        int dimensions;
        int[] table;
        int[] topRestrictions;
        int[] bottomRestrictions;
        int[] leftRestrictions;
        int[] rightRestrictions;

        PizzaSolver(String[] table) {
            this.dimensions = table.length - 2;
            this.leftRestrictions = new int[table.length];
            this.rightRestrictions = new int[table.length];
            this.topRestrictions = new int[table.length];
            this.bottomRestrictions = new int[table.length];

            this.table = new int[dimensions*dimensions];
            //splits all input up
            int nZ = 0;
            for (int i = 0; i < this.dimensions + 2; i++) {
                String[] split = table[i].split("");

                for (int j = 0; j < this.dimensions + 2; j++) {
                    int val;
                    try {
                        val = Integer.parseInt(split[j]);
                    } catch (Exception e) {
                        val = 0;
                    }

                    if (i == 0) {
                        this.topRestrictions[j] = val;
                    } else if (i == this.dimensions + 1) {
                        this.bottomRestrictions[j] = val;
                    } else {
                        if (j == 0) {
                            this.leftRestrictions[i] = val;
                        } else if (j == this.dimensions + 1) {
                            this.rightRestrictions[i] = val;
                        } else {
                            this.table[nZ++] = val;
                        }
                    }
                }
            }
        }

        boolean dfs(int[] table, int z, PrintWriter writer) {
            while (z < dimensions*dimensions && table[z] != 0) z++;
            if (z == (this.dimensions * this.dimensions)) {printSol(table, writer); return true;}
            for (int k = 1; k <= this.dimensions; k++) {
                if (valid(table, z, k)) {
                    table[z] = k;
                    int nZ = z + 1;
                    while (nZ < (dimensions * dimensions) && table[nZ] > 0) nZ++;

                    boolean res = dfs(table, nZ, writer);

                    //table[z] = 0;
                    if (res) return true;
                    else table[z] = 0;
                } else {
                    table[z] = 0;
                }
            }
            return false;
        }

        boolean valid(int[] table, int z, int k) {
            table[z] = k;
            int j = 0;
            int[] nums = new int[dimensions]; Arrays.fill(nums,0);
            int[] valid = new int[dimensions]; Arrays.fill(valid, 1);

            //checks if goes 1 to dimension in each row
            for (int i = 0; i <= dimensions; i++) {
                if (i == dimensions) {
                    i = 0;
                    j += dimensions;
                    //if (!(Arrays.equals(valid,nums))) return false; // FOR TESTING IF COMPLETE
                    Arrays.fill(nums,0);
                }
                if (j >= dimensions * dimensions) break;
                if (table[i + j] != 0) {
                    if (nums[table[i + j] - 1] == 0) nums[table[i + j] - 1] = 1;
                    else return false;
                }
            }

            j = 0; Arrays.fill(nums,0);
            //checks if goes 1 to dimension in each column
            for (int i = 0; i < dimensions*dimensions + dimensions; i += dimensions) {
                if (i >= dimensions*dimensions) {
                    j++; if (j == dimensions) break;
                    i = j;
                    //if (!(Arrays.equals(valid,nums))) return false;
                    Arrays.fill(nums,0);
                }
                if (table[i] != 0) {
                    int temp = table[i];
                    if (nums[table[i] - 1] == 0) nums[table[i] - 1] = 1;
                    else return false;
                }
            }
            //if (!(Arrays.equals(valid,nums))) return false;

            for (int i = 0; i < dimensions; i++) { //left to right checking
                if (!(checkVisibility(leftRestrictions[i+1], rightRestrictions[i+1],
                        Arrays.copyOfRange(table,i*dimensions, i*dimensions + dimensions))))
                    return false;
                if (!(checkVisibility(topRestrictions[i+1], bottomRestrictions[i+1], getColumn(table,i, dimensions))))
                    return false;
            }

            return true;
        }

        int[] getColumn(int[] table, int column, int length){
            int[] res = new int[length];
            int j = 0;
            for (int i = column; i < length*length; i += length, j++) {
                res[j] = table[i];
            }
            return res;
        }

        boolean checkVisibility(int a, int b, int[]x) {
            int ac = 0, bc = 0, prev = 0;
            if (a != 0) {
                for (int i : x) {
                    if (i == 0) {ac = a; break;}
                    if (i > prev) {ac++;
                    //else break;
                    prev = i;}
                }
            }
            prev = 0;

            if (b != 0) {
                for (int i = x.length - 1; i >= 0; i--) {
                    if (x[i] == 0) {bc = b; break;} //if all values in row not entered dont bother checking
                    if (x[i] > prev) {bc++;
                    //else break;
                    prev = x[i];}
                }
            }
            return ((ac == a) & (bc == b)); // THIS ISN'T RIGHT, how to handle visibility?
        }

        void printSol(int[]table, PrintWriter writer) {
            int dim = (int) Math.sqrt(table.length);
            for (int i = 0; i < table.length; i+=dim) {
                writer.println(printAssist(Arrays.copyOfRange(table,i, i + dim)));
            }
        }

        int printAssist(int[]toprint) {
            int res = 0;
            for (int i = 0; i < toprint.length; i++) {
                res = res*10 + toprint[i];
            }
            return res;
        }
    }
}
