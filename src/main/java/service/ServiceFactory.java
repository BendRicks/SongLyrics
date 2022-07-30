package service;

import service.impl.AlbumService;
import service.impl.PerformerService;
import service.impl.TrackService;
import service.impl.UserService;

public class ServiceFactory {

    private static volatile ServiceFactory instance;

    private final AlbumService albumService;
    private final PerformerService performerService;
    private final TrackService trackService;

    private final UserService userService;

    private ServiceFactory() {
        albumService = new AlbumService();
        performerService = new PerformerService();
        trackService = new TrackService();
        userService = new UserService();
    }

    public static ServiceFactory getInstance() {
        if (instance == null) {
            synchronized (ServiceFactory.class) {
                if (instance == null) {
                    instance = new ServiceFactory();
                }
            }
        }
        return instance;
    }

    public AlbumService getAlbumService() {
        return albumService;
    }

    public PerformerService getPerformerService() {
        return performerService;
    }

    public TrackService getTrackService() {
        return trackService;
    }

    public UserService getUserService() {
        return userService;
    }

}
