package ch7;

import jun.spring.sqlservice.registry.ConcurrentHashMapSqlRegistry;
import jun.spring.sqlservice.registry.UpdatableSqlRegistry;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
