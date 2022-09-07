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

public class SearchAlbumsCommand implements Command {

    private final Logger logger = LogManager.getLogger(SearchAlbumsCommand.class);
    private final AlbumService albumService;

    public SearchAlbumsCommand(){
        albumService = ServiceFactory.getInstance().getAlbumService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            String query = request.getParameter("query");
            int verification = Integer.parseInt(request.getParameter("verification"));
            int pages = albumService.countAlikeAlbumsPages(query, verification, 20);
            List<Album> albums = albumService.searchAlbums(query, verification, 0, 20);
            request.getSession().setAttribute("query", query);
            request.getSession().setAttribute("verification", verification);
            request.getSession().setAttribute("albums", albums);
            request.getSession().setAttribute("pages", pages);
            request.getSession().setAttribute("page", 1);
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
        } catch (NumberFormatException e){
            logger.error("Error parsing parameters");
            throw new RuntimeException(e);
        }
    }
}
