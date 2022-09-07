package service.impl.abstraction;

import entity.Performer;
import entity.UserSession;
import service.exception.ServiceException;

import java.util.List;

public interface IPerformerService {

    Performer createPerformer(String name, String description, String coverURL) throws ServiceException;
    List<Performer> searchPerformers(String strToSearch, int statusId, int page, int itemsOnPage) throws ServiceException;
    Integer countAlikePerformerPages(String strToSearch, int statusId, int itemsOnPage) throws ServiceException;
    Performer getPerformerInfo(int performerId, Integer infoType) throws ServiceException;
    void changeStatus(Integer performerId, int statusId) throws ServiceException;
    void changePerformer(Integer performerId, String performerName, String coverImagePath, String description) throws ServiceException;
    UserSession getPerformerCreator(Integer performerId) throws ServiceException;
    void addPerformerCreator(Integer performerId, Integer userId) throws ServiceException;
    List<Performer> getCreatorPerformers(Integer userId) throws ServiceException;

}
