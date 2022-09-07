package command.impl.album;

import command.impl.abstraction.Command;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public class GetChangeAlbumPageCommand implements Command {

    private final Logger logger = LogManager.getLogger(GetChangeAlbumPageCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            response.sendRedirect("/change/album");
        } catch (IOException e) {
            logger.error("Can't find file to redirect");
            throw new RuntimeException(e);
        }
    }
}