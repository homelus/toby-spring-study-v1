package ch3;

import jun.spring.v1.user.User;
import jun.spring.v3.dao.UserDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.SQLException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext-v3.xml")
public class UserDaoTest {

    @Autowired
    private UserDao userDao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        user1 = new User("hyosub", "곽효섭", "h1234");
        user2 = new User("sangmin", "이상민", "l1234");
        user3 = new User("hyungsuk", "김형석", "k1234");
    }

    @Test
    public void deleteAll() throws SQLException {
        userDao.deleteAll();
    }

    @Test
    public void add() throws SQLException {
        userDao.add(user1);
    }



}
