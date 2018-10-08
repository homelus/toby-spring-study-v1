package jun.spring.v1;

import jun.spring.v1.dao.UserDaoV2_1Separation;
import jun.spring.v1.user.User;

import java.sql.SQLException;

public class UserTestV12 {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
//        UserDaoV1 dao = new UserDaoV1();
        UserDaoV2_1Separation dao = new UserDaoV2_1Separation(); // 검증
        User user = new User();

        user.setId("jun");
        user.setName("윤현준");
        user.setPassword("hello");

        dao.add(user);

        System.out.println(user.getId() + " 등록 성공");

        User user2 = dao.get(user.getId());
        System.out.println(user2.getName());
        System.out.println(user2.getPassword());


        System.out.println(user2.getId() + " 조회 성공");
    }

}
