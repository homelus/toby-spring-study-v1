package jun.spring.sqlservice;

import jun.spring.ch7.user.exception.SqlNotFoundException;
import jun.spring.ch7.user.exception.SqlRetrievalFailureException;
import jun.spring.sqlservice.reader.SqlReader;
import jun.spring.sqlservice.registry.SqlRegistry;

import javax.annotation.PostConstruct;

public class BaseSqlService implements SqlService {

    protected SqlReader sqlReader;
    protected SqlRegistry sqlRegistry;

    public void setSqlReader(SqlReader sqlReader) {
        this.sqlReader = sqlReader;
    }

    public void setSqlRegistry(SqlRegistry sqlRegistry) {
        this.sqlRegistry = sqlRegistry;
    }

    @PostConstruct
    public void loadSql() {
        this.sqlReader.read(sqlRegistry);
    }

    @Override
    public String getSql(String key) throws SqlRetrievalFailureException {
        try {
            return sqlRegistry.findSql(key);
        } catch (SqlNotFoundException e) {
            throw new SqlRetrievalFailureException(e);
        }
    }
}
