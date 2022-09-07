package command.impl.user;

import command.impl.abstraction.Command;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class LogOutCommand implements Command {

    private final Logger logger = LogManager.getLogger(LogOutCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        request.getSession().removeAttribute("USER_SESSION_INFO");
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            logger.error("Can't find file to redirect");
            throw new RuntimeException(e);
        }
    }

}