import org.junit.Assert;
import org.junit.Test;

public class Tree23Test {

    @Test
    public void add_SingleKey_ShouldBeRoot() {
        Tree23.Tree_23<Integer> tree = new Tree23.Tree_23<>();
        tree.add(5);

        Assert.assertEquals("{5}\n", tree.toStringPreOrder());
    }

    @Test
    public void add_ThreeKeys_ShouldSplitRoot() {
        Tree23.Tree_23<Integer> tree = new Tree23.Tree_23<>();
        tree.add(1);
        tree.add(2);
        tree.add(3);

        Assert.assertEquals("{2}\n    {1}\n    {3}\n", tree.toStringPreOrder());
    }

    @Test
    public void add_SplitPropagatesMedianToParent() {
        Tree23.Tree_23<Integer> tree = new Tree23.Tree_23<>();
        for (int i = 1; i <= 5; i++) {
            tree.add(i);
        }

        // inserting 5 splits {3,4,5}: 4 (the median) moves up, not 5
        Assert.assertEquals("{2,4}\n    {1}\n    {3}\n    {5}\n", tree.toStringPreOrder());
    }

    @Test
    public void add_SevenKeys_ShouldGrowToHeightThree() {
        Tree23.Tree_23<Integer> tree = new Tree23.Tree_23<>();
        for (int i = 1; i <= 7; i++) {
            tree.add(i);
        }

        Assert.assertEquals(
                "{4}\n    {2}\n        {1}\n        {3}\n    {6}\n        {5}\n        {7}\n",
                tree.toStringPreOrder());
    }
}
