package jun.spring.v1.factory;

import jun.spring.v1.dao.UserDaoV3_3RelationSeparation;
import jun.spring.v1.dao.support.client.DConnectionMaker;
import jun.spring.v1.dao.support.ConnectionMaker;
import jun.spring.v1.dao.support.CountingConnectionMakers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {

    @Bean
    public UserDaoV3_3RelationSeparation userDao() {
        return new UserDaoV3_3RelationSeparation(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new CountingConnectionMakers(realConnectionMaker());
    }

    @Bean
    public ConnectionMaker realConnectionMaker() {
        return new DConnectionMaker();
    }

}
