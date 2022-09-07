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
import java.util.List;

public class TurnAlbumsPageCommand implements Command {

    private final Logger logger = LogManager.getLogger(TurnAlbumsPageCommand.class);
    private final AlbumService albumService;

    public TurnAlbumsPageCommand(){
        albumService = ServiceFactory.getInstance().getAlbumService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String query = (String) request.getSession().getAttribute("query");
        int verification = (int) request.getSession().getAttribute("verification");
        int page = Integer.parseInt(request.getParameter("page"));
        try {
            List<Album> albums = albumService.searchAlbums(query, verification, page, 20);
            request.getSession().setAttribute("albums", albums);
            request.getSession().setAttribute("page", page);
            response.sendRedirect("/search/album");
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/search/album?err_code="+code);
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
