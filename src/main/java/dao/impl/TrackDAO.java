package dao.impl;

import dao.DAOConstants;
import dao.DAOFactory;
import dao.connection.ConnectionManager;
import dao.exception.DAOException;
import dao.impl.abstraction.AbstractDAO;
import dao.impl.abstraction.ITrackDAO;
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

public class TrackDAO extends AbstractDAO implements ITrackDAO {

    private final Logger logger = LogManager.getLogger(TrackDAO.class);
    private static final String SAVE_TRACK_QUERY = "INSERT INTO `track` (`track_name`, `lyrics`, `status_id`) VALUES (?, ?, ?);";
    private static final String UPDATE_TRACK_STATUS_QUERY = "UPDATE `track` SET `status_id` = ? WHERE `track_id` = ?;";
    private static final String UPDATE_TRACK_QUERY = "UPDATE `track` SET `track_name` = ?, `lyrics` = ? WHERE `track_id` = ?;";
    private static final String DELETE_TRACK_QUERY = "DELETE FROM `track` WHERE `track_id` = ?;";
    private static final String FIND_TRACK_BY_NAME_QUERY = "SELECT * FROM `track` WHERE `track_name` = ?;";
    private static final String FIND_TRACK_BY_ID_QUERY = "SELECT * FROM `track` WHERE `track_id` = ?;";
    private static final String FIND_PERFORMERS_IDS_QUERY = "SELECT * FROM `track_performer` WHERE `track_id` = ?;";
    private static final String FIND_ALBUMS_IDS_QUERY = "SELECT * FROM `track_album` WHERE `track_id` = ?;";
    private static final String SEARCH_TRACK_BY_NAME_QUERY = "SELECT * FROM `track` WHERE `track_name` LIKE ?";
    private static final String STATUS_SPECIFICATION = " AND `status_id` = ?";
    private static final String SEARCH_PAGE_SPECIFICATION = " LIMIT ? OFFSET ?";
    private static final String COUNT_TRACKS_QUERY = "SELECT COUNT(*) FROM `track` WHERE `track_name` LIKE ?";
    private static final String ADD_PERFORMER_QUERY = "INSERT INTO `track_performer` (`track_id`, `performer_id`) VALUES (?, ?);";
    private static final String ADD_ALBUM_QUERY = "INSERT INTO `track_album` (`track_id`, `album_id`) VALUES (?, ?);";
    private static final String DELETE_PERFORMERS_QUERY = "DELETE FROM `track_performer` WHERE `track_id` = ?;";
    private static final String DELETE_ALBUMS_QUERY = "DELETE FROM `track_album` WHERE `track_id` = ?;";
    private static final String ADD_TRACK_CREATOR = "INSERT INTO `tracks_by_user` (`track_id`, `user_id`) VALUES (?, ?);";
    private static final String GET_TRACK_CREATOR = "SELECT * FROM `tracks_by_user` WHERE `track_id` = ?;";
    private static final String GET_CREATOR_TRACKS = "SELECT * FROM `tracks_by_user` WHERE `user_id` = ?;";
    private static final String COMPLEX_QUERY_END = ";";

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
            savePreparedStatement.setString(2, track.getLyrics());
            savePreparedStatement.setInt(3, DAOConstants.NON_VERIFIED_ID);
            int result = savePreparedStatement.executeUpdate();
            if (result == 0){
                logger.error("Error while trying to create track in db ");
                throw new DAOException("Troubles inserting track into db", 301);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to create track in db " + e.getSQLState());
            throw new DAOException(e, 300);
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
                throw new DAOException("No such track", 302);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to find track in db " + e.getSQLState());
            throw new DAOException(e, 300);
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
            logger.error("SQL error while trying to get track performers from db " + e.getSQLState());
            throw new DAOException(e, 300);
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
            logger.error("SQL error while trying to get track albums from db " + e.getSQLState());
            throw new DAOException(e, 300);
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
        Track track;
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(FIND_TRACK_BY_ID_QUERY);
            findPreparedStatement.setInt(1, id);
            trackFromDB = findPreparedStatement.executeQuery();
            if (trackFromDB.next()){
                track = new Track(id, trackFromDB.getString(2),
                        trackFromDB.getString(3), trackFromDB.getInt(4));
            } else {
                throw new DAOException("No such track", 302);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to find track by id in db " + e.getSQLState());
            throw new DAOException(e, 300);
        } finally {
            close(findPreparedStatement);
            close(trackFromDB);
            retrieve(connection);
        }
        return track;
    }

    @Override
    public void loadTrackPerformers(Track track) throws DAOException {
        track.setTrackPerformers(getTrackPerformersById(track.getId()));
    }

    @Override
    public void loadTrackAlbums(Track track) throws DAOException {
        track.setTrackAlbums(getTrackAlbumsById(track.getId()));
    }

    @Override
    public List<Track> loadTracksPerformers(List<Track> tracks) throws DAOException {
        for (Track track : tracks){
            loadTrackPerformers(track);
        }
        return tracks;
    }

    @Override
    public List<Track> loadTracksAlbums(List<Track> tracks) throws DAOException {
        for (Track track : tracks){
            loadTrackAlbums(track);
        }
        return tracks;
    }

    @Override
    public List<Track> searchTracksByName(String name, Integer statusId, Integer limit, Integer offset) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet tracksFromDB = null;
        List<Track> tracks = new ArrayList<>();
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                findPreparedStatement = connection.prepareStatement(SEARCH_TRACK_BY_NAME_QUERY + SEARCH_PAGE_SPECIFICATION + COMPLEX_QUERY_END);
                findPreparedStatement.setInt(2, limit);
                findPreparedStatement.setInt(3, offset);
            } else {
                findPreparedStatement = connection.prepareStatement(SEARCH_TRACK_BY_NAME_QUERY + STATUS_SPECIFICATION + SEARCH_PAGE_SPECIFICATION + COMPLEX_QUERY_END);
                findPreparedStatement.setInt(2, statusId);
                findPreparedStatement.setInt(3, limit);
                findPreparedStatement.setInt(4, offset);
            }
            findPreparedStatement.setString(1, name);
            tracksFromDB = findPreparedStatement.executeQuery();
            while (tracksFromDB.next()){
                tracks.add(new Track(tracksFromDB.getInt(1), tracksFromDB.getString(2),
                        tracksFromDB.getString(3), tracksFromDB.getInt(4)));
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to search tracks in db " + e.getSQLState());
            throw new DAOException(e, 300);
        } finally {
            close(findPreparedStatement);
            close(tracksFromDB);
            retrieve(connection);
        }
        return tracks;
    }

    @Override
    public Integer countTracks(String name, int statusId) throws DAOException {
        Connection connection = null;
        PreparedStatement countPreparedStatement = null;
        ResultSet countRS = null;
        Integer count = null;
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                countPreparedStatement = connection.prepareStatement(COUNT_TRACKS_QUERY + COMPLEX_QUERY_END);
            } else {
                countPreparedStatement = connection.prepareStatement(COUNT_TRACKS_QUERY + STATUS_SPECIFICATION + COMPLEX_QUERY_END);
                countPreparedStatement.setInt(2, statusId);
            }
            countPreparedStatement.setString(1, name);
            countRS = countPreparedStatement.executeQuery();
            if (countRS.next()){
                count = countRS.getInt(1);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to count tracks in db " + e.getSQLState());
            throw new DAOException(e, 300);
        } finally {
            close(countPreparedStatement);
            close(countRS);
            retrieve(connection);
        }
        return count;
    }

    @Override
    public void addTrackPerformersByIds(Integer trackId, List<Integer> performersIds) throws DAOException {
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
            logger.error("SQL error while trying to add track performers in db " + e.getSQLState());
            throw new DAOException(e, 300);
        } finally {
            for (PreparedStatement statement : addPreparedStatements) {
                close(statement);
            }
            retrieve(connection);
        }
    }

    @Override
    public void addTrackAlbumsByIds(Integer trackId, List<Integer> albumsIds) throws DAOException {
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
            logger.error("SQL error while trying to add track albums in db " + e.getSQLState());
            throw new DAOException(e, 300);
        } finally {
            for (PreparedStatement statement : addPreparedStatements) {
                close(statement);
            }
            retrieve(connection);
        }
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
                logger.error("Error while trying to delete track from db");
                throw new DAOException("Troubles with deleting track from db", 305);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to delete track from db " + e.getSQLState());
            throw new DAOException(e, 300);
        } finally {
            close(deletePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public void updateTrack(Integer trackId, String trackName, String trackLyrics) throws DAOException {
        Connection connection = null;
        PreparedStatement updatePreparedStatement = null;
        try {
            connection = getConnection(true);
            updatePreparedStatement = connection.prepareStatement(UPDATE_TRACK_QUERY);
            updatePreparedStatement.setString(1, trackName);
            updatePreparedStatement.setString(2, trackLyrics);
            updatePreparedStatement.setInt(3, trackId);
            int result = updatePreparedStatement.executeUpdate();
            if (result == 0) {
                logger.error("Error while trying to update track in db");
                throw new DAOException("Troubles updating track", 303);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to update track in db " + e.getSQLState());
            throw new DAOException(e, 300);
        } finally {
            close(updatePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public void updateTrackAlbumsByIds(Integer trackId, List<Integer> albumsIds) throws DAOException {
        Connection connection = null;
        PreparedStatement deletePreparedStatement = null;
        List<PreparedStatement> addPreparedStatements = new ArrayList<>();
        try {
            connection = getConnection(false);
            deletePreparedStatement = connection.prepareStatement(DELETE_ALBUMS_QUERY);
            deletePreparedStatement.setInt(1, trackId);
            deletePreparedStatement.executeUpdate();
            for (Integer albumId : albumsIds) {
                PreparedStatement addPreparedStatement = connection.prepareStatement(ADD_ALBUM_QUERY);
                addPreparedStatements.add(addPreparedStatement);
                addPreparedStatement.setInt(1, trackId);
                addPreparedStatement.setInt(2, albumId);
                addPreparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error("SQL error while trying to update track albums in db " + e.getSQLState());
            throw new DAOException(e, 300);
        } finally {
            close(deletePreparedStatement);
            for (PreparedStatement statement : addPreparedStatements) {
                close(statement);
            }
            retrieve(connection);
        }
    }

    @Override
    public void updateTrackPerformersByIds(Integer trackId, List<Integer> performersIds) throws DAOException {
        Connection connection = null;
        PreparedStatement deletePreparedStatement = null;
        List<PreparedStatement> addPreparedStatements = new ArrayList<>();
        try {
            connection = getConnection(false);
            deletePreparedStatement = connection.prepareStatement(DELETE_PERFORMERS_QUERY);
            deletePreparedStatement.setInt(1, trackId);
            deletePreparedStatement.executeUpdate();
            for (Integer performerId : performersIds) {
                PreparedStatement addPreparedStatement = connection.prepareStatement(ADD_PERFORMER_QUERY);
                addPreparedStatements.add(addPreparedStatement);
                addPreparedStatement.setInt(1, trackId);
                addPreparedStatement.setInt(2, performerId);
                addPreparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error("SQL error while trying to update track performers in db " + e.getSQLState());
            throw new DAOException(e, 300);
        } finally {
            close(deletePreparedStatement);
            for (PreparedStatement statement : addPreparedStatements) {
                close(statement);
            }
            retrieve(connection);
        }
    }

    @Override
    public void updateTrackStatus(Integer trackId, Integer statusId) throws DAOException{
        Connection connection = null;
        PreparedStatement updatePreparedStatement = null;
        try {
            connection = getConnection(true);
            updatePreparedStatement = connection.prepareStatement(UPDATE_TRACK_STATUS_QUERY);
            updatePreparedStatement.setInt(1, statusId);
            updatePreparedStatement.setInt(2, trackId);
            int result = updatePreparedStatement.executeUpdate();
            if (result == 0) {
                logger.error("Error while trying to update track status in db ");
                throw new DAOException("Troubles updating track status", 304);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to update track status in db " + e.getSQLState());
            throw new DAOException(e, 300);
        } finally {
            close(updatePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public Integer getCreatorId(Integer trackId) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet resultSet = null;
        Integer id = null;
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(GET_TRACK_CREATOR);
            findPreparedStatement.setInt(1, trackId);
            resultSet = findPreparedStatement.executeQuery();
            if (resultSet.next()){
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to get track creator from db " + e.getSQLState());
            throw new DAOException(e, 300);
        } finally {
            close(findPreparedStatement);
            close(resultSet);
            retrieve(connection);
        }
        return id;
    }

    @Override
    public List<Track> getCreatorTracks(Integer userId) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet resultSet = null;
        List<Track> tracks = new ArrayList<>();
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(GET_CREATOR_TRACKS);
            findPreparedStatement.setInt(1, userId);
            resultSet = findPreparedStatement.executeQuery();
            while (resultSet.next()){
                tracks.add(findById(resultSet.getInt(2)));
            }
        } catch (SQLException e) {
            logger.error("SQL error while trying to get creator tracks from db " + e.getSQLState());
            throw new DAOException(e, 300);
        } finally {
            close(findPreparedStatement);
            close(resultSet);
            retrieve(connection);
        }
        return tracks;
    }

    @Override
    public void addCreatorId(Integer trackId, Integer userId) throws DAOException {
        Connection connection = null;
        PreparedStatement addPreparedStatement = null;
        try {
            connection = getConnection(true);
            addPreparedStatement = connection.prepareStatement(ADD_TRACK_CREATOR);
            addPreparedStatement.setInt(1, trackId);
            addPreparedStatement.setInt(2, userId);
            int result = addPreparedStatement.executeUpdate();
            if (result == 0){
                logger.error("Error while trying to add track creator in db");
                throw new DAOException("Troubles with adding track-creator to db", 306);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to add track creator in db " + e.getSQLState());
            throw new DAOException(e, 300);
        } finally {
            close(addPreparedStatement);
            retrieve(connection);
        }
    }
}
