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

public class ChangePerformerStatusCommand implements Command {

    private final Logger logger = LogManager.getLogger(ChangePerformerStatusCommand.class);
    private final PerformerService performerService;

    public ChangePerformerStatusCommand() {
        performerService = ServiceFactory.getInstance().getPerformerService();
    }


    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        int performerId = Integer.parseInt(request.getParameter("item_id"));
        int statusId = Integer.parseInt(request.getParameter("status_id"));
        try {
            performerService.changeStatus(performerId, statusId);
            ((Performer)request.getSession().getAttribute("performer")).setStatus(statusId);
            response.sendRedirect("/performer");
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
