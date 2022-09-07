package dao;

import dao.impl.AlbumDAO;
import dao.impl.PerformerDAO;
import dao.impl.TrackDAO;
import dao.impl.UserDAO;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DAOFactoryTest {

    @Test
    public void testGetInstance() {
        DAOFactory factory = DAOFactory.getInstance();
        Assert.assertNotNull(factory);
    }

    @Test
    public void testGetAlbumDAO() {
        AlbumDAO dao = DAOFactory.getInstance().getAlbumDAO();
        Assert.assertNotNull(dao);
    }

    @Test
    public void testGetTrackDAO() {
        TrackDAO dao = DAOFactory.getInstance().getTrackDAO();
        Assert.assertNotNull(dao);
    }

    @Test
    public void testGetPerformerDAO() {
        PerformerDAO dao = DAOFactory.getInstance().getPerformerDAO();
        Assert.assertNotNull(dao);
    }

    @Test
    public void testGetUserDAO() {
        UserDAO dao = DAOFactory.getInstance().getUserDAO();
        Assert.assertNotNull(dao);
    }

}
