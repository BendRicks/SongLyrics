package service.impl;

import dao.DAOFactory;
import dao.exception.DAOException;
import dao.impl.UserDAO;
import entity.User;
import entity.UserSession;
import service.ServiceConverter;
import service.impl.abstraction.IUserService;
import service.exception.ServiceException;
import service.security.PasswordHasher;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;

public class UserService implements IUserService {

    private final UserDAO userDAO;

    public UserService() {
        userDAO = DAOFactory.getInstance().getUserDAO();
    }

    @Override
    public User signIn(User user, String password) throws ServiceException {
        try {
            user = userDAO.find(user);
            if (user.getId() == null) {
                throw new ServiceException("No such user", 501);
            } else {
                if (!PasswordHasher.validatePassword(password, user.getPasswordHash())) {
                    throw new ServiceException("Invalid password", 502);
                }
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ServiceException("Troubles validating password", 500);
        }
        return user;
    }

    @Override
    public User signUp(User user, String password) throws ServiceException {
        try {
            if (!user.getUsername().equals("")) {
                if (userDAO.find(user).getId() != null){
                    throw new ServiceException("User already exists", 503);
                }
                user.setPasswordHash(PasswordHasher.hash(password));
                user = userDAO.save(user);
            } else {
                throw new ServiceException("Empty username", 504);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ServiceException("Troubles hashing password", 500);
        }
        return user;
    }

    @Override
    public void changeRole(Integer userId, int roleId) throws ServiceException {
        try {
            userDAO.updateUserRole(userId, roleId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void changeStatus(Integer userId, int statusId) throws ServiceException {
        try {
            userDAO.updateUserStatus(userId, statusId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void changePassword(Integer userId, String oldPassword, String newPassword) throws ServiceException {
        User user;
        try {
            user = userDAO.findById(userId);
            if (PasswordHasher.validatePassword(oldPassword, user.getPasswordHash())) {
                userDAO.updatePassword(userId, PasswordHasher.hash(newPassword));
            } else {
                throw new ServiceException("Invalid password", 502);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ServiceException("Troubles hashing/validating password", 500);
        }
    }

    @Override
    public User changeUsername(Integer userId, String password, String username) throws ServiceException {
        User user;
        try {
            user = userDAO.findById(userId);
            if (PasswordHasher.validatePassword(password, user.getPasswordHash())) {
                if (!username.equals("")) {
                    userDAO.updateUsername(userId, username);
                    user.setUsername(username);
                } else {
                    throw new ServiceException("Empty username", 504);
                }
            } else {
                throw new ServiceException("Invalid password", 502);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ServiceException("Troubles hashing/validating password", 500);
        }
        return user;
    }

    @Override
    public List<UserSession> searchUsers(String strToSearch, int statusId, int roleId,  int page, int itemsOnPage) throws ServiceException {
        List<User> users;
        try {
            page = Math.max(page, 1);
            users = userDAO.searchUsersByName("%" + strToSearch + "%", statusId, roleId, itemsOnPage, (page - 1) * itemsOnPage);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return ServiceConverter.convertUsersToUserSessions(users);
    }

    @Override
    public Integer countAlikeUsersPages(String strToSearch, int statusId, int roleId, int itemsOnPage) throws ServiceException {
        int pages;
        try {
            int itemsAmount = userDAO.countUsers("%" + strToSearch + "%", statusId, roleId);
            pages = (itemsAmount/itemsOnPage) + (itemsAmount % itemsOnPage != 0 ? 1 : 0);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return pages;
    }

}
