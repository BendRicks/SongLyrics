package dao.impl;

import dao.DAOConstants;
import dao.DAOFactory;
import dao.exception.DAOException;
import entity.Performer;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class PerformerDAOTest {

    private static Performer performerExpected;
    private Performer performer;

    @BeforeClass
    public static void init() {
        performerExpected = new Performer(1, "Alestorm", "https://cdns-images.dzcdn.net/images/artist/c18b6750b1629d1c53aeb7aa0565dfe4/500x500.jpg", "Фолк/пауэр-метал-группа из города Перт (Шотландия). Песни Alestorm посвящены пиратам, и свою музыку они называют «Настоящий шотландский пиратский метал» (англ. True scottish pirate metal).", DAOConstants.VERIFIED_ID);
    }

    @Test
    public void testFindById() {
        try {
            performer = DAOFactory.getInstance().getPerformerDAO().findById(1);
            Assert.assertEquals(performer, performerExpected);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindByName() {
        performer = new Performer("Alestorm", null, null);
        try {
            performer = DAOFactory.getInstance().getPerformerDAO().find(performer);
            Assert.assertEquals(performer, performerExpected);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

}
