package service.impl.abstraction;

import entity.Track;
import entity.User;
import entity.UserSession;
import service.exception.ServiceException;

import java.util.List;

public interface IUserService {

    User signIn(User user, String password) throws ServiceException;
    User signUp(User user, String password) throws ServiceException;
    void changeRole(Integer userId, int roleId) throws ServiceException;
    void changeStatus(Integer userId, int statusId) throws ServiceException;
    void changePassword(Integer userId, String oldPassword, String newPassword) throws ServiceException;
    User changeUsername(Integer userId, String password, String username) throws ServiceException;

    List<UserSession> searchUsers(String strToSearch, int statusId, int roleId, int page, int itemsOnPage) throws ServiceException;
    Integer countAlikeUsersPages(String strToSearch, int statusId, int roleId, int itemsOnPage) throws ServiceException;

}
