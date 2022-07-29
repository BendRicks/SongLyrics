package dao.impl.abstraction;

import dao.exception.DAOException;
import entity.Album;
import entity.Performer;
import entity.Track;

import java.util.List;

public interface IAlbumDAO extends DAOFrame<Album>{

    Album updateAlbumStatus(Integer albumId, int statusId) throws DAOException;
    Album updateAlbumName(Integer albumId, String albumName) throws DAOException;
    List<Track> getAlbumTracksById(Integer id) throws DAOException;
    List<Performer> getAlbumPerformersById(Integer id) throws DAOException;
    List<Album> searchAlbumsByName(String name, Integer statusId, Integer pageOffset) throws DAOException;
    Integer countAlbums(Integer statusId) throws DAOException;
    Album addAlbumPerformersByIds(Integer albumId, List<Integer> performersIds) throws DAOException;
    void clearAlbumPerformers(Integer albumId) throws DAOException;
    Album loadAlbumDependencies(Album album) throws DAOException;
    List<Album> loadAlbumsDependencies(List<Album> albums) throws DAOException;
    Album addAlbumPerformerById(Integer albumId, Integer performerId) throws DAOException;

}
