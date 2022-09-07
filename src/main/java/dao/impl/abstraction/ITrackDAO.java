package dao.impl.abstraction;

import dao.exception.DAOException;
import entity.Album;
import entity.Performer;
import entity.Track;

import java.util.List;

public interface ITrackDAO extends DAOFrame<Track> {

    void updateTrackStatus(Integer trackId, Integer statusId) throws DAOException;
    void updateTrack(Integer trackId, String trackName, String trackLyrics) throws DAOException;
    List<Performer> getTrackPerformersById(Integer id) throws DAOException;
    List<Album> getTrackAlbumsById(Integer id) throws DAOException;
    List<Track> searchTracksByName(String name, Integer statusId, Integer limit, Integer offset) throws DAOException;
    Integer countTracks(String name, int statusId) throws DAOException;
    void addTrackAlbumsByIds(Integer trackId, List<Integer> albumsIds) throws DAOException;
    void updateTrackAlbumsByIds(Integer trackId, List<Integer> albumsIds) throws DAOException;
    void addTrackPerformersByIds(Integer trackId, List<Integer> performersIds) throws DAOException;
    void updateTrackPerformersByIds(Integer trackId, List<Integer> performersIds) throws DAOException;
    void loadTrackPerformers(Track track) throws DAOException;
    void loadTrackAlbums(Track track) throws DAOException;
    List<Track> loadTracksPerformers(List<Track> tracks) throws DAOException;
    List<Track> loadTracksAlbums(List<Track> tracks) throws DAOException;
    Integer getCreatorId(Integer trackId) throws DAOException;
    List<Track> getCreatorTracks(Integer userId) throws DAOException;
    void addCreatorId(Integer trackId, Integer userId) throws DAOException;

}
