package Assignment5;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.LinkedList;
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
            tokenizer = new StringTokenizer(reader.readLine());
            int N = Integer.parseInt(tokenizer.nextToken());
            Point position = new Point(0, 0, 0);

            LinkedList<Segment> segments = new LinkedList<>();
            int a, d;
            while (N > 0) {
                tokenizer = new StringTokenizer(reader.readLine());
                a = Integer.parseInt(tokenizer.nextToken());
                d = Integer.parseInt(tokenizer.nextToken());
                Segment newSeg = getLineSegment(position, a, d);

                if (a == 0 && segments.size() > 0) {
                    Segment colin = segments.pollLast();
                    segments.add(new Segment(colin.a, newSeg.b));
                } else {
                    segments.add(newSeg);
                }

                position = newSeg.b;
                N--;
            }

            int whenFall = whenFall(segments);
            if (whenFall == -1) printWriter.println("SAFE");
            else printWriter.println(whenFall);

            T--;
        }
        printWriter.close();
        reader.close();
    }

    class Segment {
        Point a, b;

        Segment(Point a, Point b) {
            this.a = a;
            this.b = b;
        }
    }

    class Point {
        double x, y;
        int offset;

        Point(double x, double y, int offset) {
            this.x = x;
            this.y = y;
            this.offset = offset;
        }
    }

    private boolean ccw(Point A, Point B, Point C) {
        return(C.y-A.y)*(B.x-A.x)>(B.y-A.y)*(C.x-A.x);
    }

    private boolean intersect(Point A,Point B, Point C,Point D) {
        return ccw(A, C, D) != ccw(B, C, D) && ccw(A, B, C) != ccw(A, B, D);
    }

    private boolean get_line_intersection(double p0_x, double p0_y, double p1_x, double p1_y,
                               double p2_x, double p2_y, double p3_x, double p3_y)
    {
        double s1_x, s1_y, s2_x, s2_y;
        s1_x = p1_x - p0_x;     s1_y = p1_y - p0_y;
        s2_x = p3_x - p2_x;     s2_y = p3_y - p2_y;

        double s, t;
        s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / (-s2_x * s1_y + s1_x * s2_y);
        t = ( s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / (-s2_x * s1_y + s1_x * s2_y);

        return (s >= 0 && s <= 1 && t >= 0 && t <= 1);
    }

    private boolean seeIfFalls(Segment a, LinkedList<Segment> segments) {
        int i = 0;
        for (Segment segment : segments) {
            if (i < segments.size() - 1)
                //if (intersect(a.a, a.b, segment.a, segment.b))
                if (get_line_intersection(a.a.x, a.a.y, a.b.x, a.b.y,
                        segment.a.x, segment.a.y, segment.b.x, segment.b.y))
                    return true;
            i++;
        }
        return false;
    }

    private int whenFall(LinkedList<Segment> segments) {
        LinkedList<Segment> oldsegs = new LinkedList<>();
        if (segments.size() > 0) oldsegs.add(segments.poll());
        if (segments.size() > 0) oldsegs.add(segments.poll());

        int i = segments.size();
        for (int j = 3; j < (i+3); j++) {
            Segment current = segments.poll();
            if (seeIfFalls(current, oldsegs))
                return j;
            oldsegs.add(current);
        }

        return -1;
    }

    private Segment getLineSegment(Point current, int a, int d) {
        if (a < 0) a = (-180 - a)*-1 + 180;

        int offset = current.offset;
        double x = current.x + d * Math.sin(Math.toRadians(a + offset));
        double y = current.y + d * Math.cos(Math.toRadians(a + offset));

        return new Segment(current, new Point(x, y, a + offset));
    }

}
