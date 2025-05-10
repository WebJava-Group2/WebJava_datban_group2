package org.datban.webjava.repositories.base;

import java.sql.SQLException;
import java.util.List;


public interface IBaseRepository<T, ID> {
    /**
     * 1.getAll()
     2. getWithPaginate()
     3. insert
     4. update
     5. delete
     * @return
     * @throws java.sql.SQLException
     */

    List<T> getAll() throws SQLException;

    List<T> getWithPaginate(int page, int size) throws SQLException;

    void insert(T entity) throws SQLException;

    void update(T entity) throws SQLException;

    void delete(ID id) throws SQLException;
}



