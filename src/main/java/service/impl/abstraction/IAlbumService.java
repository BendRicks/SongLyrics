package service.impl.abstraction;

import entity.Album;
import entity.Performer;
import entity.UserSession;
import service.exception.ServiceException;

import java.util.List;

public interface IAlbumService {

    Album createAlbum(String name, String description, String coverURL, String performerIDsString) throws ServiceException;
    List<Album> searchAlbums(String strToSearch, int statusId, int page, int itemsOnPage) throws ServiceException;
    Integer countAlikeAlbumsPages(String strToSearch, int statusId, int itemsOnPage) throws ServiceException;
    Album getAlbumInfo(int albumId, Integer infoType) throws ServiceException;
    void changeStatus(Integer albumId, int statusId) throws ServiceException;
    void changeAlbum(Integer albumId, String albumName, String coverImagePath, String description, String performerIDsString) throws ServiceException;
    UserSession getAlbumCreator(Integer albumId) throws ServiceException;
    void addAlbumCreator(Integer albumId, Integer userId) throws ServiceException;
    List<Album> getCreatorAlbums(Integer userId) throws ServiceException;

}
