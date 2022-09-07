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

public class TurnPerformersPageCommand implements Command {

    private final Logger logger = LogManager.getLogger(TurnPerformersPageCommand.class);
    private final PerformerService performerService;

    public TurnPerformersPageCommand(){
        performerService = ServiceFactory.getInstance().getPerformerService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            String query = (String) request.getSession().getAttribute("query");
            int verification = (int) request.getSession().getAttribute("verification");
            int page = Integer.parseInt(request.getParameter("page"));
            List<Performer> performers = performerService.searchPerformers(query, verification, page, 20);
            request.getSession().setAttribute("performers", performers);
            request.getSession().setAttribute("page", page);
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
        }
    }
}
