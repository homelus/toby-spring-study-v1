package jun.spring.ch1.factory;

import jun.spring.ch1.dao.UserDaoV3_3RelationSeparation;
import jun.spring.ch1.dao.support.client.DConnectionMaker;
import jun.spring.ch1.dao.support.ConnectionMaker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
    런타임 의존 관계를 설정해주는 의존관계 주입 작업을 주도하는 클래스 (다양한 Connection 연결 가능)
    IoC 방식으로 오브젝트 생성과 초기화, 제공 등의 작업을 수행하는 컨테이너(DI 컨테이너)

    단점.
    대부분 틀에 박힌 구조가 반복하고 DI 구성이 바뀔때마다 자바 코드를 수정하고 클래스를 컴파일 해야 한다.
 */
@Configuration // Spring 의 빈 팩토리를 위한 오브젝트 설정을 담당하는 클래스라는 인식표
public class SpringDaoFactory {

    @Bean // 오브젝트라는 인식표
    public UserDaoV3_3RelationSeparation userDao() {
        // 런타임 의존관계 설정(의존관계 주입)
        return new UserDaoV3_3RelationSeparation(connectionMaker());
    }

    @Bean
    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

}
