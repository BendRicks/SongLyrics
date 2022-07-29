package dao;

import dao.connection.ConnectionManager;
import dao.impl.AlbumDAO;
import dao.impl.PerformerDAO;
import dao.impl.TrackDAO;
import dao.impl.UserDAO;

public class DAOFactory {

    private static volatile DAOFactory instance;

    private final AlbumDAO albumDAO;
    private final PerformerDAO performerDAO;
    private final TrackDAO trackDAO;
    private final UserDAO userDAO;

    private DAOFactory() {
        ConnectionManager manager = ConnectionManager.getInstance();
        albumDAO = new AlbumDAO(manager);
        performerDAO = new PerformerDAO(manager);
        trackDAO = new TrackDAO(manager);
        userDAO = new UserDAO(manager);
    }

    public static DAOFactory getInstance() {
        if (instance == null) {
            synchronized (ConnectionManager.class) {
                if (instance == null) {
                    instance = new DAOFactory();
                }
            }
        }
        return instance;
    }

    public AlbumDAO getAlbumDAO() {
        return albumDAO;
    }

    public PerformerDAO getPerformerDAO() {
        return performerDAO;
    }

    public TrackDAO getTrackDAO() {
        return trackDAO;
    }

    public UserDAO getUserDAO() {
        return userDAO;
    }

}
