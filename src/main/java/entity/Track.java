package entity;

import dao.DAOConstants;

import java.io.Serializable;
import java.util.List;

public class Track extends Entity implements Serializable {

    private String name;
    private List<Album> trackAlbums;
    private List<Performer> trackPerformers;
    private final String lyrics;
    private int status;

    public Track(String name, String lyrics){
        this.name = name;
        this.lyrics = lyrics;
        trackAlbums = null;
        trackPerformers = null;
    }

    public Track(int id, String name, String lyrics, int status){
        super(id);
        this.name = name;
        this.lyrics = lyrics;
        this.status = status;
        trackAlbums = null;
        trackPerformers = null;
    }

    public Track(String name, String lyrics, List<Album> trackAlbums, List<Performer> trackPerformers, int status){
        this.name = name;
        this.trackAlbums = trackAlbums;
        this.trackPerformers = trackPerformers;
        this.status = status;
        this.lyrics = lyrics;
    }

    public Track(int id, String name, String lyrics, List<Album> trackAlbums, List<Performer> trackPerformers, int status){
        super(id);
        this.name = name;
        this.trackAlbums = trackAlbums;
        this.trackPerformers = trackPerformers;
        this.status = status;
        this.lyrics = lyrics;
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

    public String getLyrics() {
        return lyrics;
    }

    public void setTrackAlbums(List<Album> trackAlbums) {
        this.trackAlbums = trackAlbums;
    }

    public void setTrackPerformers(List<Performer> trackPerformers) {
        this.trackPerformers = trackPerformers;
    }

}
