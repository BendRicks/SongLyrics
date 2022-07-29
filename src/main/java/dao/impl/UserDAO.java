package dao.impl;

import dao.connection.ConnectionManager;
import dao.exception.DAOException;
import dao.impl.abstraction.AbstractDAO;
import dao.impl.abstraction.IUserDAO;
import entity.User;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO extends AbstractDAO implements IUserDAO {

    private static final String SAVE_USER_QUERY = "INSERT INTO `user` (`username`, `password`, `role_id`, `acc_status_id`) VALUES (?, ?, ?, ?);";
    private static final String UPDATE_USER_ROLE_QUERY = "UPDATE `user` SET `role_id` = ? WHERE `user_id` = ?;";
    private static final String UPDATE_ACC_STATUS_QUERY = "UPDATE `user` SET `acc_status_id` = ? WHERE `user_id` = ?;";
    private static final String UPDATE_USERNAME_QUERY = "UPDATE `user` SET `username` = ? WHERE `user_id` = ?;";
    private static final String UPDATE_PASSWORD_QUERY = "UPDATE `user` SET `password` = ? WHERE `user_id` = ?;";
    private static final String DELETE_USER_QUERY = "DELETE FROM `user` WHERE `user_id` = ?;";
    private static final String FIND_USER_BY_NAME_QUERY = "SELECT * FROM `user` WHERE `username` = ?;";
    private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM `user` WHERE `user_id` = ?;";
    private static final String SEARCH_USER_QUERY = "SELECT * FROM `user` WHERE `username` LIKE ?;";

    public UserDAO(ConnectionManager manager){
        super(manager);
    }

    @Override
    public User save(User user) throws DAOException {
        Connection connection = null;
        PreparedStatement savePreparedStatement = null;
        try {
            connection = getConnection(true);
            savePreparedStatement = connection.prepareStatement(SAVE_USER_QUERY);
            savePreparedStatement.setString(1, user.getUsername());
            savePreparedStatement.setString(2, user.getPasswordHash());
            savePreparedStatement.setInt(3, DAOConstants.USER_ID);
            savePreparedStatement.setInt(4, DAOConstants.INNOCENT_ID);
            int result = savePreparedStatement.executeUpdate();
            if (result == 0){
                throw new DAOException("Troubles inserting user into db", 111);
            }
        } catch (SQLException e){
            throw new DAOException(e, 110);
        } finally {
           close(savePreparedStatement);
           retrieve(connection);
        }
        return find(user);
    }

    @Override
    public User find(User user) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet userFromDB = null;
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(FIND_USER_BY_NAME_QUERY);
            findPreparedStatement.setString(1, user.getUsername());
            userFromDB = findPreparedStatement.executeQuery();
            if (userFromDB.next()){
                user.setId(userFromDB.getInt(1));
                user.setPasswordHash(userFromDB.getString(3));
                user.setRole(userFromDB.getInt(4));
            } else {
                throw new DAOException("No such user", 122);
            }
        } catch (SQLException e){
            throw new DAOException(e, 120);
        } finally {
            close(findPreparedStatement);
            close(userFromDB);
            retrieve(connection);
        }
        return user;
    }

    @Override
    public User findById(int id) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet userFromDB = null;
        User user = null;
        try {
            connection = getConnection(true);
            findPreparedStatement = connection.prepareStatement(FIND_USER_BY_ID_QUERY);
            findPreparedStatement.setInt(1, id);
            userFromDB = findPreparedStatement.executeQuery();
            if (userFromDB.next()){
                user = new User(userFromDB.getInt(1), userFromDB.getString(2),
                        userFromDB.getString(3), userFromDB.getInt(4), userFromDB.getInt(5));
            } else {
                throw new DAOException("No such user", 121);
            }
        } catch (SQLException e){
            throw new DAOException(e, 120);
        } finally {
            close(findPreparedStatement);
            close(userFromDB);
            retrieve(connection);
        }
        return user;
    }

    @Override
    public void delete(User user) throws DAOException {
        Connection connection = null;
        PreparedStatement deletePreparedStatement = null;
        try {
            connection = getConnection(true);
            deletePreparedStatement = connection.prepareStatement(DELETE_USER_QUERY);
            deletePreparedStatement.setInt(1, user.getId());
            int result = deletePreparedStatement.executeUpdate();
            if (result == 0){
                throw new DAOException("Troubles with deleting user fro db", 131);
            }
        } catch (SQLException e){
            throw new DAOException(e, 130);
        } finally {
            close(deletePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public User updateUserStatus(Integer userId, Integer statusId) throws DAOException {
        return updateParameter(userId, UPDATE_ACC_STATUS_QUERY, statusId);
    }

    @Override
    public User updateUserRole(Integer userId, Integer roleId) throws DAOException{
        return updateParameter(userId, UPDATE_USER_ROLE_QUERY, roleId);
    }

    @Override
    public User updatePassword(Integer userId, String passwordHash) throws DAOException {
        return updateParameter(userId, UPDATE_PASSWORD_QUERY, passwordHash);
    }

    @Override
    public User updateUsername(Integer userId, String username) throws DAOException{
        return updateParameter(userId, UPDATE_USERNAME_QUERY, username);
    }

    @Override
    public User updateParameter(Integer id, String query, Object newValue) throws DAOException {
        Connection connection = null;
        PreparedStatement updatePreparedStatement = null;
        try {
            connection = getConnection(true);
            updatePreparedStatement = connection.prepareStatement(query);
            if (newValue.getClass() == Integer.class){
                updatePreparedStatement.setInt(1, (Integer)newValue);
            } else if (newValue.getClass() == String.class) {
                updatePreparedStatement.setString(1, (String)newValue);
            }
            updatePreparedStatement.setInt(2, id);
            int result = updatePreparedStatement.executeUpdate();
            if (result == 0) {
                throw new DAOException("Troubles updating user status", 141);
            }
        } catch (SQLException e){
            throw new DAOException(e, 140);
        } finally {
            close(updatePreparedStatement);
            retrieve(connection);
        }
        return findById(id);
    }
}
