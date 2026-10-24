package core;

import models.Vehicle;

import java.util.*;
import java.util.stream.Collectors;

public class VehicleRepositoryImpl implements VehicleRepository {
    private static final Comparator<Vehicle> BY_PRICE_THEN_ID =
            Comparator.comparingDouble(Vehicle::getPrice).thenComparing(Vehicle::getId);

    private Map<String, Vehicle> vehiclesById;
    private Map<String, String> sellerByVehicleId;
    private Map<String, TreeSet<Vehicle>> vehiclesBySeller;

    public VehicleRepositoryImpl() {
        this.vehiclesById = new LinkedHashMap<>();
        this.sellerByVehicleId = new HashMap<>();
        this.vehiclesBySeller = new HashMap<>();
    }

    @Override
    public void addVehicleForSale(Vehicle vehicle, String sellerName) {
        this.vehiclesById.put(vehicle.getId(), vehicle);
        this.sellerByVehicleId.put(vehicle.getId(), sellerName);
        this.vehiclesBySeller.computeIfAbsent(sellerName, k -> new TreeSet<>(BY_PRICE_THEN_ID)).add(vehicle);
    }

    @Override
    public void removeVehicle(String vehicleId) {
        Vehicle vehicle = this.vehiclesById.remove(vehicleId);

        if (vehicle == null) {
            throw new IllegalArgumentException();
        }

        String seller = this.sellerByVehicleId.remove(vehicleId);
        this.vehiclesBySeller.get(seller).remove(vehicle);
    }

    @Override
    public int size() {
        return this.vehiclesById.size();
    }

    @Override
    public boolean contains(Vehicle vehicle) {
        return this.vehiclesById.containsKey(vehicle.getId());
    }

    @Override
    public Iterable<Vehicle> getVehicles(List<String> keywords) {
        return this.vehiclesById.values()
                .stream()
                .filter(vehicle -> keywords.contains(vehicle.getBrand())
                        || keywords.contains(vehicle.getModel())
                        || keywords.contains(vehicle.getLocation())
                        || keywords.contains(vehicle.getColor()))
                .sorted(Comparator.comparing(Vehicle::getIsVIP).reversed()
                        .thenComparing(Comparator.comparingInt(Vehicle::getHorsepower).reversed())
                        .thenComparingDouble(Vehicle::getPrice))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Vehicle> getVehiclesBySeller(String sellerName) {
        return new ArrayList<>(this.getSellerVehicles(sellerName));
    }

    @Override
    public Iterable<Vehicle> getVehiclesInPriceRange(double lowerBound, double upperBound) {
        return this.vehiclesById.values()
                .stream()
                .filter(vehicle -> vehicle.getPrice() >= lowerBound && vehicle.getPrice() <= upperBound)
                .sorted(Comparator.comparingInt(Vehicle::getHorsepower).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Vehicle>> getAllVehiclesGroupedByBrand() {
        if (this.vehiclesById.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return this.vehiclesById.values()
                .stream()
                .sorted(Comparator.comparingDouble(Vehicle::getPrice))
                .collect(Collectors.groupingBy(Vehicle::getBrand, LinkedHashMap::new, Collectors.toList()));
    }

    @Override
    public Iterable<Vehicle> getAllVehiclesOrderedByHorsepowerDescendingThenByPriceThenBySellerName() {
        return this.vehiclesById.values()
                .stream()
                .sorted(Comparator.comparingInt(Vehicle::getHorsepower).reversed()
                        .thenComparingDouble(Vehicle::getPrice)
                        .thenComparing(vehicle -> this.sellerByVehicleId.get(vehicle.getId())))
                .collect(Collectors.toList());
    }

    @Override
    public Vehicle buyCheapestFromSeller(String sellerName) {
        Vehicle vehicle = this.getSellerVehicles(sellerName).pollFirst();

        this.vehiclesById.remove(vehicle.getId());
        this.sellerByVehicleId.remove(vehicle.getId());

        return vehicle;
    }

    private TreeSet<Vehicle> getSellerVehicles(String sellerName) {
        TreeSet<Vehicle> vehicles = this.vehiclesBySeller.get(sellerName);

        if (vehicles == null || vehicles.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return vehicles;
    }
}
