package command.impl.user.admin;

import command.impl.abstraction.Command;
import dao.DAOConstants;
import dao.exception.DAOException;
import entity.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.UserService;
import java.io.IOException;

public class ChangeUserStatusCommand implements Command {

    private final Logger logger = LogManager.getLogger(ChangeUserStatusCommand.class);
    private final UserService userService;

    public ChangeUserStatusCommand() {
        userService = ServiceFactory.getInstance().getUserService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        UserSession userSession = (UserSession) request.getSession().getAttribute("USER_SESSION_INFO");
        if (userSession != null && userSession.getRoleId() == DAOConstants.ADMIN_ID) {
            int userId = Integer.parseInt(request.getParameter("user_id"));
            int statusId = Integer.parseInt(request.getParameter("status_id"));
            try {
                userService.changeStatus(userId, statusId);
                response.sendRedirect("/search/user");
            } catch (ServiceException e) {
                int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                        e.getExceptionCode();
                try {
                    response.sendRedirect("/search/user?err_code="+code);
                } catch (IOException ex) {
                    logger.error("Can't find file to redirect");
                    throw new RuntimeException(ex);
                }
            } catch (IOException e){
                logger.error("Can't find file to redirect");
                throw new RuntimeException(e);
            }
        } else {
            try {
                response.sendRedirect("/");
            } catch (IOException e) {
                logger.error("Can't find file to redirect");
                throw new RuntimeException(e);
            }
        }
    }
}
