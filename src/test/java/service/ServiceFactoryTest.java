package service;

import dao.DAOFactory;
import dao.impl.AlbumDAO;
import dao.impl.PerformerDAO;
import dao.impl.TrackDAO;
import dao.impl.UserDAO;
import org.junit.Assert;
import org.junit.Test;
import service.impl.AlbumService;
import service.impl.PerformerService;
import service.impl.TrackService;
import service.impl.UserService;

public class ServiceFactoryTest {

    @Test
    public void testGetInstance() {
        ServiceFactory factory = ServiceFactory.getInstance();
        Assert.assertNotNull(factory);
    }

    @Test
    public void testGetAlbumService() {
        AlbumService service = ServiceFactory.getInstance().getAlbumService();
        Assert.assertNotNull(service);
    }

    @Test
    public void testGetTrackService() {
        TrackService service = ServiceFactory.getInstance().getTrackService();
        Assert.assertNotNull(service);
    }

    @Test
    public void testGetPerformerService() {
        PerformerService service = ServiceFactory.getInstance().getPerformerService();
        Assert.assertNotNull(service);
    }

    @Test
    public void testGetUserService() {
        UserService service = ServiceFactory.getInstance().getUserService();
        Assert.assertNotNull(service);
    }

}
