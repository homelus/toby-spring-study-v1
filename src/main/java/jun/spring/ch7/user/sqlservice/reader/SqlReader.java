package jun.spring.ch7.user.sqlservice.reader;

import jun.spring.ch7.user.sqlservice.registry.SqlRegistry;

public interface SqlReader {

    void read(SqlRegistry sqlRegistry);

}
