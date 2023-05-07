import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

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

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size(root);
    }

    private int size(Node node) {
        if (node == null) return 0;
        return node.n;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (contains(p)) return;
        root = insert(root, p, root != null ? root.rect : new RectHV(0, 0, 1, 1), 0);
    }

    private Node insert(Node node, Point2D pointToInsert, RectHV rect, int depth) {
        if (node == null)
            return new Node(pointToInsert, rect, 1);

        int nextDepth = (depth + 1) % 2;

        // split horizontally, compare by x
        if (nextDepth != 0) {
            int cmp = Double.compare(pointToInsert.x(), node.point.x());

            if (cmp < 0) {
                RectHV leftRect = new RectHV(node.rect.xmin(), node.rect.ymin(),
                                             node.point.x(), node.rect.ymax());
                node.left = insert(node.left, pointToInsert, leftRect, nextDepth);
            }
            else { // if (cmp > 0) {
                RectHV rightRect = new RectHV(node.point.x(), node.rect.ymin(),
                                              node.rect.xmax(), node.rect.ymax());
                node.right = insert(node.right, pointToInsert, rightRect, nextDepth);
            }

            // else node.point = pointToInsert;

        }
        // split vertically, compare by y
        else {
            int cmp = Double.compare(pointToInsert.y(), node.point.y());
            if (cmp < 0) {
                RectHV bottomRect = new RectHV(node.rect.xmin(), node.rect.ymin(),
                                               node.rect.xmax(), node.point.y());
                node.left = insert(node.left, pointToInsert, bottomRect, nextDepth);
            }

            else { // if (cmp > 0) {
                RectHV topRect = new RectHV(node.rect.xmin(), node.point.y(),
                                            node.rect.xmax(), node.rect.ymax());
                node.right = insert(node.right, pointToInsert, topRect, nextDepth);
            }
            // else node.point = pointToInsert;
        }

        node.n = size(node.left) + size(node.right) + 1;
        return node;
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return contains(root, p, 0);
    }

    private boolean contains(Node node, Point2D p, int depth) {
        if (node == null) return false;

        int cmpPoints = node.point.compareTo(p);

        if (cmpPoints != 0) {
            int nextDepth = (depth + 1) % 2;
            if (nextDepth != 0) {
                int cmp = Double.compare(p.x(), node.point.x());
                if (cmp < 0) return contains(node.left, p, nextDepth);
                else return contains(node.right, p, nextDepth);
            }
            int cmp = Double.compare(p.y(), node.point.y());
            if (cmp < 0) return contains(node.left, p, nextDepth);
            else return contains(node.right, p, nextDepth);
        }
        return true;
    }

    public void draw() {

    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        return traverse(root, rect, new Queue<>());
    }

    private Iterable<Point2D> traverse(Node node, RectHV rect, Queue<Point2D> queue) {
        if (node == null) return queue;
        if (node.rect.intersects(rect)) {
            if (rect.contains(node.point)) queue.enqueue(node.point);
            // if (node.left != null)
            traverse(node.left, rect, queue);
            // if (node.right != null)
            traverse(node.right, rect, queue);
        }
        return queue;
    }

    public Point2D nearest(Point2D queryPoint) {
        if (queryPoint == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;
        return nearest(root, queryPoint, root.point, 0);
    }

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
            if (cmp < 0) {
                champion = nearest(node.left, queryPoint, champion, nextDepth);
                // if (champion.distanceSquaredTo(queryPoint) > node.right.point.distanceSquaredTo(
                //         queryPoint))
                champion = nearest(node.right, queryPoint, champion, nextDepth);
            }
            else {
                champion = nearest(node.right, queryPoint, champion, nextDepth);
                // if (champion.distanceSquaredTo(queryPoint) > node.left.point.distanceSquaredTo(
                //         queryPoint))
                champion = nearest(node.left, queryPoint, champion, nextDepth);
            }
        }
        // split vertically, compare by y
        else {
            int cmp = Double.compare(queryPoint.y(), node.point.y());
            if (cmp < 0) {
                champion = nearest(node.left, queryPoint, champion, nextDepth);
                // if (champion.distanceSquaredTo(queryPoint) > node.right.point.distanceSquaredTo(
                //         queryPoint))
                champion = nearest(node.right, queryPoint, champion, nextDepth);
            }
            else {
                champion = nearest(node.right, queryPoint, champion, nextDepth);
                // if (champion.distanceSquaredTo(queryPoint) > node.left.point.distanceSquaredTo(
                //         queryPoint))
                champion = nearest(node.left, queryPoint, champion, nextDepth);
            }
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
