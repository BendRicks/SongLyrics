package dao.impl.abstraction;

import dao.exception.DAOException;
import dao.impl.UserDAO;
import entity.User;

public interface IUserDAO extends DAOFrame<User>{

    User updateUserStatus(Integer userId, Integer statusId) throws DAOException;
    User updateUserRole(Integer userId, Integer roleId) throws DAOException;
    User updatePassword(Integer userId, String passwordHash) throws DAOException;
    User updateUsername(Integer userId, String username) throws DAOException;

}