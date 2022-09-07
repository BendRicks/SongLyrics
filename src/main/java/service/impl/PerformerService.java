package service.impl;

import dao.DAOFactory;
import dao.exception.DAOException;
import dao.impl.PerformerDAO;
import dao.impl.UserDAO;
import entity.Performer;
import entity.UserSession;
import service.ServiceConstants;
import service.impl.abstraction.IPerformerService;
import service.exception.ServiceException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class PerformerService implements IPerformerService {

    private final PerformerDAO performerDAO;
    private final String RESOURCE_PATH = "src/main/resources/performers/descriptions/";
    private final String TXT_EXT = ".txt";

    public PerformerService(){
        performerDAO = DAOFactory.getInstance().getPerformerDAO();
    }

    @Override
    public Performer createPerformer(String name, String description, String coverURL) throws ServiceException {
        Performer performer;
        try {
            performer = performerDAO.save(new Performer(name, description, coverURL));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return performer;
    }

    @Override
    public List<Performer> searchPerformers(String strToSearch, int statusId, int page, int itemsOnPage) throws ServiceException {
        List<Performer> performers;
        try {
            page = Math.max(page, 1);
            performers = performerDAO.searchPerformersByName("%" + strToSearch + "%", statusId, itemsOnPage, (page-1) * itemsOnPage);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return performers;
    }

    @Override
    public Integer countAlikePerformerPages(String strToSearch, int statusId, int itemsOnPage) throws ServiceException {
        int pages;
        try {
            int itemsAmount = performerDAO.countPerformers(strToSearch, statusId);
            pages = (itemsAmount/itemsOnPage) + (itemsAmount % itemsOnPage != 0 ? 1 : 0);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return pages;
    }

    @Override
    public Performer getPerformerInfo(int performerId, Integer infoType) throws ServiceException {
        Performer performer;
        try {
            performer = performerDAO.findById(performerId);
            switch (infoType){
                case 0:
                    performerDAO.loadPerformerTracks(performer);
                    performerDAO.loadPerformerAlbums(performer);
                    break;
                case 1:
                    performerDAO.loadPerformerTracks(performer);
                    break;
                case 2:
                    performerDAO.loadPerformerAlbums(performer);
                    break;
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return performer;
    }

    @Override
    public void changeStatus(Integer performerId, int statusId) throws ServiceException {
        try {
            performerDAO.updatePerformerStatus(performerId, statusId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void changePerformer(Integer performerId, String performerName, String coverImagePath, String description) throws ServiceException {
        Performer performer = null;
        try {
            performerDAO.updatePerformer(performerId, performerName, coverImagePath, description);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public UserSession getPerformerCreator(Integer performerId) throws ServiceException {
        UserSession userSession;
        UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
        try {
            userSession = new UserSession(userDAO.findById(performerDAO.getCreatorId(performerId)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return userSession;
    }

    @Override
    public void addPerformerCreator(Integer performerId, Integer userId) throws ServiceException {
        try {
            performerDAO.addCreatorId(performerId, userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Performer> getCreatorPerformers(Integer userId) throws ServiceException {
        List<Performer> performers;
        try {
            performers = performerDAO.getCreatorPerformers(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return performers;
    }
}
