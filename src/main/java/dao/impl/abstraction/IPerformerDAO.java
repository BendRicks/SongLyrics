package dao.impl.abstraction;

import dao.exception.DAOException;
import entity.Album;
import entity.Performer;
import entity.Track;

import java.util.List;

public interface IPerformerDAO extends DAOFrame<Performer> {

    void updatePerformerStatus(Integer performerId, int statusId) throws DAOException;
    void updatePerformer(Integer performerId, String performerName, String coverPath, String performerDescription) throws DAOException;
    List<Track> getPerformerTracksById(Integer id) throws DAOException;
    List<Album> getPerformerAlbumsById(Integer id) throws DAOException;
    void arePerformersExist(List<Integer> performersIds) throws DAOException;
    List<Performer> searchPerformersByName(String name, Integer statusId, Integer limit, Integer offset) throws DAOException;
    Integer countPerformers(String name, int statusId) throws DAOException;
    void loadPerformerAlbums(Performer performer) throws DAOException;
    void loadPerformerTracks(Performer performer) throws DAOException;
    Integer getCreatorId(Integer performerId) throws DAOException;
    List<Performer> getCreatorPerformers(Integer userId) throws DAOException;
    void addCreatorId(Integer performerId, Integer userId) throws DAOException;

}
