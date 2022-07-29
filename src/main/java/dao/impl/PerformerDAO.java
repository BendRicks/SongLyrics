package dao.impl;

import dao.DAOFactory;
import dao.connection.ConnectionManager;
import dao.exception.DAOException;
import dao.impl.abstraction.AbstractDAO;
import dao.impl.abstraction.IPerformerDAO;
import entity.Album;
import entity.Performer;
import entity.Track;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PerformerDAO extends AbstractDAO implements IPerformerDAO {

    private static final String SAVE_PERFORMER_QUERY = "INSERT INTO `performer` (`performer_name`, `descr_file_path`, `cover_file_path`, `status_id`) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_PERFORMER_STATUS_QUERY = "UPDATE `performer` SET `status_id` = ? WHERE `performer_id` = ?";
    private static final String UPDATE_PERFORMER_NAME_QUERY = "UPDATE `performer` SET `performer_name` = ? WHERE `performer_id` = ?";
    private static final String DELETE_PERFORMER_QUERY = "DELETE FROM `performer` WHERE `performer_id` = ?;";
    private static final String FIND_PERFORMER_BY_NAME_QUERY = "SELECT * FROM `performer` WHERE `performer_name` = ?;";
    private static final String FIND_PERFORMER_BY_ID_QUERY = "SELECT * FROM `performer` WHERE `performer_id` = ?;";
    private static final String FIND_TRACKS_IDS_QUERY = "SELECT * FROM `track_performer` WHERE `performer_id` = ?;";
    private static final String FIND_ALBUMS_IDS_QUERY = "SELECT * FROM `album_performer` WHERE `performer_id` = ?;";
    private static final String COUNT_PERFORMERS_QUERY = "SELECT COUNT(*) FROM `performer`";
    private static final String COUNT_STATUS_SPECIFICATION = " WHERE `status_id` = ?";
    private static final String SEARCH_PERFORMER_BY_NAME_QUERY = "SELECT * FROM `performer` WHERE `performer_name` LIKE ?";
    private static final String SEARCH_STATUS_SPECIFICATION = " AND `status_id` = ?";
    private static final String SEARCH_PAGE_SPECIFICATION = " LIMIT 20 OFFSET ?";

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
            savePreparedStatement.setString(2, performer.getDescriptionFilePath());
            savePreparedStatement.setString(3, performer.getCoverImagePath());
            savePreparedStatement.setInt(4, DAOConstants.NON_VERIFIED_ID);
            int result = savePreparedStatement.executeUpdate();
            if (result == 0){
                throw new DAOException("Troubles inserting performer into db", 211);
            }
        } catch (SQLException e){
            throw new DAOException(e, 210);
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
                throw new DAOException("No such performer", 222);
            }
        } catch (SQLException e){
            throw new DAOException(e, 220);
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
        Performer performer = null;
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
                throw new DAOException("No such performer", 221);
            }
        } catch (SQLException e){
            throw new DAOException(e, 220);
        } finally {
            close(findPreparedStatement);
            close(trackFromDB);
            retrieve(connection);
        }
        return performer;
    }

    @Override
    public Performer loadPerformerDependencies(Performer performer) throws DAOException {
        performer.setPerformerAlbums(getPerformerAlbumsById(performer.getId()));
        performer.setPerformerTracks(getPerformerTracksById(performer.getId()));
        return performer;
    }

    @Override
    public List<Performer> loadPerformersDependencies(List<Performer> performers) throws DAOException {
        for (Performer performer : performers){
            loadPerformerDependencies(performer);
        }
        return performers;
    }

    @Override
    public Performer updateParameter(Integer performerId, String query, Object newValue) throws DAOException {
        Connection connection = null;
        PreparedStatement updatePreparedStatement = null;
        try {
            connection = getConnection(true);
            updatePreparedStatement = connection.prepareStatement(query);
            if (newValue.getClass() == Integer.class){
                updatePreparedStatement.setInt(1, (Integer)newValue);
            } else if (newValue.getClass() == String.class) {
                updatePreparedStatement.setString(1, (String)newValue);
            }
            updatePreparedStatement.setInt(2, performerId);
            int result = updatePreparedStatement.executeUpdate();
            if (result == 0) {
                throw new DAOException("Troubles updating parameter", 241);
            }
        } catch (SQLException e){
            throw new DAOException(e, 240);
        } finally {
            close(updatePreparedStatement);
            retrieve(connection);
        }
        return findById(performerId);
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
                throw new DAOException("Troubles with deleting performer from db", 231);
            }
        } catch (SQLException e){
            throw new DAOException(e, 230);
        } finally {
            close(deletePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public Performer updatePerformerStatus(Integer performerId, int statusId) throws DAOException {
        return updateParameter(performerId, UPDATE_PERFORMER_STATUS_QUERY, statusId);
    }

    @Override
    public Performer updatePerformerName(Integer performerId, String performerName) throws DAOException {
        return updateParameter(performerId, UPDATE_PERFORMER_NAME_QUERY, performerName);
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
                int performerID = performersIDs.getInt(2);
                tracks.add(trackDAO.findById(performerID));
            }
        } catch (SQLException e) {
            throw new DAOException(e, 250);
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
                int albumID = albumsIDs.getInt(2);
                albums.add(albumDAO.findById(albumID));
            }
        } catch (SQLException e) {
            throw new DAOException(e, 260);
        } finally {
            close(albumsIDsPreparedStatement);
            close(albumsIDs);
            retrieve(connection);
        }
        return albums;
    }

    @Override
    public List<Performer> searchPerformersByName(String name, Integer statusId, Integer pageOffset) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet tracksFromDB = null;
        List<Performer> performers = new ArrayList<>();
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                findPreparedStatement = connection.prepareStatement(SEARCH_PERFORMER_BY_NAME_QUERY + SEARCH_PAGE_SPECIFICATION);
                findPreparedStatement.setInt(2, pageOffset);
            } else {
                findPreparedStatement = connection.prepareStatement(SEARCH_PERFORMER_BY_NAME_QUERY + SEARCH_STATUS_SPECIFICATION + SEARCH_PAGE_SPECIFICATION);
                findPreparedStatement.setInt(2, statusId);
                findPreparedStatement.setInt(3, pageOffset);
            }
            findPreparedStatement.setString(1, name);
            tracksFromDB = findPreparedStatement.executeQuery();
            while (tracksFromDB.next()){
                performers.add(new Performer(tracksFromDB.getInt(1), tracksFromDB.getString(2),
                        tracksFromDB.getString(3), tracksFromDB.getString(4),
                        tracksFromDB.getInt(5)));
            }
        } catch (SQLException e){
            throw new DAOException(e, 280);
        } finally {
            close(findPreparedStatement);
            close(tracksFromDB);
            retrieve(connection);
        }
        return performers;
    }

    @Override
    public Integer countPerformers(Integer statusId) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet countRS = null;
        Integer count = null;
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                findPreparedStatement = connection.prepareStatement(COUNT_PERFORMERS_QUERY);
            } else {
                findPreparedStatement = connection.prepareStatement(COUNT_PERFORMERS_QUERY + COUNT_STATUS_SPECIFICATION);
                findPreparedStatement.setInt(1, statusId);
            }
            countRS = findPreparedStatement.executeQuery();
            if (countRS.next()){
                count = countRS.getInt(1);
            }
        } catch (SQLException e){
            throw new DAOException(e, 290);
        } finally {
            close(findPreparedStatement);
            retrieve(connection);
        }
        return count;
    }

}
