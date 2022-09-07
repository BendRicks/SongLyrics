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

public class SearchUsersCommand implements Command {

    private final Logger logger = LogManager.getLogger(SearchUsersCommand.class);
    private final UserService userService;

    public SearchUsersCommand(){
        userService = ServiceFactory.getInstance().getUserService();
    }

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) {
        try {
            String query = request.getParameter("query");
            int role = Integer.parseInt(request.getParameter("role"));
            int status = Integer.parseInt(request.getParameter("acc_status"));
            int pages = userService.countAlikeUsersPages(query, status, role, 20);
            List<UserSession> userSessions = userService.searchUsers(query, status, role, 0, 20);
            request.getSession().setAttribute("query", query);
            request.getSession().setAttribute("status", status);
            request.getSession().setAttribute("role", role);
            request.getSession().setAttribute("users", userSessions);
            request.getSession().setAttribute("pages", pages);
            request.getSession().setAttribute("page", 1);
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
        } catch (IOException | NumberFormatException e) {
            logger.error("Can't find file to redirect");
            throw new RuntimeException(e);
        }
    }
}
