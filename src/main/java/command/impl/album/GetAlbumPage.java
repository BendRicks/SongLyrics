package command.impl.album;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import entity.Album;
import entity.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceConstants;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.AlbumService;

import java.io.IOException;

public class GetAlbumPage implements Command {

    private final Logger logger = LogManager.getLogger(GetAlbumPage.class);
    private final AlbumService albumService;

    public GetAlbumPage() {
        albumService = ServiceFactory.getInstance().getAlbumService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            int albumId = Integer.parseInt(request.getParameter("item_id"));
            Album album = albumService.getAlbumInfo(albumId, ServiceConstants.ALL_INFO);
            UserSession creator = albumService.getAlbumCreator(albumId);
            request.getSession().setAttribute("album_creator", creator);
            request.getSession().setAttribute("album", album);
            response.sendRedirect("/album");
        } catch (NumberFormatException e) {
            logger.error("Error parsing parameters");
            throw new RuntimeException(e);
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/album?err_code="+code);
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
