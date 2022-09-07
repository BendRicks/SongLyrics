package command.impl.performer;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import entity.Performer;
import entity.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.PerformerService;

import java.io.IOException;

public class CreatePerformerCommand implements Command {

    private final Logger logger = LogManager.getLogger(CreatePerformerCommand.class);
    private final PerformerService performerService;

    public CreatePerformerCommand(){
        performerService = ServiceFactory.getInstance().getPerformerService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        UserSession userSession = (UserSession) request.getSession().getAttribute("USER_SESSION_INFO");
        String name = request.getParameter("name");
        String coverPath = request.getParameter("cover_path");
        String description = request.getParameter("description");
        try {
            Performer performer = performerService.createPerformer(name, description, coverPath);
            performerService.addPerformerCreator(performer.getId(), userSession.getId());
            request.getSession().setAttribute("performer_creator", userSession);
            request.getSession().setAttribute("performer", performer);
            response.sendRedirect("/performer");
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/create/performer?err_code="+code);
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
