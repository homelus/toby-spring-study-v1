package ch7.context;

import ch7.DummyMailSender;
import jun.spring.ch7.user.service.UserService;
import jun.spring.ch7.user.dao.UserDao;
import jun.spring.ch7.user.dao.UserDaoJdbc;
import jun.spring.ch7.user.service.UserServiceImpl;
import jun.spring.ch7.user.sqlservice.OxmSqlService;
import jun.spring.ch7.user.sqlservice.SqlAdminService;
import jun.spring.ch7.user.sqlservice.SqlService;
import jun.spring.ch7.user.sqlservice.registry.EmbeddedDbSqlRegistry;
import jun.spring.ch7.user.sqlservice.registry.SqlRegistry;
import jun.spring.ch7.user.sqlservice.registry.UpdatableSqlRegistry;
import org.mariadb.jdbc.Driver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.mail.MailSender;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

import static ch7.UserServiceTest.*;
import static org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType.HSQL;

@Configuration
@EnableTransactionManagement
public class TestApplicationContext {

    /**
     * DB 연결 및 트랜재겻ㄴ
     */

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(Driver.class);
        dataSource.setUrl("jdbc:mariadb://localhost/spring_test_dev");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");
        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        DataSourceTransactionManager tm = new DataSourceTransactionManager();
        tm.setDataSource(dataSource());
        return tm;
    }

    /**
     * 애플리케이션 로직 & 테스트
     */

    @Bean
    public UserDao userDao() {
        UserDaoJdbc userDao = new UserDaoJdbc();
        userDao.setSqlService(sqlService());
        userDao.setDataSource(dataSource());
        return userDao;
    }

    @Bean
    public UserService userService() {
        UserServiceImpl userService = new UserServiceImpl();
        userService.setUserDao(userDao());
        userService.setMailSender(mailSender());
        return userService;
    }

    public SqlAdminService sqlAdminService() {
        SqlAdminService sqlAdminService = new SqlAdminService();
        sqlAdminService.setUpdatableSqlRegistry(updatableSqlRegistry());
        return sqlAdminService;
    }

    @Bean
    public UserService testUserService() {
        TestUserService testUserService = new TestUserService();
        testUserService.setUserDao(userDao());
        testUserService.setMailSender(mailSender());
        return testUserService;
    }

    @Bean
    public MailSender mailSender() {
        return new DummyMailSender();
    }

    /**
     * SQL 서비스
     */

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
