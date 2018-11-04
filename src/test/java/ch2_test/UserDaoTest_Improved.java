package ch2_test;

import jun.spring.v1.dao.UserDaoV4_DependencyLookup;
import jun.spring.v1.user.User;
import org.junit.Test;

import java.sql.SQLException;

public class UserDaoTest_Improved {

    @Test
    public void userDaoTest() throws SQLException, ClassNotFoundException {
        UserDaoV4_DependencyLookup userDao = new UserDaoV4_DependencyLookup();

        User user = new User();

        user.setId("jun");
        user.setName("윤현준");
        user.setPassword("hello");

        userDao.add(user);

        User user2 = userDao.get(user.getId());

        System.out.println(user2.getName());
        System.out.println(user2.getPassword());
        System.out.println(user2.getId() + "조회 성공");

        if (!user.getName().equals(user2.getName())) {
            System.out.println("테스트 실패 (name)");
        } else if (!user.getPassword().equals(user2.getPassword())) {
            System.out.println("테스트 실패 (password)");
        } else {
            System.out.println("조회 테스트 성공");
        }



    }

}
