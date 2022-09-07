package dao.impl;

import dao.DAOConstants;
import dao.connection.ConnectionManager;
import dao.exception.DAOException;
import dao.impl.abstraction.AbstractDAO;
import dao.impl.abstraction.IUserDAO;
import entity.Track;
import entity.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO extends AbstractDAO implements IUserDAO {

    private final Logger logger = LogManager.getLogger(UserDAO.class);
    private static final String SAVE_USER_QUERY = "INSERT INTO `user` (`username`, `password`, `role_id`, `acc_status_id`) VALUES (?, ?, ?, ?);";
    private static final String UPDATE_USER_ROLE_QUERY = "UPDATE `user` SET `role_id` = ? WHERE `user_id` = ?;";
    private static final String UPDATE_ACC_STATUS_QUERY = "UPDATE `user` SET `acc_status_id` = ? WHERE `user_id` = ?;";
    private static final String UPDATE_USERNAME_QUERY = "UPDATE `user` SET `username` = ? WHERE `user_id` = ?;";
    private static final String UPDATE_PASSWORD_QUERY = "UPDATE `user` SET `password` = ? WHERE `user_id` = ?;";
    private static final String DELETE_USER_QUERY = "DELETE FROM `user` WHERE `user_id` = ?;";
    private static final String FIND_USER_BY_NAME_QUERY = "SELECT * FROM `user` WHERE `username` = ?;";
    private static final String FIND_USER_BY_ID_QUERY = "SELECT * FROM `user` WHERE `user_id` = ?;";
    private static final String SEARCH_USER_BY_NAME_QUERY = "SELECT * FROM `user` WHERE `username` LIKE ?";
    private static final String STATUS_SPECIFICATION = " AND `acc_status_id` = ?";
    private static final String ROLE_SPECIFICATION = " AND `role_id` = ?";
    private static final String SEARCH_PAGE_SPECIFICATION = " LIMIT ? OFFSET ?";
    private static final String COUNT_USERS_QUERY = "SELECT COUNT(*) FROM `user` WHERE `username` LIKE ?";
    private static final String COMPLEX_QUERY_END = ";";

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
                logger.error("Error while trying to create user in db ");
                throw new DAOException("Troubles inserting user into db", 401);
            }
        } catch (SQLException e){
            throw new DAOException(e, 400);
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
                user.setAccStatus(userFromDB.getInt(5));
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to find user in db " + e.getSQLState());
            throw new DAOException(e, 400);
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
                throw new DAOException("No such user", 402);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to find user by id in db " + e.getSQLState());
            throw new DAOException(e, 400);
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
                logger.error("Error while trying to delete user from db");
                throw new DAOException("Troubles with deleting user fro db", 403);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to delete user from db " + e.getSQLState());
            throw new DAOException(e, 400);
        } finally {
            close(deletePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public void updateUserStatus(Integer userId, Integer statusId) throws DAOException {
        updateParameter(userId, UPDATE_ACC_STATUS_QUERY, statusId);
    }

    @Override
    public void updateUserRole(Integer userId, Integer roleId) throws DAOException{
        updateParameter(userId, UPDATE_USER_ROLE_QUERY, roleId);
    }

    @Override
    public void updatePassword(Integer userId, String passwordHash) throws DAOException {
        updateParameter(userId, UPDATE_PASSWORD_QUERY, passwordHash);
    }

    @Override
    public void updateUsername(Integer userId, String username) throws DAOException{
        updateParameter(userId, UPDATE_USERNAME_QUERY, username);
    }

    private void updateParameter(Integer id, String query, Object newValue) throws DAOException {
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
                logger.error("Error while trying to update user in db ");
                throw new DAOException("Troubles updating user status", 404);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to update user in db " + e.getSQLState());
            throw new DAOException(e, 400);
        } finally {
            close(updatePreparedStatement);
            retrieve(connection);
        }
    }

    @Override
    public List<User> searchUsersByName(String name, Integer statusId, Integer roleId, Integer limit, Integer offset) throws DAOException {
        Connection connection = null;
        PreparedStatement findPreparedStatement = null;
        ResultSet usersFromDB = null;
        List<User> users = new ArrayList<>();
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                if (roleId == DAOConstants.ALL_ID) {
                    findPreparedStatement = connection.prepareStatement(SEARCH_USER_BY_NAME_QUERY + SEARCH_PAGE_SPECIFICATION + COMPLEX_QUERY_END);
                    findPreparedStatement.setInt(2, limit);
                    findPreparedStatement.setInt(3, offset);
                } else {
                    findPreparedStatement = connection.prepareStatement(SEARCH_USER_BY_NAME_QUERY + ROLE_SPECIFICATION + SEARCH_PAGE_SPECIFICATION + COMPLEX_QUERY_END);
                    findPreparedStatement.setInt(2, roleId);
                    findPreparedStatement.setInt(3, limit);
                    findPreparedStatement.setInt(4, offset);
                }
            } else {
                if (roleId == DAOConstants.ALL_ID) {
                    findPreparedStatement = connection.prepareStatement(SEARCH_USER_BY_NAME_QUERY + STATUS_SPECIFICATION + SEARCH_PAGE_SPECIFICATION + COMPLEX_QUERY_END);
                    findPreparedStatement.setInt(2, statusId);
                    findPreparedStatement.setInt(3, limit);
                    findPreparedStatement.setInt(4, offset);
                } else {
                    findPreparedStatement = connection.prepareStatement(SEARCH_USER_BY_NAME_QUERY + ROLE_SPECIFICATION + STATUS_SPECIFICATION + SEARCH_PAGE_SPECIFICATION + COMPLEX_QUERY_END);
                    findPreparedStatement.setInt(2, roleId);
                    findPreparedStatement.setInt(3, statusId);
                    findPreparedStatement.setInt(4, limit);
                    findPreparedStatement.setInt(5, offset);
                }
            }
            findPreparedStatement.setString(1, name);
            usersFromDB = findPreparedStatement.executeQuery();
            while (usersFromDB.next()){
                users.add(new User(usersFromDB.getInt(1), usersFromDB.getString(2),
                        usersFromDB.getString(3), usersFromDB.getInt(4), usersFromDB.getInt(5)));
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to search users in db " + e.getSQLState());
        } finally {
            close(findPreparedStatement);
            close(usersFromDB);
            retrieve(connection);
        }
        return users;
    }

    @Override
    public Integer countUsers(String name, Integer statusId, Integer roleId) throws DAOException {
        Connection connection = null;
        PreparedStatement countPreparedStatement = null;
        ResultSet countRS = null;
        Integer count = null;
        try {
            connection = getConnection(true);
            if (statusId == DAOConstants.ALL_ID) {
                if (roleId == DAOConstants.ALL_ID) {
                    countPreparedStatement = connection.prepareStatement(COUNT_USERS_QUERY + COMPLEX_QUERY_END);
                } else {
                    countPreparedStatement = connection.prepareStatement(COUNT_USERS_QUERY + ROLE_SPECIFICATION + COMPLEX_QUERY_END);
                    countPreparedStatement.setInt(2, roleId);
                }
            } else {
                if (roleId == DAOConstants.ALL_ID) {
                    countPreparedStatement = connection.prepareStatement(COUNT_USERS_QUERY + STATUS_SPECIFICATION + COMPLEX_QUERY_END);
                    countPreparedStatement.setInt(2, statusId);
                } else {
                    countPreparedStatement = connection.prepareStatement(COUNT_USERS_QUERY + ROLE_SPECIFICATION + STATUS_SPECIFICATION + COMPLEX_QUERY_END);
                    countPreparedStatement.setInt(2, roleId);
                    countPreparedStatement.setInt(3, statusId);
                }
            }
            countPreparedStatement.setString(1, name);
            countRS = countPreparedStatement.executeQuery();
            if (countRS.next()){
                count = countRS.getInt(1);
            }
        } catch (SQLException e){
            logger.error("SQL error while trying to count users in db " + e.getSQLState());
            throw new DAOException(e, 405);
        } finally {
            close(countPreparedStatement);
            retrieve(connection);
        }
        return count;
    }

}
