package service.impl.abstraction;

import entity.Performer;
import entity.Track;
import entity.UserSession;
import service.exception.ServiceException;

import java.util.List;

public interface ITrackService {

    Track createTrack(String name, String lyrics, String performersIDsString, String albumsIdsString) throws ServiceException;
    List<Track> searchTracks(String strToSearch, int statusId, int page, int itemsOnPage) throws ServiceException;
    Integer countAlikeTracksPages(String strToSearch, int statusId, int itemsOnPage) throws ServiceException;
    Track getTrackInfo(Integer trackId, Integer infoType) throws ServiceException;
    void changeStatus(Integer trackId, int statusId) throws ServiceException;
    void changeTrack(Integer trackId, String trackName, String lyrics, String performerIDsString, String albumIDsString) throws ServiceException;
    UserSession getTrackCreator(Integer trackId) throws ServiceException;
    void addTrackCreator(Integer trackId, Integer userId) throws ServiceException;
    List<Track> getCreatorTracks(Integer userId) throws ServiceException;

}
