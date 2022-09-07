package command.impl.track;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import entity.Track;
import entity.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.TrackService;

import java.io.IOException;

public class CreateTrackCommand implements Command {

    private final Logger logger = LogManager.getLogger(CreateTrackCommand.class);
    private final TrackService trackService;

    public CreateTrackCommand() {
        trackService = ServiceFactory.getInstance().getTrackService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        UserSession userSession = (UserSession) request.getSession().getAttribute("USER_SESSION_INFO");
        String name = request.getParameter("name");
        String lyrics = request.getParameter("lyrics");
        String albumsIDs = request.getParameter("albums");
        String performersIDs = request.getParameter("performers");
        try {
            Track track = trackService.createTrack(name, lyrics, performersIDs, albumsIDs);
            trackService.addTrackCreator(track.getId(), userSession.getId());
            request.getSession().setAttribute("track_creator", userSession);
            request.getSession().setAttribute("track", track);
            response.sendRedirect("/track");
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/create/track?err_code="+code);
            } catch (IOException ex) {
                logger.error("Can't find file to redirect");
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            logger.error("Can't find file to redirect");
            throw new RuntimeException(e);
        }
    }
}
