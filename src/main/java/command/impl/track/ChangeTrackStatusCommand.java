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

public class ChangeTrackStatusCommand implements Command {

    private final Logger logger = LogManager.getLogger(ChangeTrackStatusCommand.class);
    private final TrackService trackService;

    public ChangeTrackStatusCommand() {
        trackService = ServiceFactory.getInstance().getTrackService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        int trackId = Integer.parseInt(request.getParameter("item_id"));
        int statusId = Integer.parseInt(request.getParameter("status_id"));
        try {
            trackService.changeStatus(trackId, statusId);
            ((Track)request.getSession().getAttribute("track")).setStatus(statusId);
            response.sendRedirect("/track");
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
