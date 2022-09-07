package dao.impl;

import dao.DAOConstants;
import dao.DAOFactory;
import dao.exception.DAOException;
import entity.Track;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class TrackDAOTest {

    private static Track trackExpected;
    private Track track;

    @BeforeClass
    public static void init() {
        trackExpected = new Track(1, "Nancy, The Tavern Wench", "I know of a tavern not far from here\n" +
                "Where you can get some mighty fine beer\n" +
                "The company's true and the wenches are pretty\n" +
                "It's the greatest damn place in the whole of the city\n" +
                "If you're looking for crewmates, you'll sure find 'em there\n" +
                "Cutthroats and lowlifes and worse I should dare\n" +
                "Ol' Nancy don't care who comes to her inn\n" +
                "It's a den of debauchery violence and sin\n" +
                "\n" +
                "So come take a drink and drown your sorrows\n" +
                "And all of our fears will be gone till tomorrow\n" +
                "We'll have no regrets and live for the day\n" +
                "In Nancy's harbor cafe\n" +
                "\n" +
                "So come take a drink and drown your sorrows\n" +
                "And all of our fears will be gone till tomorrow\n" +
                "We'll have no regrets and live for the day\n" +
                "In Nancy's harbor cafe\n" +
                "\n" +
                "If you're looking to go on a glorious quest\n" +
                "There's a man there who knows of an old treasure chest\n" +
                "For some pieces of eight and a tankard of ale\n" +
                "He'll show you the map and tell you it's tale\n" +
                "And then there's Nancy, the lovely barmaiden\n" +
                "She may be old but her beauty ain't fading\n" +
                "Ol' Nancy don't care who comes to her inn\n" +
                "It's a den of debauchery violence and sin\n" +
                "\n" +
                "So come take a drink and drown your sorrows\n" +
                "And all of our fears will be gone till tomorrow\n" +
                "We'll have no regrets and live for the day\n" +
                "In Nancy's harbor cafe\n" +
                "\n" +
                "So come take a drink and drown your sorrows\n" +
                "And all of our fears will be gone till tomorrow\n" +
                "We'll have no regrets and live for the day\n" +
                "In Nancy's harbor cafe\n" +
                "\n" +
                "So come take a drink and drown your sorrows\n" +
                "And all of our fears will be gone till tomorrow\n" +
                "We'll have no regrets and live for the day\n" +
                "In Nancy's harbor cafe\n" +
                "\n" +
                "So come take a drink and drown your sorrows\n" +
                "And all of our fears will be gone till tomorrow\n" +
                "We'll have no regrets and live for the day\n" +
                "In Nancy's harbor cafe", DAOConstants.VERIFIED_ID);
    }

    @Test
    public void testFindById() {
        try {
            track = DAOFactory.getInstance().getTrackDAO().findById(1);
            Assert.assertEquals(track, trackExpected);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testFindByName() {
        track = new Track("Nancy, The Tavern Wench", null);
        try {
            track = DAOFactory.getInstance().getTrackDAO().find(track);
            Assert.assertEquals(track, trackExpected);
        } catch (DAOException e) {
            throw new RuntimeException(e);
        }
    }

}
