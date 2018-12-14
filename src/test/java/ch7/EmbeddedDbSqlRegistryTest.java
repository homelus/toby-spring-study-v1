package ch7;

import jun.spring.ch7.user.exception.SqlUpdateFailureException;
import jun.spring.sqlservice.registry.EmbeddedDbSqlRegistry;
import jun.spring.sqlservice.registry.UpdatableSqlRegistry;
import org.junit.After;
import org.junit.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class EmbeddedDbSqlRegistryTest extends AbstractUpdatableSqlRegistryTest{

    EmbeddedDatabase db;

    @Override
    protected UpdatableSqlRegistry createUpdatableSqlRegistry() {
        db = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.HSQL)
                .addScript("classpath:sqlRegistrySchema.sql")
                .build();

        EmbeddedDbSqlRegistry embeddedDbSqlRegistry = new EmbeddedDbSqlRegistry();
        embeddedDbSqlRegistry.setDataSource(db);

        return embeddedDbSqlRegistry;
    }

    @Test
    public void transactionalUpdate() {
        // 초기상태 확인, 롤백과 비교를 위한 데이터
        checkFindResult("SQL1", "SQL2", "SQL3");

        Map<String, String> sqlmap = new HashMap<>();
        sqlmap.put("KEY1", "Modified1");
        sqlmap.put("KEY999!!", "Modified999");

        try {
            sqlRegistry.updateSql(sqlmap);
            fail();
        } catch (SqlUpdateFailureException e) {}

        checkFindResult("SQL1", "SQL2", "SQL3");
    }

    @After
    public void tearDown() {
        db.shutdown();
    }
}
