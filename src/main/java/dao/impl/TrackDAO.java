package dao.impl;

import dao.DAOFactory;
import dao.connection.ConnectionManager;
import dao.exception.DAOException;
import dao.impl.abstraction.AbstractDAO;
import dao.impl.abstraction.ITrackDAO;
import entity.Album;
import entity.Performer;
import entity.Track;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TrackDAO extends AbstractDAO implements ITrackDAO {

    private static final String SAVE_TRACK_QUERY = "INSERT INTO `track` (`track_name`, `lyrics_file_path`, `status_id`) VALUES (?, ?, ?);";
    private static final String UPDATE_TRACK_NAME_QUERY = "UPDATE `track` SET `track_name` = ? WHERE `track_id` = ?;";
    private static final String UPDATE_TRACK_STATUS_QUERY = "UPDATE `track` SET `status_id` = ? WHERE `track_id` = ?;";
    private static final String DELETE_TRACK_QUERY = "DELETE FROM `track` WHERE `track_id` = ?;";
    private static final String FIND_TRACK_BY_NAME_QUERY = "SELECT * FROM `track` WHERE `track_name` = ?;";
    private static final String FIND_TRACK_BY_ID_QUERY = "SELECT * FROM `track` WHERE `track_id` = ?;";
    private static final String FIND_PERFORMERS_IDS_QUERY = "SELECT * FROM `track_performer` WHERE `track_id` = ?;";
    private static final String FIND_ALBUMS_IDS_QUERY = "SELECT * FROM `track_album` WHERE `track_id` = ?;";
    private static final String SEARCH_TRACK_BY_NAME_QUERY = "SELECT * FROM `track` WHERE `track_name` LIKE ?;";
    private static final String SEARCH_STATUS_SPECIFICATION = " AND `status_id` = ?;";
    private static final String SEARCH_PAGE_SPECIFICATION = " LIMIT 20 OFFSET ?";
    private static final String COUNT_TRACKS_QUERY = "SELECT COUNT(*) FROM `track`;";
    private static final String COUNT_STATUS_SPECIFICATION = " WHERE `status_id` = ?;";
    private static final String ADD_PERFORMER_QUERY = "INSERT INTO `track_performer` (`track_id`, `performer_id`) VALUES (?, ?)";
    private static final String ADD_ALBUM_QUERY = "INSERT INTO `track_album` (`track_id`, `album_id`) VALUES (?, ?)";
    private static final String DELETE_PERFORMERS_QUERY = "DELETE FROM `track_performer` WHERE `track_id` = ?";
    private static final String DELETE_ALBUMS_QUERY = "DELETE FROM `track_album` WHERE `track_id` = ?";

    public TrackDAO(ConnectionManager manager){
        super(manager);
    }

    @Override
    public Track save(Track track) throws DAOException {
        Connection connection = null;
        PreparedStatement savePreparedStatement = null;
        try {
            connection = getConnection(true);
            savePreparedStatement = connection.prepareStatement(SAVE_TRACK_QUERY);
            savePreparedStatement.setString(1, track.getName());
            savePreparedStatement.setString(2, track.getLyricsFilePath());
            savePreparedStatement.setInt(3, DAOConstants.NON_VERIFIED_ID);
            int result = savePreparedStatement.executeUpdate();
            if (result == 0){
                throw new DAOException("Troubles inserting track into db", 411);
            }
        } catch (SQLException e){
            throw new DAOException(e, 410);
        } finally {
            close(savePreparedStatement);
            retrieve(connection);
        }
        return find(track);
    }

    @Override
    public Track find(Track track) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet trackFromDB = null;
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(FIND_TRACK_BY_NAME_QUERY);
            findPreparedStatement.setString(1, track.getName());
            trackFromDB = findPreparedStatement.executeQuery();
            if (trackFromDB.next()){
                track = new Track(trackFromDB.getInt(1), trackFromDB.getString(2),
                        trackFromDB.getString(3), trackFromDB.getInt(4));
            } else {
                throw new DAOException("No such track", 422);
            }
        } catch (SQLException e){
            throw new DAOException(e, 420);
        } finally {
            close(findPreparedStatement);
            close(trackFromDB);
            retrieve(connection);
        }
        return track;
    }

    public List<Performer> getTrackPerformersById(Integer id) throws DAOException {
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
                int performerID = performersIDs.getInt(2);
                performers.add(performerDAO.findById(performerID));
            }
        } catch (SQLException e) {
            throw new DAOException(e, 470);
        } finally {
            close(performersIDsPreparedStatement);
            close(performersIDs);
            retrieve(connection);
        }
        return performers;
    }

    @Override
    public List<Album> getTrackAlbumsById(Integer id) throws DAOException {
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
            throw new DAOException(e, 460);
        } finally {
            close(albumsIDsPreparedStatement);
            close(albumsIDs);
            retrieve(connection);
        }
        return albums;
    }

    @Override
    public Track findById(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet trackFromDB = null;
        Track track = null;
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(FIND_TRACK_BY_ID_QUERY);
            findPreparedStatement.setInt(1, id);
            trackFromDB = findPreparedStatement.executeQuery();
            if (trackFromDB.next()){
                track = new Track(id, trackFromDB.getString(2),
                        trackFromDB.getString(3), trackFromDB.getInt(4));
            } else {
                throw new DAOException("No such track", 421);
            }
        } catch (SQLException e){
            throw new DAOException(e, 420);
        } finally {
            close(findPreparedStatement);
            close(trackFromDB);
            retrieve(connection);
        }
        return track;
    }

    @Override
    public Track loadTrackDependencies(Track track) throws DAOException {
        track.setTrackAlbums(getTrackAlbumsById(track.getId()));
        track.setTrackPerformers(getTrackPerformersById(track.getId()));
        return track;
    }

    @Override
    public List<Track> loadTracksDependencies(List<Track> tracks) throws DAOException {
        for (Track track : tracks){
            loadTrackDependencies(track);
        }
        return tracks;
    }

    @Override
    public List<Track> searchTracksByName(String name, Integer statusId, Integer pageOffset) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet tracksFromDB = null;
        List<Track> tracks = new ArrayList<>();
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                findPreparedStatement = connection.prepareStatement(SEARCH_TRACK_BY_NAME_QUERY + SEARCH_PAGE_SPECIFICATION);
                findPreparedStatement.setInt(2, pageOffset);
            } else {
                findPreparedStatement = connection.prepareStatement(SEARCH_TRACK_BY_NAME_QUERY + SEARCH_STATUS_SPECIFICATION + SEARCH_PAGE_SPECIFICATION);
                findPreparedStatement.setInt(2, statusId);
                findPreparedStatement.setInt(3, pageOffset);
            }
            findPreparedStatement.setString(1, name);
            tracksFromDB = findPreparedStatement.executeQuery();
            while (tracksFromDB.next()){
                tracks.add(new Track(tracksFromDB.getInt(1), tracksFromDB.getString(2),
                        tracksFromDB.getString(3), tracksFromDB.getInt(4)));
            }
        } catch (SQLException e){
            throw new DAOException(e, 480);
        } finally {
            close(findPreparedStatement);
            close(tracksFromDB);
            retrieve(connection);
        }
        return tracks;
    }

    @Override
    public Integer countTracks(Integer statusId) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet countRS = null;
        Integer count = null;
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                findPreparedStatement = connection.prepareStatement(COUNT_TRACKS_QUERY);
            } else {
                findPreparedStatement = connection.prepareStatement(COUNT_TRACKS_QUERY + COUNT_STATUS_SPECIFICATION);
                findPreparedStatement.setInt(1, statusId);
            }
            countRS = findPreparedStatement.executeQuery();
            if (countRS.next()){
                count = countRS.getInt(1);
            }
        } catch (SQLException e){
            throw new DAOException(e, 490);
        } finally {
            close(findPreparedStatement);
            retrieve(connection);
        }
        return count;
    }

    @Override
    public Track addTrackPerformersByIds(Integer trackId, List<Integer> performersIds) throws DAOException {
        Connection connection = null;
        List<PreparedStatement> addPreparedStatements = new ArrayList<>();
        try {
            connection = getConnection(false);
            for (Integer performerId : performersIds) {
                PreparedStatement addPreparedStatement = connection.prepareStatement(ADD_PERFORMER_QUERY);
                addPreparedStatements.add(addPreparedStatement);
                addPreparedStatement.setInt(1, trackId);
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
        return findById(trackId);
    }

    @Override
    public void clearTrackPerformers(Integer trackId) throws DAOException {
        Connection connection = null;
        PreparedStatement deletePreparedStatement = null;
        try {
            connection = getConnection(true);
            deletePreparedStatement = connection.prepareStatement(DELETE_PERFORMERS_QUERY);
            deletePreparedStatement.setInt(1, trackId);
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
    public Track addTrackAlbumsByIds(Integer trackId, List<Integer> albumsIds) throws DAOException {
        Connection connection = null;
        List<PreparedStatement> addPreparedStatements = new ArrayList<>();
        try {
            connection = getConnection(false);
            for (Integer albumId : albumsIds) {
                PreparedStatement addPreparedStatement = connection.prepareStatement(ADD_ALBUM_QUERY);
                addPreparedStatements.add(addPreparedStatement);
                addPreparedStatement.setInt(1, trackId);
                addPreparedStatement.setInt(2, albumId);
                addPreparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e){
            throw new DAOException(e, 460);
        } finally {
            for (PreparedStatement statement : addPreparedStatements) {
                close(statement);
            }
            retrieve(connection);
        }
        return findById(trackId);
    }

    @Override
    public void clearTrackAlbums(Integer trackId) throws DAOException {
        Connection connection = null;
        PreparedStatement deletePreparedStatement = null;
        try {
            connection = getConnection(true);
            deletePreparedStatement = connection.prepareStatement(DELETE_ALBUMS_QUERY);
            deletePreparedStatement.setInt(1, trackId);
            int result = deletePreparedStatement.executeUpdate();
            if (result == 0){
                throw new DAOException("Troubles with deleting track from db", 461);
            }
        } catch (SQLException e){
            throw new DAOException(e, 460);
        } finally {
            close(deletePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public Track updateParameter(Integer trackId, String query, Object newValue) throws DAOException {
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
            updatePreparedStatement.setInt(2, trackId);
            int result = updatePreparedStatement.executeUpdate();
            if (result == 0) {
                throw new DAOException("Troubles updating parameter", 441);
            }
        } catch (SQLException e){
            throw new DAOException(e, 440);
        } finally {
            close(updatePreparedStatement);
            retrieve(connection);
        }
        return findById(trackId);
    }

    @Override
    public void delete(Track track) throws DAOException {
        Connection connection = null;
        PreparedStatement deletePreparedStatement = null;
        try {
            connection = getConnection(true);
            deletePreparedStatement = connection.prepareStatement(DELETE_TRACK_QUERY);
            deletePreparedStatement.setInt(1, track.getId());
            int result = deletePreparedStatement.executeUpdate();
            if (result == 0){
                throw new DAOException("Troubles with deleting track from db", 431);
            }
        } catch (SQLException e){
            throw new DAOException(e, 430);
        } finally {
            close(deletePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public Track updateTrackStatus(Integer trackId, Integer statusId) throws DAOException{
        return updateParameter(trackId, UPDATE_TRACK_STATUS_QUERY, statusId);
    }

    @Override
    public Track updateTrackName(Integer trackId, String trackName) throws DAOException{
        return updateParameter(trackId, UPDATE_TRACK_NAME_QUERY, trackName);
    }
}
