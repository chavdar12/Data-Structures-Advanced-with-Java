package core;

import models.Movie;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MovieDatabaseImpl implements MovieDatabase {
    private static final Comparator<Movie> BY_RATING_THEN_YEAR =
            Comparator.comparingDouble(Movie::getRating).reversed()
                    .thenComparing(Comparator.comparingInt(Movie::getReleaseYear).reversed());

    private Map<String, Movie> moviesById;
    private Map<String, Set<Movie>> moviesByActor;

    public MovieDatabaseImpl() {
        this.moviesById = new LinkedHashMap<>();
        this.moviesByActor = new HashMap<>();
    }

    @Override
    public void addMovie(Movie movie) {
        this.moviesById.put(movie.getId(), movie);

        for (String actor : movie.getActors()) {
            this.moviesByActor.computeIfAbsent(actor, k -> new LinkedHashSet<>()).add(movie);
        }
    }

    @Override
    public void removeMovie(String movieId) {
        Movie movie = this.moviesById.remove(movieId);

        if (movie == null) {
            throw new IllegalArgumentException();
        }

        for (String actor : movie.getActors()) {
            Set<Movie> movies = this.moviesByActor.get(actor);
            movies.remove(movie);

            if (movies.isEmpty()) {
                this.moviesByActor.remove(actor);
            }
        }
    }

    @Override
    public int size() {
        return this.moviesById.size();
    }

    @Override
    public boolean contains(Movie movie) {
        return this.moviesById.containsKey(movie.getId());
    }

    @Override
    public Iterable<Movie> getMoviesByActor(String actorName) {
        return this.sortedOrThrow(this.moviesByActor.getOrDefault(actorName, Collections.emptySet()).stream());
    }

    @Override
    public Iterable<Movie> getMoviesByActors(List<String> actors) {
        if (actors.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return this.sortedOrThrow(this.moviesByActor.getOrDefault(actors.get(0), Collections.emptySet())
                .stream()
                .filter(movie -> movie.getActors().containsAll(actors)));
    }

    @Override
    public Iterable<Movie> getMoviesByYear(Integer releaseYear) {
        return this.moviesById.values()
                .stream()
                .filter(movie -> movie.getReleaseYear() == releaseYear)
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Movie> getMoviesInRatingRange(double lowerBound, double upperBound) {
        return this.moviesById.values()
                .stream()
                .filter(movie -> movie.getRating() >= lowerBound && movie.getRating() <= upperBound)
                .sorted(Comparator.comparingDouble(Movie::getRating).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Movie> getAllMoviesOrderedByActorPopularityThenByRatingThenByYear() {
        return this.moviesById.values()
                .stream()
                .sorted(Comparator.comparingInt(this::getActorPopularity).reversed()
                        .thenComparing(BY_RATING_THEN_YEAR))
                .collect(Collectors.toList());
    }

    // popularity = movie count of the most popular actor in the movie
    private int getActorPopularity(Movie movie) {
        return movie.getActors()
                .stream()
                .mapToInt(actor -> this.moviesByActor.get(actor).size())
                .max()
                .orElse(0);
    }

    private List<Movie> sortedOrThrow(Stream<Movie> movies) {
        List<Movie> result = movies.sorted(BY_RATING_THEN_YEAR).collect(Collectors.toList());

        if (result.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return result;
    }
}
