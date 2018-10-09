package jun.spring.v1;

import jun.spring.v1.dao.UserDaoV3_3RelationSeparation;
import jun.spring.v1.dao.support.ConnectionMaker;
import jun.spring.v1.dao.support.client.DConnectionMaker;
import jun.spring.v1.factory.DaoFactoryV1;
import jun.spring.v1.user.User;

import java.sql.SQLException;

public class UserDaoTestV3_RelationSeparation {

    /*
        이 클래스의 순수한 의도는 UserDao의 기능을 테스트하는 것이 목적
        클라이언트에서 구현 클래스 사용을 결정하는 기능을 가지는 것이 타당한가?
    */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        // 오브젝트 생성 역할이 클라이언트 코드 내 존재
        ConnectionMaker connectionMaker = new DConnectionMaker();
        UserDaoV3_3RelationSeparation userDao = new UserDaoV3_3RelationSeparation(connectionMaker);

        // 오브젝트 생성을 팩토리에 요청
//        UserDaoV3_3RelationSeparation userDao = new DaoFactoryV1().userDao();

        // 오브젝트 사용
        User user = new User();

        user.setId("jun");
        user.setName("윤현준");
        user.setPassword("hello");

        userDao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = userDao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());


        System.out.println(user2.getId() + " 조회 성공");

    }

}
