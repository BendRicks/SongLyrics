package dao.impl;

import dao.DAOConstants;
import dao.DAOFactory;
import dao.exception.DAOException;
import entity.Album;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AlbumDAOTest {

    private static Album albumExpected;
    private Album album;

    @BeforeClass
    public static void init() {
        albumExpected = new Album(2, "Black Sails At Midnight", "https://upload.wikimedia.org/wikipedia/ru/1/12/Alestorm_Black_Sails_at_Midnight_w.jpg", "Второй полноформатный альбом шотландской фолк/пауэр-метал-группы Alestorm. Лирика альбома посвящена легендам и реальным историям из жизни морских разбойников Карибского бассейна второй половины XVII века.\n" +
                "\n" +
                "Композиция «Wolves Of The Sea» (с англ. — «морские волки») является кавером на песню латвийской группы Pirates Of The Sea, выступившей с этой песней на конкурсе «Евровидение» в 2008 году. Латвия тогда набрала 83 итоговых балла (11 место).", DAOConstants.NON_VERIFIED_ID);
    }

    @Test
    public void testFindById() {
        try {
            album = DAOFactory.getInstance().getAlbumDAO().findById(2);
            Assert.assertEquals(album, albumExpected);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindByName() {
        album = new Album("Black Sails At Midnight", null, null);
        try {
            album = DAOFactory.getInstance().getAlbumDAO().find(album);
            Assert.assertEquals(album, albumExpected);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

}
