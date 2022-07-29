package dao.impl.abstraction;

import dao.exception.DAOException;
import entity.Album;
import entity.Performer;
import entity.Track;

import java.util.List;

public interface IPerformerDAO extends DAOFrame<Performer> {

    Performer updatePerformerStatus(Integer performerId, int statusId) throws DAOException;
    Performer updatePerformerName(Integer performerId, String performerName) throws DAOException;
    List<Track> getPerformerTracksById(Integer id) throws DAOException;
    List<Album> getPerformerAlbumsById(Integer id) throws DAOException;
    List<Performer> searchPerformersByName(String name, Integer statusId, Integer pageOffset) throws DAOException;
    Integer countPerformers(Integer statusId) throws DAOException;
    Performer loadPerformerDependencies(Performer performer) throws DAOException;
    List<Performer> loadPerformersDependencies(List<Performer> performers) throws DAOException;

}
