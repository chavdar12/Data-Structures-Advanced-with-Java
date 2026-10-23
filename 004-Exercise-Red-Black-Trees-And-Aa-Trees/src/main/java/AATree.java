import java.util.function.Consumer;

class AATree<T extends Comparable<T>> {
    public static class Node<T> {
        private T element;
        private Node<T> left;
        private Node<T> right;
        private int level;
        private int count;

        public Node(T element) {
            this.element = element;
            this.level = 1;
            this.count = 1;
        }
    }

    private Node<T> root;

    public AATree() {

    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public void clear() {
        this.root = null;
    }

    public void insert(T element) {
        this.root = this.insert(this.root, element);
    }

    private Node<T> insert(Node<T> node, T element) {
        if (node == null) {
            return new Node<>(element);
        }

        int cmp = element.compareTo(node.element);
        if (cmp < 0) {
            node.left = this.insert(node.left, element);
        } else if (cmp > 0) {
            node.right = this.insert(node.right, element);
        }

        node = this.skew(node);
        node = this.split(node);
        node.count = this.count(node.left) + this.count(node.right) + 1;

        return node;
    }

    private Node<T> skew(Node<T> node) {
        if (node.left != null && node.left.level == node.level) {
            Node<T> left = node.left;
            node.left = left.right;
            left.right = node;
            node.count = this.count(node.left) + this.count(node.right) + 1;
            return left;
        }

        return node;
    }

    private Node<T> split(Node<T> node) {
        if (node.right != null && node.right.right != null && node.right.right.level == node.level) {
            Node<T> right = node.right;
            node.right = right.left;
            right.left = node;
            right.level++;
            node.count = this.count(node.left) + this.count(node.right) + 1;
            return right;
        }

        return node;
    }

    private int count(Node<T> node) {
        return node == null ? 0 : node.count;
    }

    public int countNodes() {
        return this.count(this.root);
    }

    public boolean search(T element) {
        Node<T> current = this.root;
        while (current != null) {
            int cmp = element.compareTo(current.element);
            if (cmp < 0) {
                current = current.left;
            } else if (cmp > 0) {
                current = current.right;
            } else {
                return true;
            }
        }

        return false;
    }

    public void inOrder(Consumer<T> consumer) {
        this.inOrder(this.root, consumer);
    }

    private void inOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }

        this.inOrder(node.left, consumer);
        consumer.accept(node.element);
        this.inOrder(node.right, consumer);
    }

    public void preOrder(Consumer<T> consumer) {
        this.preOrder(this.root, consumer);
    }

    private void preOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }

        consumer.accept(node.element);
        this.preOrder(node.left, consumer);
        this.preOrder(node.right, consumer);
    }

    public void postOrder(Consumer<T> consumer) {
        this.postOrder(this.root, consumer);
    }

    private void postOrder(Node<T> node, Consumer<T> consumer) {
        if (node == null) {
            return;
        }

        this.postOrder(node.left, consumer);
        this.postOrder(node.right, consumer);
        consumer.accept(node.element);
    }
}
