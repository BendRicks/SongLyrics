package command.impl.user.admin;

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
import java.util.List;

public class TurnUsersPageCommand implements Command {

    private final Logger logger = LogManager.getLogger(TurnUsersPageCommand.class);
    private final UserService userService;

    public TurnUsersPageCommand(){
        userService = ServiceFactory.getInstance().getUserService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        String query = (String) request.getSession().getAttribute("query");
        int status = (int) request.getSession().getAttribute("acc_status");
        int role = (int) request.getSession().getAttribute("role");
        int page = Integer.parseInt(request.getParameter("page"));
        try {
            List<UserSession> userSessions = userService.searchUsers(query, status, role, page, 20);
            request.getSession().setAttribute("users", userSessions);
            request.getSession().setAttribute("page", page);
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
        } catch (IOException e) {
            logger.error("Can't find file to redirect");
            throw new RuntimeException(e);
        }
    }
}
