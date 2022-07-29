package dao.impl;

import dao.DAOFactory;
import dao.connection.ConnectionManager;
import dao.exception.DAOException;
import dao.impl.abstraction.AbstractDAO;
import dao.impl.abstraction.IAlbumDAO;
import entity.Album;
import entity.Performer;
import entity.Track;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AlbumDAO extends AbstractDAO implements IAlbumDAO {

    private static final String SAVE_ALBUM_QUERY = "INSERT INTO `album` (`album_name`, `descr_file_path`, `cover_file_path`, `status_id`) VALUES (?, ?, ?, ?)";
    private static final String UPDATE_ALBUM_STATUS_QUERY = "UPDATE `album` SET `status_id` = ? WHERE `album_id` = ?";
    private static final String UPDATE_ALBUM_NAME_QUERY = "UPDATE `album` SET `album_name` = ? WHERE `album_id` = ?";
    private static final String DELETE_ALBUM_QUERY = "DELETE FROM `album` WHERE `album_id` = ?;";
    private static final String FIND_ALBUM_BY_NAME_QUERY = "SELECT * FROM `album` WHERE `album_name` = ?;";
    private static final String FIND_ALBUM_BY_ID_QUERY = "SELECT * FROM `album` WHERE `album_id` = ?;";
    private static final String FIND_TRACKS_IDS_QUERY = "SELECT `track_id` FROM `track_album` WHERE `album_id` = ?;";
    private static final String FIND_PERFORMERS_IDS_QUERY = "SELECT `performer_id` FROM `album_performer` WHERE `album_id` = ?;";
    private static final String COUNT_ALBUMS_QUERY = "SELECT COUNT(*) FROM `album`";
    private static final String COUNT_STATUS_SPECIFICATION = " WHERE `status_id` = ?";
    private static final String SEARCH_ALBUM_BY_NAME_QUERY = "SELECT * FROM `album` WHERE `album_name` LIKE ?";
    private static final String SEARCH_STATUS_SPECIFICATION = " AND `status_id` = ?";
    private static final String SEARCH_PAGE_SPECIFICATION = " LIMIT 20 OFFSET ?";
    private static final String ADD_PERFORMER_QUERY = "INSERT INTO `album_performer` (`album_id`, `performer_id`) VALUES (?, ?)";
    private static final String DELETE_PERFORMERS_QUERY = "DELETE FROM `album_performer` WHERE `album_id` = ?";

    public AlbumDAO(ConnectionManager manager){
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
            savePreparedStatement.setString(2, album.getDescriptionFilePath());
            savePreparedStatement.setString(3, album.getCoverImagePath());
            savePreparedStatement.setInt(4, DAOConstants.NON_VERIFIED_ID);
            int result = savePreparedStatement.executeUpdate();
            if (result == 0){
                throw new DAOException("Troubles inserting album into db", 311);
            }
        } catch (SQLException e){
            throw new DAOException(e, 310);
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
            if (performerFromDB.next()){
                album = new Album(performerFromDB.getInt(1), performerFromDB.getString(2),
                        performerFromDB.getString(3), performerFromDB.getString(4),
                        performerFromDB.getInt(5));
            } else {
                throw new DAOException("No such album", 322);
            }
        } catch (SQLException e){
            throw new DAOException(e, 320);
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
        Album album = null;
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(FIND_ALBUM_BY_ID_QUERY);
            findPreparedStatement.setInt(1, id);
            trackFromDB = findPreparedStatement.executeQuery();
            if (trackFromDB.next()){
                album = new Album(id, trackFromDB.getString(2),
                        trackFromDB.getString(3), trackFromDB.getString(4),
                        trackFromDB.getInt(5));
            } else {
                throw new DAOException("No such album", 321);
            }
        } catch (SQLException e){
            throw new DAOException(e, 320);
        } finally {
            close(findPreparedStatement);
            close(trackFromDB);
            retrieve(connection);
        }
        return album;
    }

    @Override
    public Album updateParameter(Integer albumId, String query, Object newValue) throws DAOException {
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
            updatePreparedStatement.setInt(2, albumId);
            int result = updatePreparedStatement.executeUpdate();
            if (result == 0) {
                throw new DAOException("Troubles updating parameter", 341);
            }
        } catch (SQLException e){
            throw new DAOException(e, 340);
        } finally {
            close(updatePreparedStatement);
            retrieve(connection);
        }
        return findById(albumId);
    }

    @Override
    public Album updateAlbumStatus(Integer albumId, int statusId) throws DAOException {
        return updateParameter(albumId, UPDATE_ALBUM_STATUS_QUERY, statusId);
    }

    @Override
    public Album updateAlbumName(Integer albumId, String albumName) throws DAOException {
        return updateParameter(albumId, UPDATE_ALBUM_NAME_QUERY, albumName);
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
            if (result == 0){
                throw new DAOException("Troubles with deleting album from db", 331);
            }
        } catch (SQLException e){
            throw new DAOException(e, 330);
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
            while (performersIDs.next()){
                int performerID = performersIDs.getInt(1);
                tracks.add(trackDAO.findById(performerID));
            }
        } catch (SQLException e) {
            throw new DAOException(e, 350);
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
            while (performersIDs.next()){
                int performerID = performersIDs.getInt(1);
                performers.add(performerDAO.findById(performerID));
            }
        } catch (SQLException e) {
            throw new DAOException(e, 370);
        } finally {
            close(performersIDsPreparedStatement);
            close(performersIDs);
            retrieve(connection);
        }
        return performers;
    }

    @Override
    public Album addAlbumPerformersByIds(Integer albumId, List<Integer> performersIds) throws DAOException {
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
        } catch (SQLException e){
            throw new DAOException(e, 470);
        } finally {
            for (PreparedStatement statement : addPreparedStatements) {
                close(statement);
            }
            retrieve(connection);
        }
        return findById(albumId);
    }

    @Override
    public void clearAlbumPerformers(Integer albumId) throws DAOException {
        Connection connection = null;
        PreparedStatement deletePreparedStatement = null;
        try {
            connection = getConnection(true);
            deletePreparedStatement = connection.prepareStatement(DELETE_PERFORMERS_QUERY);
            deletePreparedStatement.setInt(1, albumId);
            int result = deletePreparedStatement.executeUpdate();
            if (result == 0){
                throw new DAOException("Troubles with deleting track from db", 471);
            }
        } catch (SQLException e){
            throw new DAOException(e, 470);
        } finally {
            close(deletePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public List<Album> searchAlbumsByName(String name, Integer statusId, Integer pageOffset) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet albumsFromDB = null;
        List<Album> albums = new ArrayList<>();
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                findPreparedStatement = connection.prepareStatement(SEARCH_ALBUM_BY_NAME_QUERY + SEARCH_PAGE_SPECIFICATION);
                findPreparedStatement.setInt(2, pageOffset);
            } else {
                findPreparedStatement = connection.prepareStatement(SEARCH_ALBUM_BY_NAME_QUERY + SEARCH_STATUS_SPECIFICATION + SEARCH_STATUS_SPECIFICATION);
                findPreparedStatement.setInt(2, statusId);
                findPreparedStatement.setInt(3, pageOffset);
            }
            findPreparedStatement.setString(1, name);
            albumsFromDB = findPreparedStatement.executeQuery();
            while (albumsFromDB.next()){
                albums.add(new Album(albumsFromDB.getInt(1), albumsFromDB.getString(2),
                        albumsFromDB.getString(3), albumsFromDB.getString(4),
                        albumsFromDB.getInt(5)));
            }
        } catch (SQLException e){
            throw new DAOException(e, 380);
        } finally {
            close(findPreparedStatement);
            close(albumsFromDB);
            retrieve(connection);
        }
        return albums;
    }

    @Override
    public Integer countAlbums(Integer statusId) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet countRS = null;
        Integer count = null;
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                findPreparedStatement = connection.prepareStatement(COUNT_ALBUMS_QUERY);
            } else {
                findPreparedStatement = connection.prepareStatement(COUNT_ALBUMS_QUERY + COUNT_STATUS_SPECIFICATION);
                findPreparedStatement.setInt(1, statusId);
            }
            countRS = findPreparedStatement.executeQuery();
            if (countRS.next()){
                count = countRS.getInt(1);
            }
        } catch (SQLException e){
            throw new DAOException(e, 390);
        } finally {
            close(findPreparedStatement);
            retrieve(connection);
        }
        return count;
    }

    @Override
    public Album loadAlbumDependencies(Album album) throws DAOException {
        album.setAlbumPerformers(getAlbumPerformersById(album.getId()));
        album.setAlbumTracks(getAlbumTracksById(album.getId()));
        return album;
    }

    @Override
    public List<Album> loadAlbumsDependencies(List<Album> albums) throws DAOException {
        for (Album album : albums){
            loadAlbumDependencies(album);
        }
        return albums;
    }

    @Override
    public Album addAlbumPerformerById(Integer albumId, Integer performerId) throws DAOException {
        Connection connection = null;
        PreparedStatement addPreparedStatement = null;
        try {
            connection = getConnection(true);
            addPreparedStatement = connection.prepareStatement(ADD_PERFORMER_QUERY);
            addPreparedStatement.setInt(1, albumId);
            addPreparedStatement.setInt(2, performerId);
            int result = addPreparedStatement.executeUpdate();
            if (result == 0){
                throw new DAOException("Troubles with adding track to db", 371);
            }
        } catch (SQLException e){
            throw new DAOException(e, 370);
        } finally {
            close(addPreparedStatement);
            retrieve(connection);
        }
        return findById(albumId);
    }
}
