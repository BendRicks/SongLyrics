package entity;

import java.io.Serializable;
import java.util.List;
import java.util.Scanner;

public class Track extends Entity implements Serializable {

    private String name;
    private List<Album> trackAlbums;
    private List<Performer> trackPerformers;
    private final String lyricsFilePath;
    private int status;

    public Track(String name, String lyricsFilePath){
        this.name = name;
        this.lyricsFilePath = lyricsFilePath;
        trackAlbums = null;
        trackPerformers = null;
    }

    public Track(int id, String name, String lyricsFilePath, int status){
        super(id);
        this.name = name;
        this.lyricsFilePath = lyricsFilePath;
        this.status = status;
        trackAlbums = null;
        trackPerformers = null;
    }

    public Track(int id, String name, String lyricsFilePath, List<Album> trackAlbums, List<Performer> trackPerformers, int status){
        super(id);
        this.name = name;
        this.trackAlbums = trackAlbums;
        this.trackPerformers = trackPerformers;
        this.status = status;
        this.lyricsFilePath = lyricsFilePath;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public String getName() {
        return name;
    }

    public List<Album> getTrackAlbums() {
        return trackAlbums;
    }

    public List<Performer> getTrackPerformers() {
        return trackPerformers;
    }

    public String getLyricsFilePath() {
        return lyricsFilePath;
    }

    public void setTrackAlbums(List<Album> trackAlbums) {
        this.trackAlbums = trackAlbums;
    }

    public void setTrackPerformers(List<Performer> trackPerformers) {
        this.trackPerformers = trackPerformers;
    }

}
