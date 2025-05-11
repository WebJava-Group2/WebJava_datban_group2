package org.datban.webjava.repositories.base;

import java.sql.SQLException;
import java.util.List;


public interface IBaseRepository<T, ID> {
    List<T> getAll() throws SQLException;

    T getById(ID id) throws SQLException;

    List<T> getWithPaginate(int page, int size) throws SQLException;

    void insert(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    void delete(ID id) throws SQLException;
}



