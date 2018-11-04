package ch3;

import jun.spring.v3.dao.DeleteAllStatement;
import jun.spring.v3.dao.StatementStrategy;
import jun.spring.v3.dao.UserDao;
import org.junit.Test;

import java.sql.SQLException;

public class UserDaoTest {

    @Test
    public void deleteAll() throws SQLException {
        StatementStrategy strategy = new DeleteAllStatement();
        UserDao userDao = new UserDao();
        userDao.deleteAll(strategy);
    }

}
