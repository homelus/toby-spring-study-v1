package ch4;

import jun.spring.ch4.dao.UserDao;
import jun.spring.ch4.exception.DuplicateUserIdException;
import jun.spring.ch4.exception.RetryFailedException;
import jun.spring.ch4.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;
import org.springframework.jdbc.support.SQLExceptionTranslator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test-applicationContext-v4.xml")
public class ExceptionTest {

    private User user1;

    @Autowired
    private UserDao userDao;

    @Autowired
    DataSource dataSource;

    private final static int MAX_RETRY = 5;

    @Before
    public void setUp() {
        user1 = new User("hyosub", "곽효섭", "h1234");
    }

    @Test
    public void retryTest() throws SQLException {

        int maxretry = MAX_RETRY;

        while (maxretry-- > 0) {
            try {
                userDao.deleteAll();
                return;
            } catch (Exception e) {
                System.out.println("실패 & 재시도: " + maxretry + ", Exception: " + e.getMessage());
                try {
                    TimeUnit.SECONDS.sleep(4);
                } catch (InterruptedException ignore) {}
            } finally {
                System.out.println("자원 반납");
            }
        }

        throw new RetryFailedException();
    }

    @Test
    public void duplicatedTest() throws SQLException {
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        try {
            userDao.add(user1);
            userDao.add(user1);
        } catch (DataAccessException e) {
            if (e instanceof DuplicateKeyException)
                throw new DuplicateUserIdException(e.getMessage());
        }
    }

    @Test(expected = DuplicateUserIdException.class)
    public void duplicateKey() {
        userDao.deleteAll();

        userDao.add(user1);
        userDao.add(user1);
    }

    @Test
    public void sqlExceptonTranslate() {
        userDao.deleteAll();
        try {
            userDao.add(user1);
            userDao.add(user1);
        } catch (DuplicateKeyException ex) {
            SQLException sqlEx = (SQLException) ex.getRootCause();
            SQLExceptionTranslator set = new SQLErrorCodeSQLExceptionTranslator(this.dataSource);

//            assertThat(set.translate(null, null, sqlEx), is(DuplicateKeyException.class));
        }
    }

}

