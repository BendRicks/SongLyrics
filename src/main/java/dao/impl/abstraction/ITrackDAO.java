package dao.impl.abstraction;

import dao.exception.DAOException;
import entity.Album;
import entity.Performer;
import entity.Track;

import java.util.List;

public interface ITrackDAO extends DAOFrame<Track> {

    Track updateTrackStatus(Integer trackId, Integer statusId) throws DAOException;
    Track updateTrackName(Integer trackId, String trackName) throws DAOException;
    List<Performer> getTrackPerformersById(Integer id) throws DAOException;
    List<Album> getTrackAlbumsById(Integer id) throws DAOException;
    List<Track> searchTracksByName(String name, Integer statusId, Integer pageOffset) throws DAOException;
    Integer countTracks(Integer statusId) throws DAOException;
    Track addTrackAlbumsByIds(Integer trackId, List<Integer> albumsIds) throws DAOException;
    void clearTrackAlbums(Integer trackId) throws  DAOException;
    Track addTrackPerformersByIds(Integer trackId, List<Integer> performersIds) throws DAOException;
    void clearTrackPerformers(Integer trackId) throws  DAOException;
    Track loadTrackDependencies(Track track) throws DAOException;
    List<Track> loadTracksDependencies(List<Track> tracks) throws DAOException;

}
