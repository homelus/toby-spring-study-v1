package jun.spring.ch1.dao;

import jun.spring.ch1.dao.support.ConnectionMaker;

/*
    싱글톤 패턴 구현 방식의 단점
    1. private 생성자를 갖기 때문에 상속할수 없다.
     - 객체지향의 장점인 상속과 다형성 적용 불가
    2. 테스트하기가 어렵다.
     - 초기화 과정에서 생성자 등을 통해 오브젝트를 주입하기 힘들기 때문에 테스트용 오브젝트로 대체하기 어렵다.
    3. 서버환경에서 싱글톤이 하나만 만들어지는 것을 보장하지 못한다.
     - 클래스 로더의 구성방식에 따라 하나 이상 만들어 질 수 있다.
    4. 싱글톤 사용은 전역 상태를 만들 수 있기 때문에 바람직하지 못함.
     - 누구나 쉽고 자유롭게 접근이 가능하므로 전역 상태로 사용되기 쉬움.

     스프링은 싱글톤 패턴을 사용하지 않고 직접 싱글톤 형태의 오브젝트를 만들고 관리하는 기능을 제공한다. (싱글톤 레지스트리)
 */
public class UserDaoSingletonAntiExample {

    // 싱글톤 오브젝트를 저장할 수 있는 자신과 같은 타입의 스태틱 필드
    private static UserDaoSingletonAntiExample INSTANCE;

    private ConnectionMaker connectionMaker;

    // 클래스 밖에서 생성을 제한하기 위해 private 타입의 생성자
    // 외부에서 호출할 수 없기 때문에 connectionMaker 적용 불가
    private UserDaoSingletonAntiExample(ConnectionMaker connectionMaker) {
        this.connectionMaker = connectionMaker;
    }

    // 메소드가 최초로 호출되는시점에 한번만 오브젝트를 생성, 그 이후에 만들어진 오브젝트를 반환
    public static synchronized UserDaoSingletonAntiExample getInstance() {
        if (INSTANCE == null) {
//            INSTANCE = new UserDaoSingletonExample(???);
        }

        return INSTANCE;
    }

}
