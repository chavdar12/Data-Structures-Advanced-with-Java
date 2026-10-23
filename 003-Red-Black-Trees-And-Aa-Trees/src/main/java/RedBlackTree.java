import java.util.function.Consumer;

public class RedBlackTree<T extends Comparable<T>> {
    private static final boolean RED = true;
    private static final boolean BLACK = false;

    private Node<T> root;

    public RedBlackTree() {
    }

    private RedBlackTree(Node<T> node) {
        this.preOrderCopy(node);
    }

    private void preOrderCopy(Node<T> node) {
        if (node == null) {
            return;
        }

        this.insert(node.value);
        this.preOrderCopy(node.left);
        this.preOrderCopy(node.right);
    }

    public int getNodesCount() {
        return this.getNodesCount(this.root);
    }

    private int getNodesCount(Node<T> node) {
        if (node == null) {
            return 0;
        }
        return node.count;
    }

    public void insert(T value) {
        this.root = this.insert(this.root, value);
        this.root.color = BLACK;
    }

    private Node<T> insert(Node<T> node, T value) {
        if (node == null) {
            return new Node<>(value);
        }

        int cmp = value.compareTo(node.value);
        if (cmp < 0) {
            node.left = this.insert(node.left, value);
        } else if (cmp > 0) {
            node.right = this.insert(node.right, value);
        }

        if (this.isRed(node.right) && !this.isRed(node.left)) {
            node = this.rotateLeft(node);
        }
        if (this.isRed(node.left) && this.isRed(node.left.left)) {
            node = this.rotateRight(node);
        }
        if (this.isRed(node.left) && this.isRed(node.right)) {
            this.flipColors(node);
        }

        node.count = this.getNodesCount(node.left) + this.getNodesCount(node.right) + 1;

        return node;
    }

    private boolean isRed(Node<T> node) {
        return node != null && node.color == RED;
    }

    private Node<T> rotateLeft(Node<T> node) {
        Node<T> temp = node.right;
        node.right = temp.left;
        temp.left = node;
        temp.color = node.color;
        node.color = RED;
        temp.count = node.count;
        node.count = this.getNodesCount(node.left) + this.getNodesCount(node.right) + 1;
        return temp;
    }

    private Node<T> rotateRight(Node<T> node) {
        Node<T> temp = node.left;
        node.left = temp.right;
        temp.right = node;
        temp.color = node.color;
        node.color = RED;
        temp.count = node.count;
        node.count = this.getNodesCount(node.left) + this.getNodesCount(node.right) + 1;
        return temp;
    }

    private void flipColors(Node<T> node) {
        node.color = RED;
        node.left.color = BLACK;
        node.right.color = BLACK;
    }

    public boolean contains(T value) {
        return this.findElement(value) != null;
    }

    public RedBlackTree<T> search(T item) {
        return new RedBlackTree<>(this.findElement(item));
    }

    private Node<T> findElement(T item) {
        Node<T> current = this.root;
        while (current != null) {
            if (item.compareTo(current.value) < 0) {
                current = current.left;
            } else if (item.compareTo(current.value) > 0) {
                current = current.right;
            } else {
                break;
            }
        }
        return current;
    }

    public void eachInOrder(Consumer<T> consumer) {
        this.eachInOrder(this.root, consumer);
    }

    private void eachInOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }

        this.eachInOrder(node.left, consumer);
        consumer.accept(node.value);
        this.eachInOrder(node.right, consumer);
    }

    public static class Node<T extends Comparable<T>> {
        private T value;
        private Node<T> left;
        private Node<T> right;
        private boolean color;
        private int count;

        private Node(T value) {
            this.value = value;
            this.color = RED;
            this.count = 1;
        }
    }
}

