package entity;

import java.io.Serializable;
import java.util.List;

public class Album extends Entity implements Serializable {

    private final String name;
    private final String coverImagePath;
    private List<Performer> albumPerformers;
    private List<Track> albumTracks;
    private final String description;
    private int status;

    public Album(String name, String coverImagePath, String description){
        this.name = name;
        this.coverImagePath = coverImagePath;
        this.description = description;
        this.albumPerformers = null;
        this.albumTracks = null;
    }

    public Album(int id, String name, String coverImagePath, String description, int status){
        super(id);
        this.name = name;
        this.coverImagePath = coverImagePath;
        this.description = description;
        this.albumPerformers = null;
        this.albumTracks = null;
        this.status = status;
    }

    public Album(int id, String name, String coverImagePath, String description,
                 List<Performer> albumPerformers, List<Track> albumTracks, int status){
        super(id);
        this.name = name;
        this.coverImagePath = coverImagePath;
        this.description = description;
        this.albumPerformers = albumPerformers;
        this.albumTracks = albumTracks;
        this.status = status;
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

    public String getCoverImagePath() {
        return coverImagePath;
    }

    public String getDescription() {
        return description;
    }

    public List<Performer> getAlbumPerformers() {
        return albumPerformers;
    }

    public List<Track> getAlbumTracks() {
        return albumTracks;
    }

    public void setAlbumPerformers(List<Performer> albumPerformers) {
        this.albumPerformers = albumPerformers;
    }

    public void setAlbumTracks(List<Track> albumTracks) {
        this.albumTracks = albumTracks;
    }


}
