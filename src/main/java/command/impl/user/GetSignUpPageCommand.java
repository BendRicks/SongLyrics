package command.impl.user;

import command.impl.abstraction.Command;
import entity.UserSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GetSignUpPageCommand implements Command {

    private final Logger logger = LogManager.getLogger(GetSignUpPageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        UserSession userInfo = (UserSession) request.getSession().getAttribute("USER_SESSION_INFO");
        try {
            if (userInfo == null) {
                response.sendRedirect("/register");
            } else {
                response.sendRedirect("/");
            }
        } catch (IOException e) {
            logger.error("Can't find file to redirect");
            throw new RuntimeException(e);
        }
    }

}
