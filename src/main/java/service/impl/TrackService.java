package service.impl;

import dao.DAOFactory;
import dao.exception.DAOException;
import dao.impl.TrackDAO;
import dao.impl.UserDAO;
import entity.Performer;
import entity.Track;
import entity.UserSession;
import service.ServiceConstants;
import service.impl.abstraction.ITrackService;
import service.exception.ServiceException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class TrackService implements ITrackService {

    private final TrackDAO trackDAO;

    public TrackService() {
        trackDAO = DAOFactory.getInstance().getTrackDAO();
    }

    @Override
    public Track createTrack(String name, String lyrics, String performersIDsString, String albumsIDsString) throws ServiceException {
        Track track;
        try {
            List<Integer> performersIDs = splitStringOnIDs(performersIDsString);
            List<Integer> albumsIDs = splitStringOnIDs(albumsIDsString);
            DAOFactory.getInstance().getPerformerDAO().arePerformersExist(performersIDs);
            DAOFactory.getInstance().getAlbumDAO().areAlbumsExist(albumsIDs);
            track = trackDAO.save(new Track(name, lyrics));
            int trackId = track.getId();
            trackDAO.addTrackPerformersByIds(trackId, performersIDs);
            trackDAO.addTrackAlbumsByIds(trackId, albumsIDs);
            track = getTrackInfo(trackId, ServiceConstants.ALL_INFO);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return track;
    }

    public List<Integer> splitStringOnIDs(String str){
        String[] splittedIDs = str.split(",");
        HashSet<Integer> idsSet = new HashSet<>();
        for (String idStr : splittedIDs) {
            idsSet.add(Integer.parseInt(idStr));
        }
        return new ArrayList<>(idsSet);
    }

    @Override
    public List<Track> searchTracks(String strToSearch, int statusId, int page, int itemsOnPage) throws ServiceException {
        List<Track> tracks;
        try {
            page = Math.max(page, 1);
            tracks = trackDAO.searchTracksByName("%" + strToSearch + "%", statusId, itemsOnPage, (page - 1) * itemsOnPage);
            tracks = trackDAO.loadTracksAlbums(trackDAO.loadTracksPerformers(tracks));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return tracks;
    }

    @Override
    public Integer countAlikeTracksPages(String strToSearch, int statusId, int itemsOnPage) throws ServiceException {
        int pages;
        try {
            int itemsAmount = trackDAO.countTracks("%" + strToSearch + "%", statusId);
            pages = (itemsAmount/itemsOnPage) + (itemsAmount % itemsOnPage != 0 ? 1 : 0);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return pages;
    }

    @Override
    public Track getTrackInfo(Integer trackId, Integer infoType) throws ServiceException {
        Track track;
        try {
            track = trackDAO.findById(trackId);
            switch (infoType){
                case 0:
                    trackDAO.loadTrackAlbums(track);
                    trackDAO.loadTrackPerformers(track);
                    break;
                case 2:
                    trackDAO.loadTrackAlbums(track);
                    break;
                case 3:
                    trackDAO.loadTrackPerformers(track);
                    break;
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return track;
    }

    @Override
    public void changeStatus(Integer trackId, int statusId) throws ServiceException {
        try {
           trackDAO.updateTrackStatus(trackId, statusId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void changeTrack(Integer trackId, String trackName, String lyrics, String performerIDsString, String albumIDsString) throws ServiceException {
        try {
            trackDAO.updateTrack(trackId, trackName, lyrics);
            trackDAO.updateTrackPerformersByIds(trackId, splitStringOnIDs(performerIDsString));
            trackDAO.updateTrackAlbumsByIds(trackId, splitStringOnIDs(albumIDsString));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public UserSession getTrackCreator(Integer trackId) throws ServiceException {
        UserSession userSession;
        UserDAO userDAO = DAOFactory.getInstance().getUserDAO();
        try {
            userSession = new UserSession(userDAO.findById(trackDAO.getCreatorId(trackId)));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return userSession;
    }

    @Override
    public void addTrackCreator(Integer trackId, Integer userId) throws ServiceException {
        try {
            trackDAO.addCreatorId(trackId, userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Track> getCreatorTracks(Integer userId) throws ServiceException {
        List<Track> tracks;
        try {
            tracks = trackDAO.getCreatorTracks(userId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return tracks;
    }
}
