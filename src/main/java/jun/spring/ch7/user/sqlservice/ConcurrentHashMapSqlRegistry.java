package jun.spring.ch7.user.sqlservice;

import jun.spring.ch7.user.exception.SqlNotFoundException;
import jun.spring.ch7.user.exception.SqlUpdateFailureException;

import java.util.Map;

public class ConcurrentHashMapSqlRegistry implements UpdatableSqlRegistry {
    @Override
    public void updateSql(String key, String sql) throws SqlUpdateFailureException {

    }

    @Override
    public void updateSql(Map<String, String> sqlmap) throws SqlUpdateFailureException {

    }

    @Override
    public void registerSql(String key, String sql) {

    }

    @Override
    public String findSql(String key) throws SqlNotFoundException {
        return null;
    }
}
