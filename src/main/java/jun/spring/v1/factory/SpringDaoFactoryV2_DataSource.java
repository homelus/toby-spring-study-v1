package jun.spring.v1.factory;

import jun.spring.v1.dao.UserDaoV3_3RelationSeparation;
import jun.spring.v1.dao.UserDaoV5_MethodInjection;
import jun.spring.v1.dao.UserDaoV6_DataSource;
import jun.spring.v1.dao.support.ConnectionMaker;
import jun.spring.v1.dao.support.client.DConnectionMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;

import javax.sql.DataSource;

@Configuration
public class SpringDaoFactoryV2_DataSource {

    @Bean // 오브젝트라는 인식표
    public UserDaoV6_DataSource userDao() {
        // 런타임 의존관계 설정(의존관계 주입)
        UserDaoV6_DataSource userDao = new UserDaoV6_DataSource();
        userDao.setDataSource(dataSource());
        return userDao;
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

    @Bean
    public DataSource dataSource() {
        SimpleDriverDataSource dataSource = new SimpleDriverDataSource();

        dataSource.setDriverClass(org.mariadb.jdbc.Driver.class);
        dataSource.setUrl("jdbc:mariadb://localhost/spring_test");
        dataSource.setUsername("root");
        dataSource.setPassword("1234");

        return dataSource;
    }

}
