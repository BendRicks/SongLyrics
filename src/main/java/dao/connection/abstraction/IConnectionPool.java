package dao.connection.abstraction;

import dao.exception.DAOException;

import java.sql.Connection;

public interface IConnectionPool {

    Connection getFreeConnection() throws DAOException;
    void retrieveConnection(final Connection connection);

}
