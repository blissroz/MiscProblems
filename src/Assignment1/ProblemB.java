package Assignment1;

import java.io.PrintWriter;
import java.util.*;

public class ProblemB {
    public static void main(String[] args) throws Exception {
        ProblemB problem = new ProblemB();
    }

    private void problem_b() {
        String line;
        String[] vals;
        Scanner scanner = new Scanner(System.in);
        int T = Integer.parseInt(scanner.nextLine());
        PrintWriter printWriter = new PrintWriter(System.out);

        while (T > 0) {
            line = scanner.nextLine();
            int n, m, i,j,x,y;
            vals = line.split(" ");

            n = Integer.parseInt(vals[0]);
            m = Integer.parseInt(vals[1]);
            i = 0; j=0;
            Set<Integer> vertices = new HashSet<>();

            LinkedList<Integer>[] adjacency = new LinkedList[n];
            while (j < n) {
                adjacency[j] = new LinkedList<Integer>();
            }

            while (i < m) {
                line = scanner.nextLine();
                vals = line.split(" ");
                x=Integer.parseInt(vals[0]);
                y=Integer.parseInt(vals[1]);

                adjacency[x].add(y);
                adjacency[y].add(x);
                if (!vertices.contains(x)) vertices.add(x);
                if (!vertices.contains(y)) vertices.add(y);

                i++;
            }
            Boolean visited[] = new Boolean[n];
            List<Integer> q = new LinkedList<Integer>();

            T--;
        }

    }
    private long getPermutations(int a) {
        return factorial(a)/factorial(a-1);
    }
    private long factorial(int n) {
        long result = 1;
        for (int f = 2; f <= n; f++) result *= f;
        return result;
    }
}
