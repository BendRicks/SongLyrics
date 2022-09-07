package command.impl.user;

import command.impl.abstraction.Command;
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

public class ChangeUsernameCommand implements Command {

    private final Logger logger = LogManager.getLogger(ChangeUsernameCommand.class);
    private final UserService userService;

    public ChangeUsernameCommand(){
        userService = ServiceFactory.getInstance().getUserService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        UserSession userInfo = (UserSession) request.getSession().getAttribute("USER_SESSION_INFO");
        String newUsername = request.getParameter("new_username");
        String password = request.getParameter("password");
        try {
            UserSession userSession = new UserSession(userService.changeUsername(userInfo.getId(), password, newUsername));
            request.getSession().setAttribute("USER_SESSION_INFO", userSession);
            response.sendRedirect("/user");
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/user?err_code="+code);
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
