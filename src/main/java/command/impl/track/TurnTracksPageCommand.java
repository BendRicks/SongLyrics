package command.impl.track;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import entity.Track;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.TrackService;

import java.io.IOException;
import java.util.List;

public class TurnTracksPageCommand implements Command {

    private final Logger logger = LogManager.getLogger(TurnTracksPageCommand.class);
    private final TrackService trackService;

    public TurnTracksPageCommand() {
        trackService = ServiceFactory.getInstance().getTrackService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String query = (String) request.getSession().getAttribute("query");
        int verification = (int) request.getSession().getAttribute("verification");
        int page = Integer.parseInt(request.getParameter("page"));
        try {
            List<Track> tracks = trackService.searchTracks(query, verification, page, 20);
            request.getSession().setAttribute("tracks", tracks);
            request.getSession().setAttribute("page", page);
            response.sendRedirect("/search/track");
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/search/track?err_code="+code);
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
