import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> points;

    public PointSET() {
        points = new TreeSet<>();
    }

    public boolean isEmpty() {
        return points.size() == 0;
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        TreeSet<Point2D> pointsInRange = new TreeSet<>();
        for (Point2D point : points) {
            boolean isPointInsideRectAbscissa = point.x() >= rect.xmin()
                    && point.x() <= rect.xmax();
            boolean isPointInsideRectOrdinate = point.y() >= rect.ymin()
                    && point.y() <= rect.ymax();

            if (isPointInsideRectAbscissa && isPointInsideRectOrdinate) pointsInRange.add(point);
        }
        return pointsInRange;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D queryPoint) {
        if (queryPoint == null) throw new IllegalArgumentException();
        Point2D champion = points.first();

        for (Point2D point : points) {
            if (point.distanceSquaredTo(queryPoint) < champion.distanceSquaredTo(queryPoint))
                champion = point;
        }

        return champion;
    }

    public static void main(String[] args) {

    }
}
