package command.impl.album;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import entity.Album;
import entity.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.AlbumService;
import java.io.IOException;

public class CreateAlbumCommand implements Command {

    private final Logger logger = LogManager.getLogger(ChangeAlbumCommand.class);
    private final AlbumService albumService;

    public CreateAlbumCommand(){
        albumService = ServiceFactory.getInstance().getAlbumService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        UserSession userSession = (UserSession) request.getSession().getAttribute("USER_SESSION_INFO");
        String name = request.getParameter("name");
        String coverPath = request.getParameter("cover_path");
        String description = request.getParameter("description");
        String performersIDs = request.getParameter("performers");
        try {
            Album album = albumService.createAlbum(name, coverPath, description, performersIDs);
            albumService.addAlbumCreator(album.getId(), userSession.getId());
            request.getSession().setAttribute("album_creator", userSession);
            request.getSession().setAttribute("album", album);
            response.sendRedirect("/album");
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/create/album?err_code="+code);
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
