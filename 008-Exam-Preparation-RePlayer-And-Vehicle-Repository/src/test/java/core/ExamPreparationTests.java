package core;

import models.Track;
import models.Vehicle;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

// Covers the methods the Judge skeleton tests leave out.
public class ExamPreparationTests {

    @Test
    public void rePlayer_QueueAndQueries_ShouldWork() {
        RePlayer player = new RePlayerImpl();
        Track a = new Track("1", "A", "Queen", 10, 200);
        Track b = new Track("2", "B", "Queen", 30, 100);
        Track c = new Track("3", "C", "Abba", 20, 200);
        player.addTrack(a, "Album1");
        player.addTrack(b, "Album1");
        player.addTrack(c, "Album2");

        assertSame(b, player.getTrack("B", "Album1"));
        assertEquals(List.of(b, a), player.getAlbum("Album1"));
        assertEquals(List.of(b, c, a), player.getTracksInDurationRangeOrderedByDurationThenByPlaysDescending(100, 200));
        assertEquals(Map.of("Album1", List.of(a, b)), player.getDiscography("Queen"));

        player.addToQueue("A", "Album1");
        player.addToQueue("C", "Album2");
        player.removeTrack("A", "Album1");

        assertSame(c, player.play());
        assertEquals(21, c.getPlays());
        assertEquals(2, player.size());
        assertThrows(IllegalArgumentException.class, player::play);
    }

    @Test
    public void rePlayer_MissingData_ShouldThrow() {
        RePlayer player = new RePlayerImpl();
        player.addTrack(new Track("1", "A", "Queen", 1, 1), "Album1");

        assertThrows(IllegalArgumentException.class, () -> player.getAlbum("Missing"));
        assertThrows(IllegalArgumentException.class, () -> player.getTrack("Missing", "Album1"));
        assertThrows(IllegalArgumentException.class, () -> player.removeTrack("A", "Missing"));
        assertThrows(IllegalArgumentException.class, () -> player.addToQueue("Missing", "Album1"));
        assertThrows(IllegalArgumentException.class, () -> player.getDiscography("Nobody"));
    }

    @Test
    public void vehicleRepository_QueriesAndBuying_ShouldWork() {
        VehicleRepository repository = new VehicleRepositoryImpl();
        Vehicle cheap = new Vehicle("1", "BMW", "X5", "Sofia", "Blue", 300, 10000, false);
        Vehicle vip = new Vehicle("2", "Audi", "A4", "Varna", "Red", 200, 20000, true);
        Vehicle strong = new Vehicle("3", "BMW", "M5", "Sofia", "Black", 600, 90000, false);
        repository.addVehicleForSale(cheap, "George");
        repository.addVehicleForSale(vip, "George");
        repository.addVehicleForSale(strong, "Ivan");

        assertEquals(List.of(vip, strong, cheap), repository.getVehicles(List.of("Sofia", "Red")));
        assertEquals(List.of(cheap, vip), repository.getVehiclesBySeller("George"));
        assertEquals(List.of(cheap, vip), repository.getVehiclesInPriceRange(10000, 20000));
        assertEquals(Map.of("BMW", List.of(cheap, strong), "Audi", List.of(vip)),
                repository.getAllVehiclesGroupedByBrand());

        assertSame(cheap, repository.buyCheapestFromSeller("George"));
        assertFalse(repository.contains(cheap));
        assertEquals(2, repository.size());

        repository.removeVehicle("2");
        assertThrows(IllegalArgumentException.class, () -> repository.buyCheapestFromSeller("George"));
        assertThrows(IllegalArgumentException.class, () -> repository.getVehiclesBySeller("Nobody"));
        assertThrows(IllegalArgumentException.class,
                () -> new VehicleRepositoryImpl().getAllVehiclesGroupedByBrand());
    }
}
