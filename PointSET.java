import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

/**
 * ASSESSMENT SUMMARY
 * <p>
 * Compilation:  PASSED
 * API:          PASSED
 * <p>
 * SpotBugs:     PASSED
 * PMD:          PASSED
 * Checkstyle:   PASSED
 * <p>
 * Correctness:  35/35 tests passed
 * Memory:       16/16 tests passed
 * Timing:       42/42 tests passed
 */
public class PointSET {
    private TreeSet<Point2D> points;

    /**
     * Construct an empty set of points.
     */
    public PointSET() {
        points = new TreeSet<>();
    }

    /**
     * Return true if the set of points is empty, and false if it's not empty
     *
     * @return {@code true} if set is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return points.isEmpty();
    }

    /**
     * Return the number of points in the set.
     *
     * @return number of points in the set
     */
    public int size() {
        return points.size();
    }

    /**
     * Add a point to the set (if it is not already in the set).
     *
     * @param p point to insert into set
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);
    }

    /**
     * Return true if the set contains the point, false if the point is not in the set.
     *
     * @param p point to check if it's in the set
     * @return {@code true} if the point can be found in the set, {@code false} otherwise
     */
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return false;
        return points.contains(p);
    }

    /**
     * Draw all points to standard draw.
     */
    public void draw() {
        for (Point2D point : points) point.draw();
    }

    /**
     * Return all points that are inside the rectangle (or on the boundary).
     * Return null if no points are inside the rectangle or if the set is empty.
     *
     * @param rect rectangle to which the set points will be checked
     * @return iterable range of points that are inside the range of the rectangle,
     * null if the set is empty or no points are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        TreeSet<Point2D> pointsInRange = new TreeSet<>();
        for (Point2D point : points) {
            boolean isPointInsideRectAbscissa = point.x() >= rect.xmin()
                    && point.x() <= rect.xmax();
            boolean isPointInsideRectOrdinate = point.y() >= rect.ymin()
                    && point.y() <= rect.ymax();
            if (isPointInsideRectAbscissa && isPointInsideRectOrdinate)
                pointsInRange.add(point);
        }
        return pointsInRange;
    }

    /**
     * Return the nearest neighbor in the set to the query point; null if the set is empty.
     *
     * @param queryPoint point whose nearest neighbor from the set will be searched for
     * @return nearest query point's neighbor from set
     */
    public Point2D nearest(Point2D queryPoint) {
        if (queryPoint == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        Point2D champion = points.first();
        for (Point2D point : points)
            if (point.distanceSquaredTo(queryPoint) < champion.distanceSquaredTo(queryPoint))
                champion = point;
        return champion;
    }

    public static void main(String[] args) {

    }
}
