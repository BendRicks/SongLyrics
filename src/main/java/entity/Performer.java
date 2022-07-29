package entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Performer extends Entity implements Serializable {

    private final String name;
    private final String coverImagePath;
    private List<Album> performerAlbums;
    private List<Track> performerTracks;
    private final String descriptionFilePath;
    private int status;

    public Performer(String name, String descriptionFilePath, String coverImagePath){
        this.name = name;
        this.descriptionFilePath = descriptionFilePath;
        this.coverImagePath = coverImagePath;
    }

    public Performer(int id, String name, String descriptionFilePath, String coverImagePath, int status){
        super(id);
        this.name = name;
        this.descriptionFilePath = descriptionFilePath;
        this.coverImagePath = coverImagePath;
        this.status = status;

    }

    public Performer(int id, String name, String descriptionFilePath, String coverImagePath,
                     List<Album> performerAlbums, List<Track> performerTracks, int status){
        super(id);
        this.name = name;
        this.coverImagePath = coverImagePath;
        this.descriptionFilePath = descriptionFilePath;
        this.performerAlbums = performerAlbums;
        this.performerTracks = performerTracks;
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

    public String getDescriptionFilePath() {
        return descriptionFilePath;
    }

    public List<Album> getPerformerAlbums() {
        return performerAlbums;
    }

    public List<Track> getPerformerTracks() {
        return performerTracks;
    }

    public void setPerformerAlbums(List<Album> performerAlbums) {
        this.performerAlbums = performerAlbums;
    }

    public void setPerformerTracks(List<Track> performerTracks) {
        this.performerTracks = performerTracks;
    }

}
