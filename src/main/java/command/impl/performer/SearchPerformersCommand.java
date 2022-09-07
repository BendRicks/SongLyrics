package command.impl.performer;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import entity.Performer;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.PerformerService;

import java.io.IOException;
import java.util.List;

public class SearchPerformersCommand implements Command {

    private final Logger logger = LogManager.getLogger(SearchPerformersCommand.class);
    private final PerformerService performerService;

    public SearchPerformersCommand(){
        performerService = ServiceFactory.getInstance().getPerformerService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            String query = request.getParameter("query");
            int verification = Integer.parseInt(request.getParameter("verification"));
            int pages = performerService.countAlikePerformerPages(query, verification, 20);
            List<Performer> performers = performerService.searchPerformers(query, verification, 0, 20);
            request.getSession().setAttribute("query", query);
            request.getSession().setAttribute("verification", verification);
            request.getSession().setAttribute("performers", performers);
            request.getSession().setAttribute("pages", pages);
            request.getSession().setAttribute("page", 1);
            response.sendRedirect("/search/performer");
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/search/performer?err_code="+code);
            } catch (IOException ex) {
                logger.error("Can't find file to redirect");
                throw new RuntimeException(ex);
            }
        } catch (IOException e) {
            logger.error("Can't find file to redirect");
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            logger.error("Error parsing parameters");
            throw new RuntimeException(e);
        }
    }
}
