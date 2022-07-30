package service.impl.abstraction;

import entity.Track;
import service.exception.ServiceException;

import java.util.List;

public interface ITrackService {

    Track createTrack(Track track, List<Integer> performersIds, List<Integer> albumsIds) throws ServiceException;
    List<Track> searchTracks(String strToSearch, int statusId, int page) throws ServiceException;
    Track getTrackInfo(Integer trackId) throws ServiceException;
    Track changeStatus(Integer trackId, int statusId) throws ServiceException;
    Track changeTrackName(Integer trackId, String trackName) throws ServiceException;
    Track updatePerformers(Integer trackId, List<Integer> performersIds) throws ServiceException;
    Track updateAlbums(Integer trackId, List<Integer> albumsIds) throws ServiceException;

}
