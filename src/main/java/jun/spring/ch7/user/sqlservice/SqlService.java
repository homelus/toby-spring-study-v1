package jun.spring.ch7.user.sqlservice;

import jun.spring.ch7.user.exception.SqlRetrievalFailureException;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
