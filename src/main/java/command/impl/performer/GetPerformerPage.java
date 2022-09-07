package command.impl.performer;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import entity.Performer;
import entity.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceConstants;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.PerformerService;

import java.io.IOException;

public class GetPerformerPage implements Command {

    private final Logger logger = LogManager.getLogger(GetPerformerPage.class);
    private final PerformerService performerService;

    public GetPerformerPage() {
        performerService = ServiceFactory.getInstance().getPerformerService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            int performerId = Integer.parseInt(request.getParameter("item_id"));
            Performer performer = performerService.getPerformerInfo(performerId, ServiceConstants.ALL_INFO);
            UserSession creator = performerService.getPerformerCreator(performerId);
            request.getSession().setAttribute("performer_creator", creator);
            request.getSession().setAttribute("performer", performer);
            response.sendRedirect("/performer");
        } catch (NumberFormatException e) {
            logger.error("Error parsing parameters");
            throw new RuntimeException(e);
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/performer?err_code="+code);
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
