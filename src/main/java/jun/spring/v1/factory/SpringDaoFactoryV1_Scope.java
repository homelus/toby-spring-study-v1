package jun.spring.v1.factory;

import jun.spring.v1.user.User;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class SpringDaoFactoryV1_Scope {

    /*
        빈 스코프 종류
        singleton : 기본 싱글톤
        prototype : 애플리케이션 요청 시 새 인스턴스 생성
        request : HTTP 요청 별로 인스턴스화 되고 요청이 끝나면 소멸
        session : HTTP Session 별로 인스턴스화 되고 세션이 끝나면 소멸
        등등..
     */

    @Bean
    @Scope("singleton")
    public User singletonUser() {
        return new User();
    }

    @Bean
    @Scope("prototype")
    public User prototypeUser() {
        return new User();
    }

}
