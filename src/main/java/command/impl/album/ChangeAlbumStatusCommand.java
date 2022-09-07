package command.impl.album;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import entity.Album;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.AlbumService;

import java.io.IOException;

public class ChangeAlbumStatusCommand implements Command {

    private final Logger logger = LogManager.getLogger(ChangeAlbumStatusCommand.class);
    private final AlbumService albumService;

    public ChangeAlbumStatusCommand() {
        albumService = ServiceFactory.getInstance().getAlbumService();
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        int albumId = Integer.parseInt(request.getParameter("item_id"));
        int statusId = Integer.parseInt(request.getParameter("status_id"));
        try {
            albumService.changeStatus(albumId, statusId);
            ((Album)request.getSession().getAttribute("album")).setStatus(statusId);
            response.sendRedirect("/album");
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
