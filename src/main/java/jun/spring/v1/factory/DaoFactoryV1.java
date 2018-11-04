package jun.spring.v1.factory;

import jun.spring.v1.dao.UserDaoV3_3RelationSeparation;
import jun.spring.v1.dao.support.client.DConnectionMaker;
import jun.spring.v1.dao.support.ConnectionMaker;

/*
    오브젝트를 생성하는 클래스
    이를 통해 오브젝트를 생성하는 역할과 사용하는 역할을 분리
    팩토리 - 객체의 생성방법을 결정하고 만들어진 오브젝트를 반환

    책임의 분리
    UserDaoJdbc 와 ConnectionMaker 는 각각 애플리케이션의 핵심적인 데이터 로직과 기술 로직을 담당
    DaoFactory 는 위의 애플리케이션 오브젝트를 구성하고 관계를 정의(컴포넌트 구조를 정의한 설계도)
 */
public class DaoFactoryV1 {

    /*
        런타임 시에 오브젝트 사이에 만들어지는 의존관계
     */
    public UserDaoV3_3RelationSeparation userDao() {
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDaoV3_3RelationSeparation userDao = new UserDaoV3_3RelationSeparation(connectionMaker);
        return userDao;
    }

}
