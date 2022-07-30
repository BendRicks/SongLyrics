package service.impl;

import dao.DAOFactory;
import dao.exception.DAOException;
import dao.impl.UserDAO;
import entity.User;
import service.impl.abstraction.IUserService;
import service.exception.ServiceException;
import service.security.PasswordHasher;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class UserService implements IUserService {

    private final UserDAO userDAO;

    public UserService() {
        userDAO = DAOFactory.getInstance().getUserDAO();
    }

    @Override
    public User signIn(User user, String password) throws ServiceException {
        try {
            user = userDAO.find(user);
            if (!PasswordHasher.validatePassword(password, user.getPasswordHash())) {
                throw new ServiceException("Invalid password", 611);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ServiceException("Troubles validating password", 610);
        }
        return user;
    }

    @Override
    public User signUp(User user, String password) throws ServiceException {
        try {
            user.setPasswordHash(PasswordHasher.hash(password));
            user = userDAO.save(user);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ServiceException("Troubles hashing password", 610);
        }
        return user;
    }

    @Override
    public User changeRole(Integer userId, int roleId) throws ServiceException {
        User user = null;
        try {
            user = userDAO.updateUserRole(userId, roleId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public User changeStatus(Integer userId, int statusId) throws ServiceException {
        User user = null;
        try {
            user = userDAO.updateUserStatus(userId, statusId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        return user;
    }

    @Override
    public User changePassword(Integer userId, String oldPassword, String newPassword) throws ServiceException {
        User user = null;
        try {
            user = userDAO.findById(userId);
            if (PasswordHasher.validatePassword(oldPassword, user.getPasswordHash())) {
                user = userDAO.updatePassword(userId, PasswordHasher.hash(newPassword));
            } else {
                throw new ServiceException("Invalid password", 611);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ServiceException("Troubles hashing/validating password", 620);
        }
        return user;
    }

    @Override
    public User changeUsername(Integer userId, String password, String username) throws ServiceException {
        User user = null;
        try {
            user = userDAO.findById(userId);
            if (PasswordHasher.validatePassword(password, user.getPasswordHash())) {
                user = userDAO.updateUsername(userId, username);
            } else {
                throw new ServiceException("Invalid password", 611);
            }
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new ServiceException("Troubles hashing/validating password", 620);
        }
        return user;
    }
}
