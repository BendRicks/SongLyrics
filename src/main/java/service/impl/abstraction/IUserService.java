package service.impl.abstraction;

import entity.User;
import service.exception.ServiceException;

public interface IUserService {

    User signIn(User user, String password) throws ServiceException;
    User signUp(User user, String password) throws ServiceException;
    User changeRole(Integer userId, int roleId) throws ServiceException;
    User changeStatus(Integer userId, int statusId) throws ServiceException;
    User changePassword(Integer userId, String oldPassword, String newPassword) throws ServiceException;
    User changeUsername(Integer userId, String password, String username) throws ServiceException;

}
