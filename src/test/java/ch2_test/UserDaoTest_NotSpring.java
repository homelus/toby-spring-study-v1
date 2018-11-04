package ch2_test;

import jun.spring.v1.dao.UserDao;
import jun.spring.v1.user.User;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class UserDaoTest_NotSpring {

    UserDao userDao;

    private User user1;
    private User user2;
    private User user3;

    @Before
    public void setUp() {
        userDao = new UserDao();

        DataSource dataSource = new SingleConnectionDataSource("jdbc:mariadb://localhost/spring_test_dev", "root", "1234", true);
        userDao.setDataSource(dataSource);

        user1 = new User("hyosub", "곽효섭", "h1234");
        user2 = new User("sangmin", "이상민", "l1234");
        user3 = new User("hyungsuk", "김형석", "k1234");
    }

    @Test
    public void addAndGet() throws SQLException {

        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        userDao.add(user1);
        userDao.add(user2);
        assertThat(userDao.getCount(), is(2));

        User userget1= userDao.get(user1.getId());

        assertThat(userget1.getName(), is(user1.getName()));
        assertThat(userget1.getPassword(), is(user1.getPassword()));

        User userget2 = userDao.get(user2.getId());
        assertThat(userget2.getName(), is(user2.getName()));
        assertThat(userget2.getPassword(), is(user2.getPassword()));

    }



}
