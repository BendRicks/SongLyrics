package command.impl.main;

import command.impl.abstraction.Command;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class ChangeLanguageCommand implements Command {

    private final Logger logger = LogManager.getLogger(ChangeLanguageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String lang = request.getParameter("lang");
        request.getSession().setAttribute("locale", lang);
        try {
            response.sendRedirect("/");
        } catch (IOException e) {
            logger.error("Can't find file to redirect");
            throw new RuntimeException(e);
        }
    }
}
