package ch1;

import jun.spring.ch1.dao.UserDaoV4_DependencyLookup;
import jun.spring.ch1.user.User;
import org.junit.Test;

import java.sql.SQLException;

public class UserDaoTest {

    @Test
    public void userDaoTest() throws SQLException, ClassNotFoundException {

        // 오브젝트 자신이 빈일 필요가 없다.
        UserDaoV4_DependencyLookup userDao = new UserDaoV4_DependencyLookup();

//        UserDaoV5_MethodInjection userDao = new DaoFactoryV3_MethodInjection().userDao();

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
