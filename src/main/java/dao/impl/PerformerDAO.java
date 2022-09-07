package dao.impl;

import dao.DAOConstants;
import dao.DAOFactory;
import dao.connection.ConnectionManager;
import dao.exception.DAOException;
import dao.impl.abstraction.AbstractDAO;
import dao.impl.abstraction.IPerformerDAO;
import entity.Album;
import entity.Performer;
import entity.Track;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.event.ListDataEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PerformerDAO extends AbstractDAO implements IPerformerDAO {

    private final Logger logger = LogManager.getLogger(Performer.class);
    private static final String SAVE_PERFORMER_QUERY = "INSERT INTO `performer` (`performer_name`, `description`, `cover_url`, `status_id`) VALUES (?, ?, ?, ?);";
    private static final String UPDATE_PERFORMER_STATUS_QUERY = "UPDATE `performer` SET `status_id` = ? WHERE `performer_id` = ?";
    private static final String UPDATE_PERFORMER_QUERY = "UPDATE `performer` SET `performer_name` = ?, `cover_url` = ?, `description` = ? WHERE `performer_id` = ?";
    private static final String DELETE_PERFORMER_QUERY = "DELETE FROM `performer` WHERE `performer_id` = ?;";
    private static final String FIND_PERFORMER_BY_NAME_QUERY = "SELECT * FROM `performer` WHERE `performer_name` = ?;";
    private static final String FIND_PERFORMER_BY_ID_QUERY = "SELECT * FROM `performer` WHERE `performer_id` = ?;";
    private static final String FIND_TRACKS_IDS_QUERY = "SELECT * FROM `track_performer` WHERE `performer_id` = ?;";
    private static final String FIND_ALBUMS_IDS_QUERY = "SELECT * FROM `album_performer` WHERE `performer_id` = ?;";
    private static final String COUNT_PERFORMERS_QUERY = "SELECT COUNT(*) FROM `performer` WHERE `performer_name` LIKE ?";
    private static final String STATUS_SPECIFICATION = " AND `status_id` = ?";
    private static final String SEARCH_PERFORMER_BY_NAME_QUERY = "SELECT * FROM `performer` WHERE `performer_name` LIKE ?";
    private static final String SEARCH_PAGE_SPECIFICATION = " LIMIT ? OFFSET ?";
    private static final String ADD_PERFORMER_CREATOR = "INSERT INTO `performers_by_user` (`performer_id`, `user_id`) VALUES (?, ?);";
    private static final String GET_PERFORMER_CREATOR = "SELECT * FROM `performers_by_user` WHERE `performer_id` = ?;";
    private static final String GET_CREATOR_PERFORMERS = "SELECT * FROM `performers_by_user` WHERE `user_id` = ?;";
    private static final String COMPLEX_QUERY_END = ";";

    public PerformerDAO(ConnectionManager manager){
        super(manager);
    }

    @Override
    public Performer save(Performer performer) throws DAOException {
        Connection connection = null;
        PreparedStatement savePreparedStatement = null;
        try {
            connection = getConnection(true);
            savePreparedStatement = connection.prepareStatement(SAVE_PERFORMER_QUERY);
            savePreparedStatement.setString(1, performer.getName());
            savePreparedStatement.setString(2, performer.getDescription());
            savePreparedStatement.setString(3, performer.getCoverImagePath());
            savePreparedStatement.setInt(4, DAOConstants.NON_VERIFIED_ID);
            int result = savePreparedStatement.executeUpdate();
            if (result == 0){
                logger.error("Error while trying to create performer in db ");
                throw new DAOException("Troubles inserting performer into db", 201);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to create performer in db " + e.getSQLState());
            throw new DAOException(e, 200);
        } finally {
            close(savePreparedStatement);
            retrieve(connection);
        }
        return find(performer);
    }

    @Override
    public Performer find(Performer performer) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet performerFromDB = null;
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(FIND_PERFORMER_BY_NAME_QUERY);
            findPreparedStatement.setString(1, performer.getName());
            performerFromDB = findPreparedStatement.executeQuery();
            if (performerFromDB.next()){
                performer = new Performer(performerFromDB.getInt(1), performerFromDB.getString(2),
                        performerFromDB.getString(3), performerFromDB.getString(4),
                        performerFromDB.getInt(5));
            } else {
                throw new DAOException("No such performer", 202);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to find performer in db " + e.getSQLState());
            throw new DAOException(e, 200);
        } finally {
            close(findPreparedStatement);
            close(performerFromDB);
            retrieve(connection);
        }
        return performer;
    }

    @Override
    public Performer findById(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet trackFromDB = null;
        Performer performer;
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(FIND_PERFORMER_BY_ID_QUERY);
            findPreparedStatement.setInt(1, id);
            trackFromDB = findPreparedStatement.executeQuery();
            if (trackFromDB.next()){
                performer = new Performer(id, trackFromDB.getString(2),
                        trackFromDB.getString(3), trackFromDB.getString(4),
                        trackFromDB.getInt(5));
            } else {
                throw new DAOException("No such performer", 202);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to create performer by id in db " + e.getSQLState());
            throw new DAOException(e, 200);
        } finally {
            close(findPreparedStatement);
            close(trackFromDB);
            retrieve(connection);
        }
        return performer;
    }

    @Override
    public void loadPerformerAlbums(Performer performer) throws DAOException {
        performer.setPerformerAlbums(getPerformerAlbumsById(performer.getId()));
    }

    @Override
    public void loadPerformerTracks(Performer performer) throws DAOException {
        performer.setPerformerTracks(getPerformerTracksById(performer.getId()));
    }

    @Override
    public void arePerformersExist(List<Integer> performersIds) throws DAOException {
        for (Integer performerId : performersIds){
            findById(performerId);
        }
    }

    @Override
    public void delete(Performer performer) throws DAOException {
        Connection connection = null;
        PreparedStatement deletePreparedStatement = null;
        try {
            connection = getConnection(true);
            deletePreparedStatement = connection.prepareStatement(DELETE_PERFORMER_QUERY);
            deletePreparedStatement.setInt(1, performer.getId());
            int result = deletePreparedStatement.executeUpdate();
            if (result == 0){
                logger.error("Error while trying to delete performer from db");
                throw new DAOException("Troubles with deleting performer from db", 205);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to delete performer from db " + e.getSQLState());
            throw new DAOException(e, 200);
        } finally {
            close(deletePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public void updatePerformer(Integer performerId, String performerName, String coverPath, String performerDescription) throws DAOException {
        Connection connection = null;
        PreparedStatement updatePreparedStatement = null;
        try {
            connection = getConnection(true);
            updatePreparedStatement = connection.prepareStatement(UPDATE_PERFORMER_QUERY);
            updatePreparedStatement.setString(1, performerName);
            updatePreparedStatement.setString(2, coverPath);
            updatePreparedStatement.setString(3, performerDescription);
            updatePreparedStatement.setInt(4, performerId);
            int result = updatePreparedStatement.executeUpdate();
            if (result == 0) {
                logger.error("Error while trying to update performer in db");
                throw new DAOException("Troubles updating performer", 203);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to update performer in db " + e.getSQLState());
            throw new DAOException(e, 200);
        } finally {
            close(updatePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public void updatePerformerStatus(Integer performerId, int statusId) throws DAOException {
        Connection connection = null;
        PreparedStatement updatePreparedStatement = null;
        try {
            connection = getConnection(true);
            updatePreparedStatement = connection.prepareStatement(UPDATE_PERFORMER_STATUS_QUERY);
            updatePreparedStatement.setInt(1, statusId);
            updatePreparedStatement.setInt(2, performerId);
            int result = updatePreparedStatement.executeUpdate();
            if (result == 0) {
                logger.error("Error while trying to update performer status in db");
                throw new DAOException("Troubles updating performer status", 204);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to update performer status in db " + e.getSQLState());
            throw new DAOException(e, 200);
        } finally {
            close(updatePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public List<Track> getPerformerTracksById(Integer id) throws DAOException {
        Connection connection = null;
        PreparedStatement performersIDsPreparedStatement = null;
        ResultSet performersIDs = null;
        TrackDAO trackDAO = DAOFactory.getInstance().getTrackDAO();
        List<Track> tracks = new ArrayList<>();
        try {
            connection = getConnection(true);
            performersIDsPreparedStatement = connection.prepareStatement(FIND_TRACKS_IDS_QUERY);
            performersIDsPreparedStatement.setInt(1, id);
            performersIDs = performersIDsPreparedStatement.executeQuery();
            while (performersIDs.next()){
                int performerID = performersIDs.getInt(1);
                tracks.add(trackDAO.findById(performerID));
            }
            trackDAO.loadTracksAlbums(tracks);
        } catch (SQLException e) {
            logger.error("SQL error while trying to get performer tracks from db " + e.getSQLState());
            throw new DAOException(e, 200);
        } finally {
            close(performersIDsPreparedStatement);
            close(performersIDs);
            retrieve(connection);
        }
        return tracks;
    }

    @Override
    public List<Album> getPerformerAlbumsById(Integer id) throws DAOException {
        Connection connection = null;
        PreparedStatement albumsIDsPreparedStatement = null;
        ResultSet albumsIDs = null;
        AlbumDAO albumDAO = DAOFactory.getInstance().getAlbumDAO();
        List<Album> albums = new ArrayList<>();
        try {
            connection = getConnection(true);
            albumsIDsPreparedStatement = connection.prepareStatement(FIND_ALBUMS_IDS_QUERY);
            albumsIDsPreparedStatement.setInt(1, id);
            albumsIDs = albumsIDsPreparedStatement.executeQuery();
            while (albumsIDs.next()){
                int albumID = albumsIDs.getInt(1);
                albums.add(albumDAO.findById(albumID));
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to get performer albums in db " + e.getSQLState());
            throw new DAOException(e, 200);
        } finally {
            close(albumsIDsPreparedStatement);
            close(albumsIDs);
            retrieve(connection);
        }
        return albums;
    }

    @Override
    public List<Performer> searchPerformersByName(String name, Integer statusId, Integer limit, Integer offset) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet tracksFromDB = null;
        List<Performer> performers = new ArrayList<>();
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                findPreparedStatement = connection.prepareStatement(SEARCH_PERFORMER_BY_NAME_QUERY + SEARCH_PAGE_SPECIFICATION + COMPLEX_QUERY_END);
                findPreparedStatement.setInt(2, limit);
                findPreparedStatement.setInt(3, offset);
            } else {
                findPreparedStatement = connection.prepareStatement(SEARCH_PERFORMER_BY_NAME_QUERY + STATUS_SPECIFICATION + SEARCH_PAGE_SPECIFICATION + COMPLEX_QUERY_END);
                findPreparedStatement.setInt(2, statusId);
                findPreparedStatement.setInt(3, limit);
                findPreparedStatement.setInt(4, offset);
            }
            findPreparedStatement.setString(1, name);
            tracksFromDB = findPreparedStatement.executeQuery();
            while (tracksFromDB.next()){
                performers.add(new Performer(tracksFromDB.getInt(1), tracksFromDB.getString(2),
                        tracksFromDB.getString(3), tracksFromDB.getString(4),
                        tracksFromDB.getInt(5)));
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to search performer in db " + e.getSQLState());
            throw new DAOException(e, 200);
        } finally {
            close(findPreparedStatement);
            close(tracksFromDB);
            retrieve(connection);
        }
        return performers;
    }

    @Override
    public Integer countPerformers(String name, int statusId) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet countRS = null;
        Integer count = null;
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                findPreparedStatement = connection.prepareStatement(COUNT_PERFORMERS_QUERY + COMPLEX_QUERY_END);
            } else {
                findPreparedStatement = connection.prepareStatement(COUNT_PERFORMERS_QUERY + STATUS_SPECIFICATION + COMPLEX_QUERY_END);
                findPreparedStatement.setInt(2, statusId);
            }
            findPreparedStatement.setString(1, name);
            countRS = findPreparedStatement.executeQuery();
            if (countRS.next()){
                count = countRS.getInt(1);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to count performers in db " + e.getSQLState());
            throw new DAOException(e, 200);
        } finally {
            close(findPreparedStatement);
            close(countRS);
            retrieve(connection);
        }
        return count;
    }

    @Override
    public Integer getCreatorId(Integer performerId) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet resultSet = null;
        Integer id = null;
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(GET_PERFORMER_CREATOR);
            findPreparedStatement.setInt(1, performerId);
            resultSet = findPreparedStatement.executeQuery();
            if (resultSet.next()){
                id = resultSet.getInt(2);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to get performer creator from db " + e.getSQLState());
            throw new DAOException(e, 200);
        } finally {
            close(findPreparedStatement);
            close(resultSet);
            retrieve(connection);
        }
        return id;
    }

    @Override
    public List<Performer> getCreatorPerformers(Integer userId) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet resultSet = null;
        List<Performer> performers = new ArrayList<>();
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(GET_CREATOR_PERFORMERS);
            findPreparedStatement.setInt(1, userId);
            resultSet = findPreparedStatement.executeQuery();
            while (resultSet.next()){
                performers.add(findById(resultSet.getInt(1)));
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to get creator performers from db " + e.getSQLState());
            throw new DAOException(e, 200);
        } finally {
            close(findPreparedStatement);
            close(resultSet);
            retrieve(connection);
        }
        return performers;
    }

    @Override
    public void addCreatorId(Integer performerId, Integer userId) throws DAOException {
        Connection connection = null;
        PreparedStatement addPreparedStatement = null;
        try {
            connection = getConnection(true);
            addPreparedStatement = connection.prepareStatement(ADD_PERFORMER_CREATOR);
            addPreparedStatement.setInt(1, performerId);
            addPreparedStatement.setInt(2, userId);
            int result = addPreparedStatement.executeUpdate();
            if (result == 0){
                logger.error("Error while trying to add performer creator in db");
                throw new DAOException("Troubles with adding performer-creator to db", 206);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to add performer creator in db " + e.getSQLState());
            throw new DAOException(e, 200);
        } finally {
            close(addPreparedStatement);
            retrieve(connection);
        }
    }
}
