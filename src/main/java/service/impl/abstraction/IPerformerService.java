package service.impl.abstraction;

import entity.Performer;
import service.exception.ServiceException;

import java.util.List;

public interface IPerformerService {

    Performer createPerformer(Performer performer) throws ServiceException;
    List<Performer> searchPerformers(String strToSearch, int statusId, int page) throws ServiceException;
    Performer getPerformerInfo(int performerId) throws ServiceException;
    Performer changeStatus(Integer performerId, int statusId) throws ServiceException;
    Performer changePerformerName(Integer performerId, String performerName) throws ServiceException;

}
