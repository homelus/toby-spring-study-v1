package jun.spring.ch7.user.sqlservice;

import jun.spring.ch7.user.exception.SqlNotFoundException;

public interface SqlRegistry {

    void registerSql(String key, String sql);

    String findSql(String key) throws SqlNotFoundException;

}
