package dao.impl.abstraction;

import dao.exception.DAOException;
import entity.Entity;

public interface DAOFrame<T extends Entity> {

    T save(T t) throws DAOException;
    T find(T t) throws DAOException;
    T findById(int id) throws DAOException;
    void delete(T t) throws DAOException;

}
