package ch7;

import jun.spring.ch7.user.sqlservice.registry.ConcurrentHashMapSqlRegistry;
import jun.spring.ch7.user.sqlservice.registry.UpdatableSqlRegistry;

public class ConcurrentHashMapSqlRegistryTest extends AbstractUpdatableSqlRegistryTest {
    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        return new ConcurrentHashMapSqlRegistry();
    }
}
