package command.impl.track;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceConstants;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.TrackService;

import java.io.IOException;

public class ChangeTrackCommand implements Command {

    private final Logger logger = LogManager.getLogger(ChangeTrackCommand.class);
    private final TrackService trackService;

    public ChangeTrackCommand() {
        trackService = ServiceFactory.getInstance().getTrackService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        int trackId = Integer.parseInt(request.getParameter("item_id"));
        String name = request.getParameter("name");
        String lyrics = request.getParameter("lyrics");
        String albumsIDs = request.getParameter("albums");
        String performersIDs = request.getParameter("performers");
        try {
            trackService.changeTrack(trackId, name, lyrics, performersIDs, albumsIDs);
            request.getSession().setAttribute("track", trackService.getTrackInfo(trackId, ServiceConstants.ALL_INFO));
            response.sendRedirect("/track");
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/change/track?err_code="+code);
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
