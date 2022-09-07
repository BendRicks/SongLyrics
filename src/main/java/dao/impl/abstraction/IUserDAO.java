package dao.impl.abstraction;

import dao.exception.DAOException;
import dao.impl.UserDAO;
import entity.Track;
import entity.User;
import entity.UserSession;
import service.impl.UserService;

import java.util.List;

public interface IUserDAO extends DAOFrame<User>{

    void updateUserStatus(Integer userId, Integer statusId) throws DAOException;
    void updateUserRole(Integer userId, Integer roleId) throws DAOException;
    void updatePassword(Integer userId, String passwordHash) throws DAOException;
    void updateUsername(Integer userId, String username) throws DAOException;
    List<User> searchUsersByName(String name, Integer statusId, Integer roleId, Integer limit, Integer offset) throws DAOException;
    Integer countUsers(String name, Integer statusId, Integer roleId) throws DAOException;

}