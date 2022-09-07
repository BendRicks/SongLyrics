package command.impl.user;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import entity.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceFactory;
import service.exception.ServiceException;

import java.io.IOException;

public class GetUserPageCommand implements Command {

    private final Logger logger = LogManager.getLogger(GetUserPageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        UserSession userSession = (UserSession) request.getSession().getAttribute("USER_SESSION_INFO");
        try {
            if (userSession == null) {
                response.sendRedirect("/");
            } else {
                int userId = userSession.getId();
                request.getSession().setAttribute("user_performers",
                        ServiceFactory.getInstance().getPerformerService().getCreatorPerformers(userId));
                request.getSession().setAttribute("user_albums",
                        ServiceFactory.getInstance().getAlbumService().getCreatorAlbums(userId));
                request.getSession().setAttribute("user_tracks",
                        ServiceFactory.getInstance().getTrackService().getCreatorTracks(userId));
                response.sendRedirect("/user");
            }
        } catch (IOException e) {
            logger.error("Can't find file to redirect");
            throw new RuntimeException(e);
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/user?err_code="+code);
            } catch (IOException ex) {
                logger.error("Can't find file to redirect");
                throw new RuntimeException(ex);
            }
        }
    }
}
