package jun.spring.ch1;

import jun.spring.ch1.dao.UserDaoV3_3RelationSeparation;
import jun.spring.ch1.dao.support.CountingConnectionMakers;
import jun.spring.ch1.factory.CountingDaoFactory;
import jun.spring.ch1.user.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.sql.SQLException;

public class UserDaoConnectionCountingTest {

    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        ApplicationContext context = new AnnotationConfigApplicationContext(CountingDaoFactory.class);
        UserDaoV3_3RelationSeparation userDao = context.getBean("userDao", UserDaoV3_3RelationSeparation.class);

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

        CountingConnectionMakers ccm = context.getBean("connectionMaker", CountingConnectionMakers.class);
        System.out.println("Connection counter : " + ccm.getCount());

    }

}
