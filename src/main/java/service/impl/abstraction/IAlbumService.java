package service.impl.abstraction;

import entity.Album;
import service.exception.ServiceException;

import java.util.List;

public interface IAlbumService {

    Album createAlbum(Album album, List<Integer> performersIds) throws ServiceException;
    List<Album> searchAlbums(String strToSearch, int statusId, int page) throws ServiceException;
    Album getAlbumInfo(int albumId) throws ServiceException;
    Album changeStatus(Integer albumId, int statusId) throws ServiceException;
    Album changeAlbumName(Integer albumId, String albumName) throws ServiceException;
    Album updatePerformers(Integer albumId, List<Integer> performersIds) throws ServiceException;

}
