package core;

import models.Doodle;

import java.util.*;
import java.util.stream.Collectors;

public class DoodleSearchImpl implements DoodleSearch {
    private static final Comparator<Doodle> BY_REVENUE_THEN_VISITS =
            Comparator.comparingDouble(Doodle::getRevenue).reversed()
                    .thenComparing(Comparator.comparingInt(Doodle::getVisits).reversed());

    private Map<String, Doodle> doodlesById;
    private Map<String, Doodle> doodlesByTitle;

    public DoodleSearchImpl() {
        this.doodlesById = new LinkedHashMap<>();
        this.doodlesByTitle = new HashMap<>();
    }

    @Override
    public void addDoodle(Doodle doodle) {
        this.doodlesById.put(doodle.getId(), doodle);
        this.doodlesByTitle.put(doodle.getTitle(), doodle);
    }

    @Override
    public void removeDoodle(String doodleId) {
        Doodle doodle = this.doodlesById.remove(doodleId);

        if (doodle == null) {
            throw new IllegalArgumentException();
        }

        this.doodlesByTitle.remove(doodle.getTitle(), doodle);
    }

    @Override
    public int size() {
        return this.doodlesById.size();
    }

    @Override
    public boolean contains(Doodle doodle) {
        return this.doodlesById.containsKey(doodle.getId());
    }

    @Override
    public Doodle getDoodle(String id) {
        Doodle doodle = this.doodlesById.get(id);

        if (doodle == null) {
            throw new IllegalArgumentException();
        }

        return doodle;
    }

    @Override
    public double getTotalRevenueFromDoodleAds() {
        return this.doodlesById.values()
                .stream()
                .filter(Doodle::getIsAd)
                .mapToDouble(doodle -> doodle.getRevenue() * doodle.getVisits())
                .sum();
    }

    @Override
    public void visitDoodle(String title) {
        Doodle doodle = this.doodlesByTitle.get(title);

        if (doodle == null) {
            throw new IllegalArgumentException();
        }

        doodle.setVisits(doodle.getVisits() + 1);
    }

    @Override
    public Iterable<Doodle> searchDoodles(String searchQuery) {
        return this.doodlesById.values()
                .stream()
                .filter(doodle -> doodle.getTitle().contains(searchQuery))
                .sorted(Comparator.comparing(Doodle::getIsAd).reversed()
                        .thenComparingInt(doodle -> doodle.getTitle().indexOf(searchQuery))
                        .thenComparing(Comparator.comparingInt(Doodle::getVisits).reversed()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Doodle> getDoodleAds() {
        return this.doodlesById.values()
                .stream()
                .filter(Doodle::getIsAd)
                .sorted(BY_REVENUE_THEN_VISITS)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Doodle> getTop3DoodlesByRevenueThenByVisits() {
        return this.doodlesById.values()
                .stream()
                .sorted(BY_REVENUE_THEN_VISITS)
                .limit(3)
                .collect(Collectors.toList());
    }
}
