package jun.spring.sqlservice.reader;

import jun.spring.sqlservice.registry.SqlRegistry;

public interface SqlReader {
    void read(SqlRegistry sqlRegistry);
}
