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

public class SearchTracksCommand implements Command {

    private final Logger logger = LogManager.getLogger(SearchTracksCommand.class);
    private final TrackService trackService;

    public SearchTracksCommand(){
        trackService = ServiceFactory.getInstance().getTrackService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            String query = request.getParameter("query");
            int verification = Integer.parseInt(request.getParameter("verification"));
            int pages = trackService.countAlikeTracksPages(query, verification, 20);
            List<Track> tracks = trackService.searchTracks(query, verification, 0, 20);
            request.getSession().setAttribute("query", query);
            request.getSession().setAttribute("verification", verification);
            request.getSession().setAttribute("tracks", tracks);
            request.getSession().setAttribute("pages", pages);
            request.getSession().setAttribute("page", 1);
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
        } catch (IOException | NumberFormatException e) {
            logger.error("Can't find file to redirect");
            throw new RuntimeException(e);
        }
    }
}
