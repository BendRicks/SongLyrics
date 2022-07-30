package service.impl;

import dao.DAOFactory;
import dao.exception.DAOException;
import dao.impl.AlbumDAO;
import entity.Album;
import service.impl.abstraction.IAlbumService;
import service.exception.ServiceException;

import java.util.List;

public class AlbumService implements IAlbumService {

    private final AlbumDAO albumDAO;

    public AlbumService(){
        albumDAO = DAOFactory.getInstance().getAlbumDAO();
    }

    @Override
    public Album createAlbum(Album album, List<Integer> performersIds) throws ServiceException {
        Album albumFromDB = null;
        try {
            albumFromDB = albumDAO.save(album);
            albumDAO.addAlbumPerformersByIds(album.getId(), performersIds);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return albumFromDB;
    }

    @Override
    public List<Album> searchAlbums(String strToSearch, int statusId, int page) throws ServiceException {
        List<Album> albums = null;
        try {
            albums = albumDAO.loadAlbumsDependencies(
                    albumDAO.searchAlbumsByName("%" + strToSearch + "%", statusId, page * 20));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return albums;
    }

    @Override
    public Album getAlbumInfo(int albumId) throws ServiceException {
        Album album = null;
        try {
            album = albumDAO.loadAlbumDependencies(albumDAO.findById(albumId));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return album;
    }

    @Override
    public Album changeStatus(Integer albumId, int statusId) throws ServiceException {
        Album album = null;
        try {
            album = albumDAO.updateAlbumStatus(albumId, statusId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return album;
    }

    @Override
    public Album changeAlbumName(Integer albumId, String albumName) throws ServiceException {
        Album album = null;
        try {
            album = albumDAO.updateAlbumName(albumId, albumName);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return album;
    }

    @Override
    public Album updatePerformers(Integer albumId, List<Integer> performersIds) throws ServiceException {
        Album album = null;
        try {
            albumDAO.clearAlbumPerformers(albumId);
            album = albumDAO.addAlbumPerformersByIds(albumId, performersIds);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return album;
    }
}
