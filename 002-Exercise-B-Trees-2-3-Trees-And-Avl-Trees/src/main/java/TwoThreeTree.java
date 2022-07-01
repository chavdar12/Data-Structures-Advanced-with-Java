public class TwoThreeTree<K extends Comparable<K>> {
    private TreeNode<K> root;

    private static class TreeNode<K> {
        private K leftKey;
        private K rightKey;

        private TreeNode<K> leftChild;
        private TreeNode<K> middleChild;
        private TreeNode<K> rightChild;

        private TreeNode(K key) {
            this.leftKey = key;
        }

        public TreeNode(K left, K middle, K right) {
            this.leftKey = middle;
            this.leftChild = new TreeNode<>(left);
            this.rightChild = new TreeNode<>(right);
        }

        boolean isThreeNode() {
            return rightKey != null;
        }

        boolean isTwoNode() {
            return rightKey == null;
        }

        boolean isLeaf() {
            return this.leftChild == null && this.middleChild == null && this.rightChild == null;
        }
    }

    private TreeNode<K> internalInsert(TreeNode<K> currentNode, K value) {
        if (currentNode.isLeaf()) {
            if (currentNode.isTwoNode()) {
                if (value.compareTo(currentNode.leftKey) < 0) {
                    currentNode.rightKey = currentNode.leftKey;
                    currentNode.leftKey = value;
                } else {
                    currentNode.rightKey = value;
                }
            } else {

                if (value.compareTo(currentNode.leftKey) < 0) {
                    return new TreeNode<>(value, currentNode.leftKey, currentNode.rightKey);
                } else if (value.compareTo(currentNode.rightKey) > 0) {
                    return new TreeNode<>(currentNode.leftKey, currentNode.rightKey, value);
                } else {
                    return new TreeNode<>(currentNode.leftKey, value, currentNode.rightKey);
                }
            }
        } else {
            TreeNode<K> restructuredNode;

            if (currentNode.isTwoNode() && value.compareTo(currentNode.leftKey) < 0) {
                restructuredNode = this.internalInsert(currentNode.leftChild, value);
            } else if (currentNode.isTwoNode() && value.compareTo(currentNode.leftKey) > 0) {
                restructuredNode = this.internalInsert(currentNode.rightChild, value);
            } else if (currentNode.isThreeNode() && value.compareTo(currentNode.leftKey) < 0) {
                restructuredNode = this.internalInsert(currentNode.leftChild, value);
            } else if (currentNode.isThreeNode() && value.compareTo(currentNode.rightKey) > 0) {
                restructuredNode = this.internalInsert(currentNode.rightChild, value);
            } else {
                restructuredNode = this.internalInsert(currentNode.middleChild, value);
            }

            if (restructuredNode != null) {
                if (currentNode.isTwoNode()) {
                    if (restructuredNode.leftKey.compareTo(currentNode.leftKey) < 0) {
                        currentNode.rightKey = currentNode.leftKey;
                        currentNode.leftKey = restructuredNode.leftKey;

                        currentNode.leftChild = restructuredNode.leftChild;
                        currentNode.middleChild = restructuredNode.rightChild;
                    } else {
                        currentNode.rightKey = restructuredNode.leftKey;

                        currentNode.middleChild = restructuredNode.leftChild;
                        currentNode.rightChild = restructuredNode.rightChild;
                    }
                } else {
                    TreeNode<K> newNode;

                    if (restructuredNode.leftKey.compareTo(currentNode.leftKey) < 0) {
                        newNode = new TreeNode<>(currentNode.leftKey);

                        newNode.leftChild = restructuredNode;
                        newNode.rightChild = new TreeNode<>(currentNode.rightKey);

                        newNode.rightChild.leftChild = currentNode.middleChild;
                        newNode.rightChild.rightChild = currentNode.rightChild;
                    } else if (restructuredNode.leftKey.compareTo(currentNode.rightKey) > 0) {
                        newNode = new TreeNode<>(currentNode.rightKey);

                        newNode.leftChild = new TreeNode<>(currentNode.leftKey);
                        newNode.rightChild = restructuredNode;

                        newNode.leftChild.leftChild = currentNode.leftChild;
                        newNode.leftChild.rightChild = currentNode.middleChild;
                    } else {
                        newNode = new TreeNode<>(restructuredNode.leftKey);

                        newNode.leftChild = new TreeNode<>(currentNode.leftKey);
                        newNode.rightChild = new TreeNode<>(currentNode.rightKey);

                        newNode.leftChild.leftChild = currentNode.leftChild;
                        newNode.leftChild.rightChild = restructuredNode.leftChild;

                        newNode.rightChild.rightChild = currentNode.rightChild;
                        newNode.rightChild.leftChild = restructuredNode.rightChild;
                    }

                    return newNode;
                }
            }
        }

        return null;
    }

    public void insert(K key) {
        if (this.root == null) {
            this.root = new TreeNode<>(key);
        } else {
            TreeNode<K> node = this.internalInsert(this.root, key);

            if (node != null) {
                this.root = node;
            }
        }
    }

    public String getAsString() {
        StringBuilder out = new StringBuilder();
        recursivePrint(this.root, out);
        return out.toString().trim();
    }

    private void recursivePrint(TreeNode<K> node, StringBuilder out) {
        if (node == null) {
            return;
        }
        if (node.leftKey != null) {
            out.append(node.leftKey)
                    .append(" ");
        }
        if (node.rightKey != null) {
            out.append(node.rightKey).append(System.lineSeparator());
        } else {
            out.append(System.lineSeparator());
        }
        if (node.isTwoNode()) {
            recursivePrint(node.leftChild, out);
            recursivePrint(node.rightChild, out);
        } else if (node.isThreeNode()) {
            recursivePrint(node.leftChild, out);
            recursivePrint(node.middleChild, out);
            recursivePrint(node.rightChild, out);
        }
    }
}
