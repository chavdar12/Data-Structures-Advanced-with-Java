import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class RoyalArenaTest {
    private IArena arena;

    @Before
    public void setUp() {
        this.arena = new RoyalArena();
        this.arena.add(new Battlecard(1, CardType.MELEE, "Knight", 10, 5));
        this.arena.add(new Battlecard(2, CardType.MELEE, "Knight", 20, 15));
        this.arena.add(new Battlecard(3, CardType.SPELL, "Fireball", 30, 1));
        this.arena.add(new Battlecard(4, CardType.MELEE, "Giant", 20, 8));
    }

    private static List<Integer> ids(Iterable<Battlecard> cards) {
        List<Integer> ids = new ArrayList<>();
        cards.forEach(card -> ids.add(card.getId()));
        return ids;
    }

    @Test
    public void add_ShouldIncreaseCountAndBeContained() {
        Assert.assertEquals(4, this.arena.count());
        Assert.assertTrue(this.arena.contains(new Battlecard(3, CardType.SPELL, "x", 0, 0)));
        Assert.assertFalse(this.arena.contains(new Battlecard(99, CardType.SPELL, "x", 0, 0)));
    }

    @Test
    public void add_SameId_ShouldReplaceCardInAllIndexes() {
        this.arena.add(new Battlecard(1, CardType.RANGED, "Archer", 1, 1));

        Assert.assertEquals(4, this.arena.count());
        Assert.assertEquals(List.of(2), ids(this.arena.getByNameOrderedBySwagDescending("Knight")));
        Assert.assertEquals(List.of(1), ids(this.arena.getByNameOrderedBySwagDescending("Archer")));
    }

    @Test
    public void changeCardType_ShouldUpdateType() {
        this.arena.changeCardType(3, CardType.BUILDING);
        Assert.assertEquals(CardType.BUILDING, this.arena.getById(3).getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void changeCardType_MissingId_ShouldThrow() {
        this.arena.changeCardType(99, CardType.BUILDING);
    }

    @Test
    public void removeById_ShouldRemoveFromAllIndexes() {
        this.arena.removeById(1);

        Assert.assertEquals(3, this.arena.count());
        Assert.assertEquals(List.of(2), ids(this.arena.getByNameOrderedBySwagDescending("Knight")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void removeById_MissingId_ShouldThrow() {
        this.arena.removeById(99);
    }

    @Test
    public void getByCardType_ShouldOrderByDamageDescThenId() {
        Assert.assertEquals(List.of(2, 4, 1), ids(this.arena.getByCardType(CardType.MELEE)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getByCardType_NoMatches_ShouldThrow() {
        this.arena.getByCardType(CardType.BUILDING);
    }

    @Test
    public void getByCardTypeAndMaximumDamage_ShouldFilterInclusive() {
        Assert.assertEquals(List.of(2, 4, 1), ids(this.arena.getByCardTypeAndMaximumDamage(CardType.MELEE, 20)));
        Assert.assertEquals(List.of(1), ids(this.arena.getByCardTypeAndMaximumDamage(CardType.MELEE, 19)));
    }

    @Test
    public void getByNameOrderedBySwagDescending_ShouldOrder() {
        Assert.assertEquals(List.of(2, 1), ids(this.arena.getByNameOrderedBySwagDescending("Knight")));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getByNameOrderedBySwagDescending_UnknownName_ShouldThrow() {
        this.arena.getByNameOrderedBySwagDescending("Nobody");
    }

    @Test
    public void getByNameAndSwagRange_ShouldUseHalfOpenRange() {
        Assert.assertEquals(List.of(1), ids(this.arena.getByNameAndSwagRange("Knight", 5, 15)));
    }

    @Test
    public void getAllByNameAndSwag_ShouldReturnHighestSwagPerName() {
        Assert.assertEquals(List.of(2, 3, 4), ids(this.arena.getAllByNameAndSwag()));
    }

    @Test
    public void findFirstLeastSwag_ShouldReturnLowestSwagFirst() {
        Assert.assertEquals(List.of(3, 1), ids(this.arena.findFirstLeastSwag(2)));
    }

    @Test
    public void findFirstLeastSwag_AllCards_ShouldNotThrow() {
        Assert.assertEquals(List.of(3, 1, 4, 2), ids(this.arena.findFirstLeastSwag(4)));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void findFirstLeastSwag_MoreThanCount_ShouldThrow() {
        this.arena.findFirstLeastSwag(5);
    }

    @Test
    public void getAllInSwagRange_ShouldBeInclusiveAndOrdered() {
        Assert.assertEquals(List.of(1, 4), ids(this.arena.getAllInSwagRange(5, 8)));
    }

    @Test
    public void iterator_ShouldVisitAllCards() {
        Assert.assertEquals(List.of(1, 2, 3, 4), ids(this.arena));
    }
}
