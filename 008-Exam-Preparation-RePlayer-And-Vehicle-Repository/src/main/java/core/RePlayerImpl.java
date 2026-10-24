package core;

import models.Track;

import java.util.*;
import java.util.stream.Collectors;

public class RePlayerImpl implements RePlayer {
    private Map<String, Track> tracksById;
    private Map<String, Map<String, Track>> tracksByAlbum;
    private Map<String, String> albumByTrackId;
    private Deque<Track> queue;

    public RePlayerImpl() {
        this.tracksById = new HashMap<>();
        this.tracksByAlbum = new LinkedHashMap<>();
        this.albumByTrackId = new HashMap<>();
        this.queue = new ArrayDeque<>();
    }

    @Override
    public void addTrack(Track track, String album) {
        this.tracksById.put(track.getId(), track);
        this.tracksByAlbum.computeIfAbsent(album, k -> new LinkedHashMap<>()).put(track.getTitle(), track);
        this.albumByTrackId.put(track.getId(), album);
    }

    @Override
    public void removeTrack(String trackTitle, String albumName) {
        Map<String, Track> album = this.getAlbumTracks(albumName);
        Track track = album.remove(trackTitle);

        if (track == null) {
            throw new IllegalArgumentException();
        }

        // the queue is cleaned lazily in play(), so removal stays O(1)
        this.tracksById.remove(track.getId());
        this.albumByTrackId.remove(track.getId());
    }

    @Override
    public boolean contains(Track track) {
        return this.tracksById.containsKey(track.getId());
    }

    @Override
    public int size() {
        return this.tracksById.size();
    }

    @Override
    public Track getTrack(String title, String albumName) {
        Track track = this.getAlbumTracks(albumName).get(title);

        if (track == null) {
            throw new IllegalArgumentException();
        }

        return track;
    }

    @Override
    public Iterable<Track> getAlbum(String albumName) {
        return this.getAlbumTracks(albumName).values()
                .stream()
                .sorted(Comparator.comparingInt(Track::getPlays).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public void addToQueue(String trackName, String albumName) {
        this.queue.offer(this.getTrack(trackName, albumName));
    }

    @Override
    public Track play() {
        while (!this.queue.isEmpty()) {
            Track track = this.queue.poll();

            if (this.tracksById.get(track.getId()) == track) {
                track.setPlays(track.getPlays() + 1);
                return track;
            }
        }

        throw new IllegalArgumentException();
    }

    @Override
    public Iterable<Track> getTracksInDurationRangeOrderedByDurationThenByPlaysDescending(int lowerBound, int upperBound) {
        return this.tracksById.values()
                .stream()
                .filter(track -> track.getDurationInSeconds() >= lowerBound
                        && track.getDurationInSeconds() <= upperBound)
                .sorted(Comparator.comparingInt(Track::getDurationInSeconds)
                        .thenComparing(Comparator.comparingInt(Track::getPlays).reversed()))
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Track> getTracksOrderedByAlbumNameThenByPlaysDescendingThenByDurationDescending() {
        return this.tracksById.values()
                .stream()
                .sorted(Comparator.comparing((Track track) -> this.albumByTrackId.get(track.getId()))
                        .thenComparing(Comparator.comparingInt(Track::getPlays).reversed())
                        .thenComparing(Comparator.comparingInt(Track::getDurationInSeconds).reversed()))
                .collect(Collectors.toList());
    }

    @Override
    public Map<String, List<Track>> getDiscography(String artistName) {
        Map<String, List<Track>> discography = new LinkedHashMap<>();

        this.tracksByAlbum.forEach((album, tracks) -> {
            List<Track> artistTracks = tracks.values()
                    .stream()
                    .filter(track -> track.getArtist().equals(artistName))
                    .collect(Collectors.toList());

            if (!artistTracks.isEmpty()) {
                discography.put(album, artistTracks);
            }
        });

        if (discography.isEmpty()) {
            throw new IllegalArgumentException();
        }

        return discography;
    }

    private Map<String, Track> getAlbumTracks(String albumName) {
        Map<String, Track> album = this.tracksByAlbum.get(albumName);

        if (album == null) {
            throw new IllegalArgumentException();
        }

        return album;
    }
}
