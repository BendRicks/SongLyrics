package dao.impl;

import dao.DAOConstants;
import dao.DAOFactory;
import dao.connection.ConnectionManager;
import dao.exception.DAOException;
import dao.impl.abstraction.AbstractDAO;
import dao.impl.abstraction.IAlbumDAO;
import entity.Album;
import entity.Performer;
import entity.Track;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO extends AbstractDAO implements IAlbumDAO {

    private final Logger logger = LogManager.getLogger(AlbumDAO.class);
    private static final String SAVE_ALBUM_QUERY = "INSERT INTO `album` (`album_name`, `description`, `cover_url`, `status_id`) VALUES (?, ?, ?, ?);";
    private static final String UPDATE_ALBUM_STATUS_QUERY = "UPDATE `album` SET `status_id` = ? WHERE `album_id` = ?";
    private static final String UPDATE_ALBUM_QUERY = "UPDATE `album` SET `album_name` = ?, `cover_url` = ?, `description` = ? WHERE `album_id` = ?";
    private static final String DELETE_ALBUM_QUERY = "DELETE FROM `album` WHERE `album_id` = ?;";
    private static final String FIND_ALBUM_BY_NAME_QUERY = "SELECT * FROM `album` WHERE `album_name` = ?;";
    private static final String FIND_ALBUM_BY_ID_QUERY = "SELECT * FROM `album` WHERE `album_id` = ?;";
    private static final String FIND_TRACKS_IDS_QUERY = "SELECT `track_id` FROM `track_album` WHERE `album_id` = ?;";
    private static final String FIND_PERFORMERS_IDS_QUERY = "SELECT `performer_id` FROM `album_performer` WHERE `album_id` = ?;";
    private static final String COUNT_ALBUMS_QUERY = "SELECT COUNT(*) FROM `album` WHERE `album_name` LIKE ?";
    private static final String SEARCH_ALBUM_BY_NAME_QUERY = "SELECT * FROM `album` WHERE `album_name` LIKE ?";
    private static final String STATUS_SPECIFICATION = " AND `status_id` = ?";
    private static final String SEARCH_PAGE_SPECIFICATION = " LIMIT ? OFFSET ?";
    private static final String ADD_PERFORMER_QUERY = "INSERT INTO `album_performer` (`album_id`, `performer_id`) VALUES (?, ?);";
    private static final String DELETE_PERFORMERS_QUERY = "DELETE FROM `album_performer` WHERE `album_id` = ?;";
    private static final String ADD_ALBUM_CREATOR = "INSERT INTO `albums_by_user` (`album_id`, `user_id`) VALUES (?, ?);";
    private static final String GET_ALBUM_CREATOR = "SELECT * FROM `albums_by_user` WHERE `album_id` = ?;";
    private static final String GET_CREATOR_ALBUMS = "SELECT * FROM `albums_by_user` WHERE `user_id` = ?;";
    private static final String COMPLEX_QUERY_END = ";";

    public AlbumDAO(ConnectionManager manager) {
        super(manager);
    }

    @Override
    public Album save(Album album) throws DAOException {
        Connection connection = null;
        PreparedStatement savePreparedStatement = null;
        try {
            connection = getConnection(true);
            savePreparedStatement = connection.prepareStatement(SAVE_ALBUM_QUERY);
            savePreparedStatement.setString(1, album.getName());
            savePreparedStatement.setString(2, album.getDescription());
            savePreparedStatement.setString(3, album.getCoverImagePath());
            savePreparedStatement.setInt(4, DAOConstants.NON_VERIFIED_ID);
            int result = savePreparedStatement.executeUpdate();
            if (result == 0) {
                logger.error("Error creating album in db");
                throw new DAOException("Troubles inserting album into db", 101);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to create album in db " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            close(savePreparedStatement);
            retrieve(connection);
        }
        return find(album);
    }

    @Override
    public Album find(Album album) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet performerFromDB = null;
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(FIND_ALBUM_BY_NAME_QUERY);
            findPreparedStatement.setString(1, album.getName());
            performerFromDB = findPreparedStatement.executeQuery();
            if (performerFromDB.next()) {
                album = new Album(performerFromDB.getInt(1), performerFromDB.getString(2),
                        performerFromDB.getString(3), performerFromDB.getString(4),
                        performerFromDB.getInt(5));
            } else {
                throw new DAOException("No such album", 102);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to find album in db " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            close(findPreparedStatement);
            close(performerFromDB);
            retrieve(connection);
        }
        return album;
    }

    @Override
    public Album findById(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet trackFromDB = null;
        Album album;
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(FIND_ALBUM_BY_ID_QUERY);
            findPreparedStatement.setInt(1, id);
            trackFromDB = findPreparedStatement.executeQuery();
            if (trackFromDB.next()) {
                album = new Album(id, trackFromDB.getString(2),
                        trackFromDB.getString(3), trackFromDB.getString(4),
                        trackFromDB.getInt(5));
            } else {
                throw new DAOException("No such album", 102);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to find album in db by id " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            close(findPreparedStatement);
            close(trackFromDB);
            retrieve(connection);
        }
        return album;
    }

    @Override
    public void updateAlbum(Integer albumId, String albumName, String coverPath, String albumDescription) throws DAOException {
        Connection connection = null;
        PreparedStatement updatePreparedStatement = null;
        try {
            connection = getConnection(true);
            updatePreparedStatement = connection.prepareStatement(UPDATE_ALBUM_QUERY);
            updatePreparedStatement.setString(1, albumName);
            updatePreparedStatement.setString(2, coverPath);
            updatePreparedStatement.setString(3, albumDescription);
            updatePreparedStatement.setInt(4, albumId);
            int result = updatePreparedStatement.executeUpdate();
            if (result == 0) {
                logger.error("Error while trying to update album in db");
                throw new DAOException("Troubles updating album", 103);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to update album in db " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            close(updatePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public void updateAlbumStatus(Integer albumId, int statusId) throws DAOException {
        Connection connection = null;
        PreparedStatement updatePreparedStatement = null;
        try {
            connection = getConnection(true);
            updatePreparedStatement = connection.prepareStatement(UPDATE_ALBUM_STATUS_QUERY);
            updatePreparedStatement.setInt(1, statusId);
            updatePreparedStatement.setInt(2, albumId);
            int result = updatePreparedStatement.executeUpdate();
            if (result == 0) {
                logger.error("Error while trying to update album status in db ");
                throw new DAOException("Troubles updating album status", 104);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to update album status in db " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            close(updatePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public void updateAlbumPerformersByIds(Integer albumId, List<Integer> performersIds) throws DAOException {
        Connection connection = null;
        PreparedStatement deletePreparedStatement = null;
        List<PreparedStatement> addPreparedStatements = new ArrayList<>();
        try {
            connection = getConnection(false);
            deletePreparedStatement = connection.prepareStatement(DELETE_PERFORMERS_QUERY);
            deletePreparedStatement.setInt(1, albumId);
            deletePreparedStatement.executeUpdate();
            for (Integer performerId : performersIds) {
                PreparedStatement addPreparedStatement = connection.prepareStatement(ADD_PERFORMER_QUERY);
                addPreparedStatements.add(addPreparedStatement);
                addPreparedStatement.setInt(1, albumId);
                addPreparedStatement.setInt(2, performerId);
                addPreparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error("SQL error while trying to update album performers in db " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            close(deletePreparedStatement);
            for (PreparedStatement statement : addPreparedStatements) {
                close(statement);
            }
            retrieve(connection);
        }
    }

    @Override
    public void delete(Album album) throws DAOException {
        Connection connection = null;
        PreparedStatement deletePreparedStatement = null;
        try {
            connection = getConnection(true);
            deletePreparedStatement = connection.prepareStatement(DELETE_ALBUM_QUERY);
            deletePreparedStatement.setInt(1, album.getId());
            int result = deletePreparedStatement.executeUpdate();
            if (result == 0) {
                logger.error("Error while trying to delete album from db");
                throw new DAOException("Troubles with deleting album from db", 105);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to delete album from db " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            close(deletePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public List<Track> getAlbumTracksById(Integer id) throws DAOException {
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
            while (performersIDs.next()) {
                int performerID = performersIDs.getInt(1);
                tracks.add(trackDAO.findById(performerID));
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to get album tracks from db " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            close(performersIDsPreparedStatement);
            close(performersIDs);
            retrieve(connection);
        }
        return tracks;
    }

    @Override
    public List<Performer> getAlbumPerformersById(Integer id) throws DAOException {
        Connection connection = null;
        PreparedStatement performersIDsPreparedStatement = null;
        ResultSet performersIDs = null;
        PerformerDAO performerDAO = DAOFactory.getInstance().getPerformerDAO();
        List<Performer> performers = new ArrayList<>();
        try {
            connection = getConnection(true);
            performersIDsPreparedStatement = connection.prepareStatement(FIND_PERFORMERS_IDS_QUERY);
            performersIDsPreparedStatement.setInt(1, id);
            performersIDs = performersIDsPreparedStatement.executeQuery();
            while (performersIDs.next()) {
                int performerID = performersIDs.getInt(1);
                performers.add(performerDAO.findById(performerID));
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to get album performers from db " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            close(performersIDsPreparedStatement);
            close(performersIDs);
            retrieve(connection);
        }
        return performers;
    }

    @Override
    public void areAlbumsExist(List<Integer> albumsIds) throws DAOException {
        for (Integer albumID : albumsIds) {
            findById(albumID);
        }
    }

    @Override
    public void addAlbumPerformersByIds(Integer albumId, List<Integer> performersIds) throws DAOException {
        Connection connection = null;
        List<PreparedStatement> addPreparedStatements = new ArrayList<>();
        try {
            connection = getConnection(false);
            for (Integer performerId : performersIds) {
                PreparedStatement addPreparedStatement = connection.prepareStatement(ADD_PERFORMER_QUERY);
                addPreparedStatements.add(addPreparedStatement);
                addPreparedStatement.setInt(1, albumId);
                addPreparedStatement.setInt(2, performerId);
                addPreparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error("SQL error while trying to get album tracks from db " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            for (PreparedStatement statement : addPreparedStatements) {
                close(statement);
            }
            retrieve(connection);
        }
    }

    @Override
    public List<Album> searchAlbumsByName(String name, Integer statusId, Integer limit, Integer offset) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet albumsFromDB = null;
        List<Album> albums = new ArrayList<>();
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                findPreparedStatement = connection.prepareStatement(SEARCH_ALBUM_BY_NAME_QUERY + SEARCH_PAGE_SPECIFICATION + COMPLEX_QUERY_END);
                findPreparedStatement.setInt(2, limit);
                findPreparedStatement.setInt(3, offset);
            } else {
                findPreparedStatement = connection.prepareStatement(SEARCH_ALBUM_BY_NAME_QUERY + STATUS_SPECIFICATION + SEARCH_PAGE_SPECIFICATION + COMPLEX_QUERY_END);
                findPreparedStatement.setInt(2, statusId);
                findPreparedStatement.setInt(3, limit);
                findPreparedStatement.setInt(4, offset);
            }
            findPreparedStatement.setString(1, name);
            albumsFromDB = findPreparedStatement.executeQuery();
            while (albumsFromDB.next()) {
                albums.add(new Album(albumsFromDB.getInt(1), albumsFromDB.getString(2),
                        albumsFromDB.getString(3), albumsFromDB.getString(4),
                        albumsFromDB.getInt(5)));
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to search albums in db " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            close(findPreparedStatement);
            close(albumsFromDB);
            retrieve(connection);
        }
        return albums;
    }

    @Override
    public Integer countAlbums(String name, int statusId) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet countRS = null;
        Integer count = null;
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                findPreparedStatement = connection.prepareStatement(COUNT_ALBUMS_QUERY + COMPLEX_QUERY_END);
            } else {
                findPreparedStatement = connection.prepareStatement(COUNT_ALBUMS_QUERY + STATUS_SPECIFICATION + COMPLEX_QUERY_END);
                findPreparedStatement.setInt(2, statusId);
            }
            findPreparedStatement.setString(1, name);
            countRS = findPreparedStatement.executeQuery();
            if (countRS.next()) {
                count = countRS.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to get count tracks in db " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            close(countRS);
            close(findPreparedStatement);
            retrieve(connection);
        }
        return count;
    }

    @Override
    public void loadAlbumTracks(Album album) throws DAOException {
        album.setAlbumTracks(getAlbumTracksById(album.getId()));
    }

    @Override
    public void loadAlbumPerformers(Album album) throws DAOException {
        album.setAlbumPerformers(getAlbumPerformersById(album.getId()));
    }

    @Override
    public List<Album> loadAlbumsPerformers(List<Album> albums) throws DAOException {
        for (Album album : albums) {
            loadAlbumPerformers(album);
        }
        return albums;
    }

    @Override
    public List<Album> loadAlbumsTracks(List<Album> albums) throws DAOException {
        for (Album album : albums) {
            loadAlbumTracks(album);
        }
        return albums;
    }

    @Override
    public Integer getCreatorId(Integer albumId) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet resultSet = null;
        Integer id = null;
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(GET_ALBUM_CREATOR);
            findPreparedStatement.setInt(1, albumId);
            resultSet = findPreparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt(2);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to get album creator id from db " + e.getSQLState());
            throw new DAOException(e, 100);
        }
        finally {
            retrieve(connection);
            close(resultSet);
            close(findPreparedStatement);
        }
        return id;
    }

    @Override
    public List<Album> getCreatorAlbums(Integer userId) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet resultSet = null;
        List<Album> albums = new ArrayList<>();
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(GET_CREATOR_ALBUMS);
            findPreparedStatement.setInt(1, userId);
            resultSet = findPreparedStatement.executeQuery();
            while (resultSet.next()) {
                albums.add(findById(resultSet.getInt(1)));
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to get creator albums from db " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            retrieve(connection);
            close(resultSet);
            close(findPreparedStatement);
        }
        return albums;
    }

    @Override
    public void addCreatorId(Integer albumId, Integer userId) throws DAOException {
        Connection connection = null;
        PreparedStatement addPreparedStatement = null;
        try {
            connection = getConnection(true);
            addPreparedStatement = connection.prepareStatement(ADD_ALBUM_CREATOR);
            addPreparedStatement.setInt(1, albumId);
            addPreparedStatement.setInt(2, userId);
            int result = addPreparedStatement.executeUpdate();
            if (result == 0) {
                logger.error("Error while trying to add album creator in db");
                throw new DAOException("Troubles with adding album-creator to db", 106);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to add album creator in db " + e.getSQLState());
            throw new DAOException(e, 100);
        } finally {
            close(addPreparedStatement);
            retrieve(connection);
        }
    }
}
