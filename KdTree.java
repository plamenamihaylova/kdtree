import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {
    private Node root;

    private class Node {
        private Point2D point; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to current node
        private Node left; // left/bottom subtree
        private Node right; // right/top subtree
        private int n; // how many subtrees are in current node

        public Node(Point2D point, RectHV rect, int n) {
            this.point = point;
            this.rect = rect;
            this.n = n;
        }
    }

    public KdTree() {
        // root = null;
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
        // RectHV rectToInsert = root.rect != null ? root.rect : new RectHV(0, 0, 1, 1);
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
        return false; // get(key) != null;
    }

    public void draw() {

    }

    public Iterable<Point2D> range(RectHV rect) {
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
        return nearest(root, queryPoint, root.point, 0);
    }

    private Point2D nearest(Node node, Point2D queryPoint, Point2D champion, int depth) {
        if (node == null) return champion;

        if (node.point.distanceSquaredTo(queryPoint) < champion.distanceSquaredTo(queryPoint))
            champion = node.point;

        int nextDepth = (depth + 1) % 2;
        // split horizontally, compare by x
        if (nextDepth != 0) {
            int cmp = Double.compare(queryPoint.x(), node.point.x());
            if (cmp < 0) champion = nearest(node.left, queryPoint, champion, nextDepth);
            else champion = nearest(node.right, queryPoint, champion, nextDepth);
        }
        // split vertically, compare by y
        else {
            int cmp = Double.compare(queryPoint.y(), node.point.y());
            if (cmp < 0) champion = nearest(node, queryPoint, champion, nextDepth);
            else champion = nearest(node.right, queryPoint, champion, nextDepth);
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
        System.out.println(kdtree.size());
    }
}
