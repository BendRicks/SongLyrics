package command.impl.track;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import entity.Track;
import entity.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceConstants;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.TrackService;

import java.io.IOException;

public class GetTrackPage implements Command {

    private final Logger logger = LogManager.getLogger(GetTrackPage.class);
    private final TrackService trackService;

    public GetTrackPage(){
        trackService = ServiceFactory.getInstance().getTrackService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            int trackId = Integer.parseInt(request.getParameter("item_id"));
            Track track = trackService.getTrackInfo(trackId, ServiceConstants.ALL_INFO);
            UserSession creator = trackService.getTrackCreator(trackId);
            request.getSession().setAttribute("track_creator", creator);
            request.getSession().setAttribute("track", track);
            response.sendRedirect("/track");
        } catch (NumberFormatException e) {
            logger.error("Error parsing parameters");
            throw new RuntimeException(e);
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/track?err_code="+code);
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
