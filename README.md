<img height="400" src="logo.png" title="8 puzzle logo" width="1000"/>

### KD-Trees

**Algorithms Part 1 | Coursera course | Week 5
Assignment | [my coursera profile](https://www.coursera.org/user/045cf702be8b31ef1aa039e2b4f07db6)**

---
<!-- TOC -->

* [KD-Trees](#kd-trees)
* [Task specification](#task-specification)
    * [PointSET API](#pointset-api)
    * [KdTree API](#kdtree-api)
* [Useful resources that have helped me in completing this assignment:](#useful-resources-that-have-helped-me-in-completing-this-assignment)

<!-- TOC -->

---

### Task specification

🔗Detailed specifications for the assignment can be
found [here](https://coursera.cs.princeton.edu/algs4/assignments/kdtree/specification.php).

The task is to write a data type to represent a set of points in the unit square using a 2d-tree to support efficient
range search and nearest-neighbor search.
By unit square we will understand such a square that all its points have x- and y-coordinates between 0 and 1.
By range search we will understand finding all the points contained in a query rectangle.
By nearest neighbor search we will understand finding the closest point to a query point.

The task is to implement two approaches to this problem: **brute-force** implementation and **2d-tree** implementation.

For the brute-force implementaion can be used `java.util.TreeSet`.

For the 2d-tree implementation mutable data type `KdTree.java` that uses a 2d-tree should be implemented.

A 2d-tree is a generalization of a BST to two-dimensional keys.
The idea is to build a BST with points in the nodes, using the x- and y-coordinates of the points as keys in strictly
alternating sequence.

---

#### PointSET API

```
public class PointSET {
   public PointSET()                            // construct an empty set of points 
   public boolean isEmpty()                     // is the set empty? 
   public int size()                            // number of points in the set 
   public void insert(Point2D p)                // add the point to the set (if it is not already in the set)
   public boolean contains(Point2D p)           // does the set contain point p? 
   public void draw()                           // draw all points to standard draw 
   public Iterable<Point2D> range(RectHV rect)  // all points that are inside the rectangle (or on the boundary) 
   public Point2D nearest(Point2D p)            // a nearest neighbor in the set to point p; null if the set is empty 
   public static void main(String[] args)       // unit testing of the methods (optional) 
}
```

---

#### KdTree API

```
public class KdTree {
   public PointSET()                            // construct an empty set of points 
   public boolean isEmpty()                     // is the kd-tree empty
   public int size()                            // number of points in the kd-tree
   public void insert(Point2D p)                // add the point to the kd-tree
   public boolean contains(Point2D p)           // does the tree contain point p? 
   public void draw()                           // draw all points to standard draw 
   public Iterable<Point2D> range(RectHV rect)  // all points that are inside the rectangle (or on the boundary) 
   public Point2D nearest(Point2D p)            // a nearest neighbor in the tree to point p; null if the tree is empty 
   public static void main(String[] args)       // unit testing of the methods (optional) 
}
```

### Useful resources that have helped me in completing this assignment:

🔗[Prinston's 8puzzle assignment specification](https://www.cs.princeton.edu/courses/archive/spr14/cos226/checklist/kdtree.html)

🔗[Prinston's 8puzzle assignment worksheet](https://www.cs.princeton.edu/courses/archive/spr14/cos226/checklist/kdtree-worksheet.pdf)

🔗[Prinstion's video explonation of the assignment pt.1](https://www.youtube.com/watch?v=iRfNvfJszIE&list=LL&index=4)

🔗[Prinstion's video explonation of the assignment pt.2](https://www.youtube.com/watch?v=c_KIuD_mvEU&list=LL&index=3&t=943s)

🔗[Prinstion's video explonation of the assignment pt.3](https://www.youtube.com/watch?v=bkG6ECT0W3o&list=LL&index=2)

---