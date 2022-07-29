package dao.impl.abstraction;

import dao.connection.ConnectionManager;
import dao.exception.DAOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class AbstractDAO {

    private ConnectionManager manager = null;

    public AbstractDAO(final ConnectionManager manager){
        this.manager = manager;
    }

    protected Connection getConnection(final boolean autoCommit) throws DAOException, SQLException {
        final Connection connection = manager.getFreeConnection();
        connection.setAutoCommit(autoCommit);
        return connection;
    }

    protected void retrieve(final Connection connection){
        manager.retrieveConnection(connection);
    }

//    protected PreparedStatement getPreparedStatement(final String query, final Connection connection,
//                                                     final List<Object> params) throws SQLException {
//        final PreparedStatement preparedStatement = connection.prepareStatement(query);
//        for (int i = 0; i < params.size(); i++) {
//            final Object parameter = params.get(i);
//            if (parameter.getClass() == Long.class){
//                preparedStatement.setLong(i+1, (Long) parameter);
//            } else if (parameter.getClass() == Integer.class) {
//                preparedStatement.setInt(i+1, (Integer) parameter);
//            } else if (parameter.getClass() == String.class) {
//                preparedStatement.setString(i+1, (String) parameter);
//            }
//        }
//        return preparedStatement;
//    }
//
    protected void close(final ResultSet... resultSets){
        if (resultSets != null) {
            for (final ResultSet resultSet : resultSets){
                if (resultSet != null) {
                    try {
                        resultSet.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        //TODO логгирование
                    }
                }
            }
        }
    }

    protected void close(final PreparedStatement... preparedStatements){
        if (preparedStatements != null) {
            for (final PreparedStatement preparedStatement : preparedStatements){
                if (preparedStatement != null) {
                    try {
                        preparedStatement.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                        //TODO логгирование
                    }
                }
            }
        }
    }

}
