package jun.spring.ch1.factory;

import jun.spring.ch1.dao.UserDaoV3_3RelationSeparation;
import jun.spring.ch1.dao.support.client.DConnectionMaker;
import jun.spring.ch1.dao.support.ConnectionMaker;
import jun.spring.ch1.dao.support.CountingConnectionMakers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountingDaoFactory {

    // 모든 DAO 는 여전히 connectionMaker 에서 만들어지는 오브젝트를 DI 받는다.
    // 분석작업이 끝나면 주입하는 부분만 바꾸면 원상복구 가능.
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
