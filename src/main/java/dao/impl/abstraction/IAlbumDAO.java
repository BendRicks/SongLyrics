package dao.impl.abstraction;

import dao.exception.DAOException;
import entity.Album;
import entity.Performer;
import entity.Track;

import java.util.List;

public interface IAlbumDAO extends DAOFrame<Album>{

    void updateAlbumStatus(Integer albumId, int statusId) throws DAOException;
    void updateAlbum(Integer albumId, String albumName, String coverPath, String albumDescription) throws DAOException;
    List<Track> getAlbumTracksById(Integer id) throws DAOException;
    List<Performer> getAlbumPerformersById(Integer id) throws DAOException;
    List<Album> searchAlbumsByName(String name, Integer statusId, Integer limit, Integer offset) throws DAOException;
    Integer countAlbums(String name, int statusId) throws DAOException;
    void addAlbumPerformersByIds(Integer albumId, List<Integer> performersIds) throws DAOException;
    void updateAlbumPerformersByIds(Integer albumId, List<Integer> performersIds) throws DAOException;
    void loadAlbumPerformers(Album album) throws DAOException;
    void loadAlbumTracks(Album album) throws DAOException;
    void areAlbumsExist(List<Integer> albumsIds) throws DAOException;
    List<Album> loadAlbumsPerformers(List<Album> albums) throws DAOException;
    List<Album> loadAlbumsTracks(List<Album> albums) throws DAOException;
    Integer getCreatorId(Integer albumId) throws DAOException;
    List<Album> getCreatorAlbums(Integer userId) throws DAOException;
    void addCreatorId(Integer albumId, Integer userId) throws DAOException;

}
