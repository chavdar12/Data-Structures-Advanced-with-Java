import java.util.*;
import java.util.stream.Collectors;

public class RoyalArena implements IArena {

    private Map<Integer, Battlecard> battleCardsById;

    Map<String, List<Battlecard>> battleCardsByName;

    public RoyalArena() {
        this.battleCardsById = new LinkedHashMap<>();
        this.battleCardsByName = new LinkedHashMap<>();
    }

    @Override
    public void add(Battlecard card) {
        this.battleCardsById.put(card.getId(), card);

        if (!battleCardsByName.containsKey(card.getName())) {
            battleCardsByName.put(card.getName(), new ArrayList<>());
        }

        battleCardsByName.get(card.getName()).add(card);
    }

    @Override
    public boolean contains(Battlecard card) {
        return this.battleCardsById.containsKey(card.getId());
    }

    @Override
    public int count() {
        return this.battleCardsById.size();
    }

    @Override
    public void changeCardType(int id, CardType type) {
        if (!this.battleCardsById.containsKey(id)) {
            throw new IllegalArgumentException();
        }

        this.battleCardsById.get(id).setType(type);
    }

    @Override
    public Battlecard getById(int id) {
        if (!this.battleCardsById.containsKey(id)) {
            throw new UnsupportedOperationException();
        }

        return this.battleCardsById.get(id);
    }

    @Override
    public void removeById(int id) {
        if (!this.battleCardsById.containsKey(id)) {
            throw new UnsupportedOperationException();
        }

        Battlecard battleCardToRemove = this.battleCardsById.remove(id);
        this.battleCardsByName.get(battleCardToRemove.getName()).remove(battleCardToRemove);
    }

    @Override
    public Iterable<Battlecard> getByCardType(CardType type) {
        List<Battlecard> result = this.battleCardsById.values()
                .stream()
                .filter(card -> card.getType().equals(type))
                .sorted((card1, card2) -> {
                    int compareResult = Double.compare(card2.getDamage(), card1.getDamage());

                    if (compareResult == 0) {
                        return Integer.compare(card1.getId(), card2.getId());
                    }

                    return compareResult;
                })
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @Override
    public Iterable<Battlecard> getByTypeAndDamageRangeOrderedByDamageThenById(CardType type, int lo, int hi) {
        List<Battlecard> result = this.battleCardsById.values()
                .stream()
                .filter(card -> card.getType().equals(type)
                        && card.getDamage() > lo && card.getDamage() < hi)
                .sorted((card1, card2) -> {
                    int compareResult = Double.compare(card2.getDamage(), card1.getDamage());

                    if (compareResult == 0) {
                        return Integer.compare(card1.getId(), card2.getId());
                    }

                    return compareResult;
                })
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @Override
    public Iterable<Battlecard> getByCardTypeAndMaximumDamage(CardType type, double damage) {
        List<Battlecard> result = this.battleCardsById.values()
                .stream()
                .filter(card -> card.getType().equals(type)
                        && card.getDamage() <= damage)
                .sorted((card1, card2) -> {
                    int compareResult = Double.compare(card2.getDamage(), card1.getDamage());

                    if (compareResult == 0) {
                        return Integer.compare(card1.getId(), card2.getId());
                    }

                    return compareResult;
                })
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @Override
    public Iterable<Battlecard> getByNameOrderedBySwagDescending(String name) {
        List<Battlecard> result = this.battleCardsByName.get(name)
                .stream()
                .sorted((card1, card2) -> {
                    int compareResult = Double.compare(card2.getSwag(), card1.getSwag());

                    if (compareResult == 0) {
                        return Integer.compare(card1.getId(), card2.getId());
                    }

                    return compareResult;
                })
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @Override
    public Iterable<Battlecard> getByNameAndSwagRange(String name, double lo, double hi) {
        List<Battlecard> result = this.battleCardsById.values()
                .stream()
                .filter(card -> card.getName().equals(name)
                        && card.getSwag() >= lo && card.getSwag() < hi)
                .sorted((card1, card2) -> {
                    int compareResult = Double.compare(card2.getSwag(), card1.getSwag());

                    if (compareResult == 0) {
                        return Integer.compare(card1.getId(), card2.getId());
                    }

                    return compareResult;
                })
                .collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new UnsupportedOperationException();
        }

        return result;
    }

    @Override
    public Iterable<Battlecard> getAllByNameAndSwag() {

        return this.battleCardsByName.values()
                .stream()
                .map(cardList -> cardList.stream()
                        .max(Comparator.comparingDouble(Battlecard::getSwag))
                        .orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Battlecard> findFirstLeastSwag(int n) {
        if (n >= this.count()) {
            throw new UnsupportedOperationException();
        }

        return battleCardsById.values()
                .stream()
                .sorted((card1, card2) -> {
                    int compareResult = Double.compare(card1.getSwag(), card2.getSwag());

                    if (compareResult == 0) {
                        return Integer.compare(card1.getId(), card2.getId());
                    }

                    return compareResult;
                })
                .limit(n)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Battlecard> getAllInSwagRange(double lo, double hi) {

        return battleCardsById.values()
                .stream()
                .filter(card -> card.getSwag() >= lo && card.getSwag() <= hi)
                .sorted(Comparator.comparingDouble(Battlecard::getSwag))
                .collect(Collectors.toList());
    }

    @Override
    public Iterator<Battlecard> iterator() {
        return this.battleCardsById.values().iterator();
    }
}
