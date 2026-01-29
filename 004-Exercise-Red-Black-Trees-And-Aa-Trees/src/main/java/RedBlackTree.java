import java.util.ArrayDeque;
import java.util.Deque;

public class RedBlackTree<Key extends Comparable<Key>, Value> {

    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node root;     // root of the BST

    // BST helper node data type
    private class Node {
        private Key key;           // key
        private Value val;         // associated data
        private Node left, right;  // links to left and right subtrees
        private boolean color;     // color of parent link
        private int size;          // subtree count

        public Node(Key key, Value val, boolean color, int size) {
            this.key = key;
            this.val = val;
            this.color = color;
            this.size = size;
        }
    }

    public RedBlackTree() {
    }

    // is node x red; false if x is null ?
    private boolean isRed(Node node) {
        if (node == null) {
            return false;
        }
        ;

        return node.color;
    }

    // number of node in subtree rooted at x; 0 if x is null
    private int size(Node node) {
        if (node == null) {
            return 0;
        }

        return node.size;
    }


    /**
     * Returns the number of key-value pairs in this symbol table.
     *
     * @return the number of key-value pairs in this symbol table
     */
    public int size() {
        // TODO: Check if this implementation is correct
        return this.size(this.root);
    }

    /**
     * Is this symbol table empty?
     *
     * @return {@code true} if this symbol table is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return this.root == null;
    }

    public Value get(Key key) {
        Node current = this.root;

        while (current != null) {
            if (key.compareTo(current.key) < 0) {
                current = current.left;
            } else if (key.compareTo(current.key) > 0) {
                current = current.right;
            } else {
                return current.val;
            }
        }

        return null;
    }

    // NOT NEEDED
    // value associated with the given key in subtree rooted at x; null if no such key
    private Value get(Node x, Key key) {
        return null;
    }

    public boolean contains(Key key) {
        return this.get(key) != null;
    }

    public void put(Key key, Value value) {
        this.root = this.put(this.root, key, value);
        this.root.color = BLACK;
    }

    // insert the key-value pair in the subtree rooted at h
    private Node put(Node node, Key key, Value value) {
        if (node == null) {
            return new Node(key, value, RED, 1);
        }

        if (key.compareTo(node.key) < 0) {
            node.left = put(node.left, key, value);
        } else if (key.compareTo(node.key) > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.val = value;
        }

        // rotations
        if (this.isRed(node.right) && !this.isRed(node.left)) {
            node = this.rotateLeft(node);
        } else if (this.isRed(node.left) && this.isRed(node.left.left)) {
            node = this.rotateRight(node);
        } else if (this.isRed(node.left) && this.isRed(node.right)) {
            flipColors(node);
        }

        node.size = this.size(node.left) + this.size(node.right) + 1;

        return node;
    }

    // TODO: Create a test that breaks deleteMin;
    public void deleteMin() {
        if (this.root == null) {
            return;
        }

        this.root = this.deleteMin(this.root);

        if (this.root != null) {
            this.root.color = BLACK;
        }
    }

    // delete the key-value pair with the minimum key rooted at h
    private Node deleteMin(Node node) {
        if (this.isRed(node.right)) {
            this.moveRedLeft(node);
        }

        if (node.left == null) {
            return node.right;
        }

        node.left = this.deleteMin(node.left);

        return balance(node);
    }

    // TODO: Create a test that breaks deleteMax;
    public void deleteMax() {
        if (this.root == null) {
            return;
        }

        this.root = this.deleteMax(this.root);

        if (this.root != null) {
            this.root.color = BLACK;
        }
    }

    // delete the key-value pair with the maximum key rooted at h
    private Node deleteMax(Node node) {
        if (this.isRed(node.left)) {
            this.moveRedRight(node);
        }

        if (node.right == null) {
            return node.left;
        }

        node.right = this.deleteMax(node.right);

        return balance(node);
    }

    // TODO: Implement delete;
    public void delete(Key key) {
    }

    // delete the key-value pair with the given key rooted at h
    private Node delete(Node h, Key key) {
        return null;
    }

    private Node rotateRight(Node node) {
        Node temp = node.left;
        node.left = temp.right;
        temp.right = node;
        temp.color = node.color;
        temp.right.color = RED;
        temp.size = node.size;
        node.size = this.size(node.left) + this.size(node.right) + 1;
        return temp;
    }

    // make a right-leaning link lean to the left
    private Node rotateLeft(Node node) {
        Node temp = node.right;
        node.right = temp.left;
        temp.left = node;
        temp.color = node.color;
        temp.left.color = RED;
        temp.size = node.size;
        node.size = this.size(node.left) + this.size(node.right) + 1;
        return temp;
    }

    // flip the colors of a node and its two children
    private void flipColors(Node node) {
        node.color = !node.color;
        node.left.color = !node.left.color;
        node.right.color = !node.right.color;
    }

    // Assuming that h is red and both h.left and h.left.left
    // are black, make h.left or one of its children red.
    private void moveRedLeft(Node node) {
        flipColors(node);
        if (this.isRed(node)) {
            node.left = this.rotateLeft(node.left);
            flipColors(node);
            node.size = this.size(node.left) + this.size(node.right) + 1;
        }
    }

    // Assuming that h is red and both h.right and h.right.left
    // are black, make h.right or one of its children red.
    private void moveRedRight(Node node) {
        if (this.isRed(node)) {
            node.right = this.rotateRight(node.right);
            node = this.rotateLeft(node);
            flipColors(node);
            node.size = this.size(node.left) + this.size(node.right) + 1;
        }

    }

    // restore red-black tree invariant
    private Node balance(Node node) {
        if (this.isRed(node.right)) {
            node = this.rotateLeft(node);
        } else if (this.isRed(node.left) && this.isRed(node.left.left)) {
            node = this.rotateRight(node);
        } else if (this.isRed(node.left) && this.isRed(node.right)) {
            flipColors(node);
        }

        node.size = this.size(node.left) + this.size(node.right) + 1;
        return node;
    }

    public int height() {
        return this.height(this.root);
    }

    private int height(Node node) {
        if (node == null) {
            return -1;
        }

        return Math.max(this.height(node.left), this.height(node.right)) + 1;
    }

    public Key min() {
        if (this.root == null) {
            return null;
        }

        Node current = this.root;

        while (current.left != null) {
            current = current.left;
        }

        return current.key;
    }

    // the smallest key in subtree rooted at x; null if no such key
    private Node min(Node x) {
        return null;
    }

    public Key max() {
        if (this.root == null) {
            return null;
        }

        Node current = this.root;

        while (current.right != null) {
            current = current.right;
        }

        return current.key;
    }

    // the largest key in the subtree rooted at x; null if no such key
    private Node max(Node x) {
        return null;
    }

    public Key floor(Key key) {
        return this.floor(this.root, key).key;
    }

    // the largest key in the subtree rooted at x less than or equal to the given key
    private Node floor(Node node, Key key) {
        if (node == null) {
            return null;
        }

        if (key.compareTo(node.key) < 0) {
            return this.floor(node.left, key);
        } else if (key.compareTo(node.key) == 0) {
            return node;
        } else {
            Node result = max(node.right);

            return result == null ? node : result;
        }
    }

    public Key ceiling(Key key) {
        return this.ceiling(this.root, key).key;
    }

    // the smallest key in the subtree rooted at x greater than or equal to the given key
    private Node ceiling(Node node, Key key) {
        if (node == null) {
            return null;
        }

        if (key.compareTo(node.key) > 0) {
            return this.ceiling(node.right, key);
        } else if (key.compareTo(node.key) == 0) {
            return node;
        } else {
            Node result = min(node.left);

            return result == null ? node : result;
        }
    }

    public Key select(int rank) {
        return this.select(this.root, rank);
    }

    // Return key in BST rooted at x of given rank.
    // Precondition: rank is in legal range.
    private Key select(Node node, int rank) {
        if (node == null) {
            return null;
        }

        // TODO: Check if contains!!!

        if (this.size(node.left) < rank) {
            return this.select(node.right, rank - this.size(node.left) - 1);
        } else if (this.size(node.left) > rank) {
            return this.select(node.left, rank);
        } else {
            return node.key;
        }
    }

    public int rank(Key key) {
        return this.rank(this.root, key);
    }

    // number of keys less than key in the subtree rooted at x
    private int rank(Node node, Key key) {
        if (node == null) {
            return 0;
        }

        // TODO: Check if contains key

        if (key.compareTo(node.key) < 0) {
            return this.rank(node.left, key);
        } else if (key.compareTo(node.key) > 0) {
            return this.size(node.left) + this.rank(node.right, key) + 1;
        } else {
            return this.size(node.left);
        }
    }

    public Iterable<Key> keys() {
        if (this.root == null) {
            return null;
        }

        return this.keys(this.min(), this.max());
    }

    public Iterable<Key> keys(Key low, Key high) {
        ArrayDeque<Key> queue = new ArrayDeque<>();
        this.keys(this.root, queue, low, high);
        return queue;
    }

    // add the keys between lo and hi in the subtree rooted at x
    // to the queue
    private void keys(Node node, Deque<Key> queue, Key low, Key high) {
        if (node == null) {
            return;
        }

        if (low.compareTo(node.key) < 0) {
            keys(node.left, queue, low, high);
        }

        if (low.compareTo(node.key) <= 0 && high.compareTo(node.key) >= 0) {
            queue.add(node.key);
        }

        if (high.compareTo(node.key) > 0) {
            keys(node.right, queue, low, high);
        }
    }

    public int size(Key low, Key high) {
        return 0;
    }

    // USELESS METHODS :D

    private boolean check() {
        return false;
    }

    // does this binary tree satisfy symmetric order?
    // Note: this test also ensures that data structure is a binary tree since order is strict
    private boolean isBST() {
        return false;
    }

    // is the tree rooted at x a BST with all keys strictly between min and max
    // (if min or max is null, treat as empty constraint)
    private boolean isBST(Node x, Key min, Key max) {
        return false;
    }

    // are the size fields correct?
    private boolean isSizeConsistent() {
        return false;
    }

    private boolean isSizeConsistent(Node x) {
        return false;
    }

    // check that ranks are consistent
    private boolean isRankConsistent() {
        return false;
    }

    // Does the tree have no red right links, and at most one (left)
    // red links in a row on any path?
    private boolean isTwoThree() {
        return false;
    }

    private boolean isTwoThree(Node x) {
        return false;
    }

    // do all paths from root to leaf have same number of black edges?
    private boolean isBalanced() {
        return false;
    }

    // does every path from the root to a leaf have the given number of black links?
    private boolean isBalanced(Node x, int black) {
        return false;
    }
}