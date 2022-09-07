package command.impl.album;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import entity.Album;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceConstants;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.AlbumService;

import java.io.IOException;

public class ChangeAlbumCommand implements Command {

    private final Logger logger = LogManager.getLogger(ChangeAlbumCommand.class);
    private final AlbumService albumService;

    public ChangeAlbumCommand() {
        albumService = ServiceFactory.getInstance().getAlbumService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        int albumId = Integer.parseInt(request.getParameter("item_id"));
        String name = request.getParameter("name");
        String coverPath = request.getParameter("cover_path");
        String description = request.getParameter("description");
        String performersIDs = request.getParameter("performers");
        try {
            albumService.changeAlbum(albumId, name, coverPath, description, performersIDs);
            request.getSession().setAttribute("album", albumService.getAlbumInfo(albumId, ServiceConstants.ALL_INFO));
            response.sendRedirect("/album");
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/change/album?err_code="+code);
            } catch (IOException ex) {
                logger.error("Can't find file to redirect");
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            logger.error("Can't find file to redirect");
            throw new RuntimeException(e);
        }
    }
}
