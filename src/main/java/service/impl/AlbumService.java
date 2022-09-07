package service.impl;

import dao.DAOFactory;
import dao.exception.DAOException;
import dao.impl.AlbumDAO;
import dao.impl.UserDAO;
import entity.Album;
import entity.Performer;
import entity.UserSession;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceConstants;
import service.impl.abstraction.IAlbumService;
import service.exception.ServiceException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AlbumService implements IAlbumService {

    private final Logger logger = LogManager.getLogger(AlbumService.class);
    private final AlbumDAO albumDAO;

    public AlbumService(){
        albumDAO = DAOFactory.getInstance().getAlbumDAO();
    }

    @Override
    public Album createAlbum(String name, String description, String coverURL, String performersIDsString) throws ServiceException {
        Album album;
        try {
            List<Integer> performersIDs = splitStringOnIDs(performersIDsString);
            DAOFactory.getInstance().getPerformerDAO().arePerformersExist(performersIDs);
            album = albumDAO.save(new Album(name, coverURL, description));
            albumDAO.addAlbumPerformersByIds(album.getId(), performersIDs);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return getAlbumInfo(album.getId(), ServiceConstants.ALL_INFO);
    }

    private List<Integer> splitStringOnIDs(String str){
        String[] splittedIDs = str.split(",");
        HashSet<Integer> idsSet = new HashSet<>();
        for (String idStr : splittedIDs) {
            idsSet.add(Integer.parseInt(idStr));
        }
        return new ArrayList<>(idsSet);
    }

    @Override
    public List<Album> searchAlbums(String strToSearch, int statusId, int page, int itemsOnPage) throws ServiceException {
        List<Album> albums;
        try {
            page = Math.max(page, 1);
            albums = albumDAO.searchAlbumsByName("%" + strToSearch + "%", statusId, itemsOnPage, (page-1) * itemsOnPage);
            albums = albumDAO.loadAlbumsPerformers(albumDAO.loadAlbumsTracks(albums));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return albums;
    }

    @Override
    public Integer countAlikeAlbumsPages(String strToSearch, int statusId, int itemsOnPage) throws ServiceException {
        int pages;
        try {
            int itemsAmount = albumDAO.countAlbums(strToSearch, statusId);
            pages = (itemsAmount/itemsOnPage) + (itemsAmount % itemsOnPage != 0 ? 1 : 0);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return pages;
    }

    @Override
    public Album getAlbumInfo(int albumId, Integer infoType) throws ServiceException {
        Album album;
        try {
            album = albumDAO.findById(albumId);
            switch (infoType){
                case 0:
                    albumDAO.loadAlbumTracks(album);
                    albumDAO.loadAlbumPerformers(album);
                    break;
                case 1:
                    albumDAO.loadAlbumTracks(album);
                    break;
                case 3:
                    albumDAO.loadAlbumPerformers(album);
                    break;
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return album;
    }

    @Override
    public void changeStatus(Integer albumId, int statusId) throws ServiceException {
        Album album = null;
        try {
            albumDAO.updateAlbumStatus(albumId, statusId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void changeAlbum(Integer albumId, String albumName, String coverImagePath, String description, String performerIDsString) throws ServiceException {
        try {
            albumDAO.updateAlbum(albumId, albumName, coverImagePath, description);
            albumDAO.updateAlbumPerformersByIds(albumId, splitStringOnIDs(performerIDsString));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public UserSession getAlbumCreator(Integer albumId) throws ServiceException {
        UserSession userSession;
        UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
        try {
            userSession = new UserSession(userDAO.findById(albumDAO.getCreatorId(albumId)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return userSession;
    }

    @Override
    public void addAlbumCreator(Integer albumId, Integer userId) throws ServiceException {
        try {
            albumDAO.addCreatorId(albumId, userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Album> getCreatorAlbums(Integer userId) throws ServiceException {
        List<Album> albums;
        try {
            albums = albumDAO.getCreatorAlbums(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return albums;
    }
}
