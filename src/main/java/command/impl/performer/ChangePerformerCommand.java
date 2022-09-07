package command.impl.performer;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceConstants;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.PerformerService;

import java.io.IOException;

public class ChangePerformerCommand implements Command {

    private final Logger logger = LogManager.getLogger(ChangePerformerCommand.class);
    private final PerformerService performerService;

    public ChangePerformerCommand() {
        performerService = ServiceFactory.getInstance().getPerformerService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        int performerId = Integer.parseInt(request.getParameter("item_id"));
        String name = request.getParameter("name");
        String coverPath = request.getParameter("cover_path");
        String description = request.getParameter("description");
        try {
            performerService.changePerformer(performerId, name, coverPath, description);
            request.getSession().setAttribute("performer", performerService.getPerformerInfo(performerId, ServiceConstants.ALL_INFO));
            response.sendRedirect("/performer");
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException) e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/change/performer?err_code=" + code);
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
