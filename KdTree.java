import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

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
public class KdTree {
    private Node root;

    private class Node {
        private Point2D point;
        private RectHV rect; // the axis-aligned rectangle corresponding to current node
        private Node left;
        private Node right;
        private int n; // how many subtrees are in current node

        public Node(Point2D point, RectHV rect, int n) {
            this.point = point;
            this.rect = rect;
            this.n = n;
        }
    }

    public KdTree() {
    }

    /**
     * Return true if the tree is empty, and false if it's not empty
     *
     * @return {@code true} if tree is empty, {@code false} otherwise
     */
    public boolean isEmpty() {
        return root == null;
    }

    /**
     * Return the number of points in the tree.
     *
     * @return number of points in the tree
     */
    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) return 0;
        return node.n;
    }

    /**
     * Add a point to the tree.
     *
     * @param p point to insert into the tree
     */
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (contains(p)) return;
        root = insert(root, p, root != null ? root.rect : new RectHV(0, 0, 1, 1), 0);
    }

    private Node insert(Node node, Point2D pointToInsert, RectHV rect, int depth) {
        if (node == null) return new Node(pointToInsert, rect, 1);

        int nextDepth = (depth + 1) % 2;


        // split horizontally, compare by x
        if (nextDepth != 0) {
            int cmp = Double.compare(pointToInsert.x(), node.point.x());
            if (cmp < 0) {
                node.left = insert(node.left,
                                   pointToInsert,
                                   constructHorizontalRectangle(
                                           node.left, rect, rect.xmin(), node.point.x()),
                                   nextDepth);
            }
            else {
                node.right = insert(node.right,
                                    pointToInsert,
                                    constructHorizontalRectangle(
                                            node.right, rect, node.point.x(), rect.xmax()),
                                    nextDepth);
            }
        }
        // split vertically, compare by y
        else {
            int cmp = Double.compare(pointToInsert.y(), node.point.y());
            if (cmp < 0) {
                node.left = insert(node.left,
                                   pointToInsert,
                                   constructVerticalRectangle(
                                           node.left, rect, rect.ymin(), node.point.y()),
                                   nextDepth);
            }
            else {
                node.right = insert(node.right,
                                    pointToInsert,
                                    constructVerticalRectangle(
                                            node.right, rect, node.point.y(), rect.ymax()),
                                    nextDepth);
            }
        }
        node.n = size(node.left) + size(node.right) + 1;
        return node;
    }

    private RectHV constructHorizontalRectangle(Node node, RectHV rect, double xmin, double xmax) {
        if (node == null) return new RectHV(xmin, rect.ymin(), xmax, rect.ymax());
        return node.rect;
    }

    private RectHV constructVerticalRectangle(Node node, RectHV rect, double ymin, double ymax) {
        if (node == null) return new RectHV(rect.xmin(), ymin, rect.xmax(), ymax);
        return node.rect;
    }

    /**
     * Return true if the tree contains the search point, false if the point is not in the tree.
     *
     * @param searchPoint point to check if it's in the set
     * @return {@code true} if search point can be found in the set, {@code false} otherwise
     */
    public boolean contains(Point2D searchPoint) {
        if (searchPoint == null) throw new IllegalArgumentException();
        return contains(root, searchPoint, 0);
    }

    private boolean contains(Node node, Point2D searchPoint, int depth) {
        if (node == null) return false;
        boolean arePointsEqual = node.point.equals(searchPoint);

        if (!arePointsEqual) {
            int nextDepth = (depth + 1) % 2;
            if (nextDepth != 0) {
                int cmp = Double.compare(searchPoint.x(), node.point.x());
                return contains(cmp, node, searchPoint, nextDepth);
            }
            int cmp = Double.compare(searchPoint.y(), node.point.y());
            return contains(cmp, node, searchPoint, nextDepth);
        }
        return true;
    }

    private boolean contains(int cmp, Node node, Point2D searchPoint, int depth) {
        if (cmp < 0) return contains(node.left, searchPoint, depth);
        else return contains(node.right, searchPoint, depth);
    }

    /**
     * Draw all points to standard draw.
     * Haven't implemented it.
     */
    public void draw() {
    }

    /**
     * Return all points that are inside the rectangle (or on the boundary).
     * Return null if no points are inside the rectangle or if the tree is empty.
     *
     * @param rect rectangle to which the tree's points will be checked
     * @return iterable range of points that are inside the range of the rectangle,
     * null if the tree is empty or no points are inside the rectangle
     */
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        return traverse(root, rect, new Queue<>());
    }

    private Iterable<Point2D> traverse(Node node, RectHV rect, Queue<Point2D> queue) {
        if (node == null) return queue;
        if (node.rect.intersects(rect)) {
            if (rect.contains(node.point)) queue.enqueue(node.point);
            traverse(node.left, rect, queue);
            traverse(node.right, rect, queue);
        }
        return queue;
    }

    public Point2D nearest(Point2D queryPoint) {
        if (queryPoint == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        return nearest(root, queryPoint, root.point, 0);
    }

    /**
     * Return the nearest neighbor in the tree to the query point; null if the tree is empty.
     *
     * @param queryPoint point whose nearest neighbor from the tree will be searched for
     * @return nearest query point's neighbor from tree
     */
    private Point2D nearest(Node node, Point2D queryPoint, Point2D champion, int depth) {
        if (node == null) return champion;

        if (node.point.distanceSquaredTo(queryPoint) <= champion.distanceSquaredTo(queryPoint))
            champion = node.point;
        if (!(node.rect.distanceSquaredTo(queryPoint) < champion.distanceSquaredTo(queryPoint)))
            return champion;

        int nextDepth = (depth + 1) % 2;
        // split horizontally, compare by x
        if (nextDepth != 0) {
            int cmp = Double.compare(queryPoint.x(), node.point.x());
            champion = nearest(cmp, node, queryPoint, champion, nextDepth);
        }
        // split vertically, compare by y
        else {
            int cmp = Double.compare(queryPoint.y(), node.point.y());
            champion = nearest(cmp, node, queryPoint, champion, nextDepth);
        }

        return champion;
    }

    private Point2D nearest(int cmp, Node node, Point2D queryPoint, Point2D champion, int depth) {
        if (cmp < 0) {
            champion = nearest(node.left, queryPoint, champion, depth);
            champion = nearest(node.right, queryPoint, champion, depth);
        }
        else {
            champion = nearest(node.right, queryPoint, champion, depth);
            champion = nearest(node.left, queryPoint, champion, depth);
        }
        return champion;
    }

    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        System.out.println(kdtree.nearest(new Point2D(0.95, 0.51)).toString());
        System.out.println(kdtree.size());
    }
}
