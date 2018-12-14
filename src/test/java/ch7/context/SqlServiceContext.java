package ch7.context;

import jun.spring.ch7.user.sqlservice.OxmSqlService;
import jun.spring.ch7.user.sqlservice.SqlService;
import jun.spring.ch7.user.sqlservice.registry.EmbeddedDbSqlRegistry;
import jun.spring.ch7.user.sqlservice.registry.SqlRegistry;
import jun.spring.ch7.user.sqlservice.registry.UpdatableSqlRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.sql.DataSource;

import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
public class SqlServiceContext {

    @Bean
    public SqlService sqlService() {
        OxmSqlService sqlService = new OxmSqlService();
        sqlService.setUnmarshaller(unmarshaller());
        sqlService.setSqlRegistry(sqlRegistry());
        return sqlService;
    }

    @Bean
    public SqlRegistry sqlRegistry() {
        EmbeddedDbSqlRegistry sqlRegistry = new EmbeddedDbSqlRegistry();
        sqlRegistry.setDataSource(embeddedDatabase());
        return sqlRegistry;
    }

    @Bean
    public UpdatableSqlRegistry updatableSqlRegistry() {
        EmbeddedDbSqlRegistry updatableSqlRegistry = new EmbeddedDbSqlRegistry();
        updatableSqlRegistry.setDataSource(embeddedDatabase());
        return updatableSqlRegistry;
    }

    @Bean
    public Unmarshaller unmarshaller() {
        Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
        marshaller.setContextPath("jun.spring.ch7.user.sqlservice.jaxb");
        return marshaller;
    }

    @Bean
    public DataSource embeddedDatabase() {
        return new EmbeddedDatabaseBuilder()
                .setName("embeddedDatabase")
                .setType(HSQL)
                .addScript("classpath:sqlRegistrySchema.sql")
                .build();
    }

}
