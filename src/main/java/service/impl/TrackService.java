package service.impl;

import dao.DAOFactory;
import dao.exception.DAOException;
import dao.impl.TrackDAO;
import entity.Track;
import service.impl.abstraction.ITrackService;
import service.exception.ServiceException;

import java.util.List;

public class TrackService implements ITrackService {

    private final TrackDAO trackDAO;

    public TrackService(){
        trackDAO = DAOFactory.getInstance().getTrackDAO();
    }

    @Override
    public Track createTrack(Track track, List<Integer> performersIds, List<Integer> albumsIds) throws ServiceException {
        Track trackFromDB = null;
        try {
            trackFromDB = trackDAO.save(track);
            int trackId = trackFromDB.getId();
            trackDAO.addTrackPerformersByIds(trackId, performersIds);
            trackDAO.addTrackAlbumsByIds(trackId, albumsIds);
            trackFromDB = trackDAO.loadTrackDependencies(track);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return trackFromDB;
    }

    @Override
    public List<Track> searchTracks(String strToSearch, int statusId, int page) throws ServiceException {
        List<Track> tracks = null;
        try {
            tracks = trackDAO.loadTracksDependencies(
                    trackDAO.searchTracksByName("%" + strToSearch + "%", statusId, page * 20));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return tracks;
    }

    @Override
    public Track getTrackInfo(Integer trackId) throws ServiceException {
        Track track = null;
        try {
            track = trackDAO.loadTrackDependencies(trackDAO.findById(trackId));
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return track;
    }

    @Override
    public Track changeStatus(Integer trackId, int statusId) throws ServiceException {
        Track track = null;
        try {
            track = trackDAO.updateTrackStatus(trackId, statusId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return track;
    }

    @Override
    public Track changeTrackName(Integer trackId, String trackName) throws ServiceException {
        Track track = null;
        try {
            track = trackDAO.updateTrackName(trackId, trackName);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return track;
    }

    @Override
    public Track updatePerformers(Integer trackId, List<Integer> performersIds) throws ServiceException {
        Track track = null;
        try {
            trackDAO.clearTrackPerformers(trackId);
            track = trackDAO.addTrackPerformersByIds(trackId, performersIds);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return track;
    }

    @Override
    public Track updateAlbums(Integer trackId, List<Integer> albumsIds) throws ServiceException {
        Track track = null;
        try {
            trackDAO.clearTrackAlbums(trackId);
            track = trackDAO.addTrackAlbumsByIds(trackId, albumsIds);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return track;
    }
}
