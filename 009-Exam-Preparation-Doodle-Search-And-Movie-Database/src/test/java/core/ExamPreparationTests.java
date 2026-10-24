package core;

import models.Doodle;
import models.Movie;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

// Covers the methods the Judge skeleton tests leave out.
public class ExamPreparationTests {

    @Test
    public void doodleSearch_RevenueVisitsAndRanking_ShouldWork() {
        DoodleSearch search = new DoodleSearchImpl();
        Doodle ad1 = new Doodle("1", "cats", 10, true, 2.0);
        Doodle ad2 = new Doodle("2", "dogs", 5, true, 3.0);
        Doodle plain = new Doodle("3", "birds", 100, false, 1.0);
        Doodle top = new Doodle("4", "fish", 1, false, 9.0);
        search.addDoodle(ad1);
        search.addDoodle(ad2);
        search.addDoodle(plain);
        search.addDoodle(top);

        assertEquals(35.0, search.getTotalRevenueFromDoodleAds(), 1e-9);

        search.visitDoodle("cats");
        assertEquals(11, ad1.getVisits());

        assertEquals(List.of(ad2, ad1), search.getDoodleAds());
        assertEquals(List.of(top, ad2, ad1), search.getTop3DoodlesByRevenueThenByVisits());
        assertSame(plain, search.getDoodle("3"));

        search.removeDoodle("3");
        assertFalse(search.contains(plain));
        assertThrows(IllegalArgumentException.class, () -> search.getDoodle("3"));
        assertThrows(IllegalArgumentException.class, () -> search.removeDoodle("3"));
        assertThrows(IllegalArgumentException.class, () -> search.visitDoodle("birds"));
    }

    @Test
    public void movieDatabase_ActorYearAndRatingQueries_ShouldWork() {
        MovieDatabase database = new MovieDatabaseImpl();
        Movie old = new Movie("1", "Old", 2000, 7.0, List.of("Pesho", "Gosho"));
        Movie recent = new Movie("2", "Recent", 2010, 7.0, List.of("Pesho"));
        Movie best = new Movie("3", "Best", 2000, 9.0, List.of("Gosho"));
        database.addMovie(old);
        database.addMovie(recent);
        database.addMovie(best);

        assertEquals(List.of(recent, old), database.getMoviesByActor("Pesho"));
        assertEquals(List.of(old), database.getMoviesByActors(List.of("Gosho", "Pesho")));
        assertEquals(List.of(best, old), database.getMoviesByYear(2000));
        assertEquals(List.of(best, old, recent), database.getMoviesInRatingRange(7.0, 9.0));

        database.removeMovie("1");
        assertEquals(2, database.size());
        assertThrows(IllegalArgumentException.class, () -> database.getMoviesByActors(List.of("Gosho", "Pesho")));
        assertThrows(IllegalArgumentException.class, () -> database.getMoviesByActor("Nobody"));
        assertThrows(IllegalArgumentException.class, () -> database.removeMovie("1"));
    }
}
