package service.impl;

import dao.DAOFactory;
import dao.exception.DAOException;
import dao.impl.PerformerDAO;
import entity.Performer;
import service.impl.abstraction.IPerformerService;
import service.exception.ServiceException;

import java.util.List;

public class PerformerService implements IPerformerService {

    private final PerformerDAO performerDAO;

    public PerformerService(){
        performerDAO = DAOFactory.getInstance().getPerformerDAO();
    }

    @Override
    public Performer createPerformer(Performer performer) throws ServiceException {
        Performer performerFromDB = null;
        try {
            performerFromDB = performerDAO.save(performer);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return performerFromDB;
    }

    @Override
    public List<Performer> searchPerformers(String strToSearch, int statusId, int page) throws ServiceException {
        List<Performer> performers = null;
        try {
            performers = performerDAO.searchPerformersByName("%" + strToSearch + "%", statusId, page * 20);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return performers;
    }

    @Override
    public Performer getPerformerInfo(int performerId) throws ServiceException {
        Performer performer = null;
        try {
            performer = performerDAO.loadPerformerDependencies(performerDAO.findById(performerId));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return performer;
    }

    @Override
    public Performer changeStatus(Integer performerId, int statusId) throws ServiceException {
        Performer performer = null;
        try {
            performer = performerDAO.updatePerformerStatus(performerId, statusId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return performer;
    }

    @Override
    public Performer changePerformerName(Integer performerId, String performerName) throws ServiceException {
        Performer performer = null;
        try {
            performer = performerDAO.updatePerformerName(performerId, performerName);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return performer;
    }
}
