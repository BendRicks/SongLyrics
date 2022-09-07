package command.impl.user;

import command.impl.abstraction.Command;
import dao.exception.DAOException;
import entity.User;
import entity.UserSession;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import service.ServiceFactory;
import service.exception.ServiceException;
import service.impl.UserService;

import java.io.IOException;

public class SignUpCommand implements Command {

    private final Logger logger = LogManager.getLogger(SignUpCommand.class);
    private final UserService userService;

    public SignUpCommand(){
        userService = ServiceFactory.getInstance().getUserService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String passwordRepeat = request.getParameter("password_repeat");
        if (!password.equals(passwordRepeat)){
            try {
                response.sendRedirect("/register?err_code=" + 1001);
            }  catch (IOException e) {
                logger.error("Can't find file to redirect");
                throw new RuntimeException(e);
            }
        }
        try {
            UserSession userSession = new UserSession(userService.signUp(new User(username), password));
            request.getSession().setAttribute("USER_SESSION_INFO", userSession);
            response.sendRedirect("/");
        } catch (ServiceException e) {
            int code = e.isDAO() ? ((DAOException)e.getCause()).getExceptionCode() :
                    e.getExceptionCode();
            try {
                response.sendRedirect("/register?err_code="+code);
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
