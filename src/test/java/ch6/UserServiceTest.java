package ch6;

import ch5.TestUserServiceException;
import jun.spring.ch6.dao.UserDao;
import jun.spring.ch6.model.Level;
import jun.spring.ch6.model.User;
import jun.spring.ch6.service.UserService;
import jun.spring.ch6.service.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static jun.spring.ch6.service.UserLevelUpgradePolicyImpl.MIN_LOGCOUNT_FOR_SILVER;
import static jun.spring.ch6.service.UserLevelUpgradePolicyImpl.MIN_RECOMMEND_FOR_GOLD;
import static junit.framework.TestCase.fail;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

@ContextConfiguration("/test-applicationContext-v6.xml")
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
//@TransactionConfiguration(defaultRollback = false)
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserService testUserService;

    @Autowired
    UserDao userDao;

    @Autowired
    PlatformTransactionManager transactionManager;

    @Autowired
    ApplicationContext context;

    @Autowired
    MailSender mailSender;

    List<User> users;

    @Before
    public void setUp() {
        users = Arrays.asList(
                new User("hyosub", "곽효섭", "h1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER - 1, 0, "ghs@email.com"),
                new User("sangmin", "이상민", "l1234", Level.BASIC, MIN_LOGCOUNT_FOR_SILVER, 0, "lsm@email.com"),
                new User("hyungsuk", "김형석", "k1234", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD - 1, "khs@email.com"),
                new User("yudongyup", "여동엽", "y1234", Level.SILVER, 60, MIN_RECOMMEND_FOR_GOLD, "ydy@email.com"),
                new User("jusunghwan", "주성환", "j1234", Level.GOLD, 100, Integer.MAX_VALUE, "jsh@email.com")
        );
    }

    @Test
    public void bean() {
        assertThat(this.userService, is(notNullValue()));
    }

    @Test
    public void add() {
        userDao.deleteAll();

        User userWithLevel = users.get(4);
        User userWithoutLevel = users.get(0);
        userWithoutLevel.setLevel(null);

        userService.add(userWithLevel);
        userService.add(userWithoutLevel);

        User userWithLevelRead = userDao.get(userWithLevel.getId());
        User userWithoutLevelRead = userDao.get(userWithoutLevel.getId());

        assertThat(userWithLevel.getLevel(), is(userWithLevelRead.getLevel()));
        assertThat(userWithoutLevelRead.getLevel(), is(Level.BASIC));
    }

    @Test
    public void mockUpgradeLevels() {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        UserDao mockUserDao = mock(UserDao.class);
        when(mockUserDao.getAll()).thenReturn(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MailSender mockMailSender = mock(MailSender.class);
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        verify(mockUserDao, times(2)).update(any(User.class));
        verify(mockUserDao).update(users.get(1));
        assertThat(users.get(1).getLevel(), is(Level.SILVER));
        verify(mockUserDao).update(users.get(3));
        assertThat(users.get(3).getLevel(), is(Level.GOLD));

        ArgumentCaptor<SimpleMailMessage> mailMessageArg = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mockMailSender, times(2)).send(mailMessageArg.capture());
        List<SimpleMailMessage> mailMessages = mailMessageArg.getAllValues();
        assertThat(mailMessages.get(0).getTo()[0], is(users.get(1).getEmail()));
        assertThat(mailMessages.get(1).getTo()[0], is(users.get(3).getEmail()));

    }

    @Test
    @DirtiesContext
    public void upgradeLevels() throws Exception {
        UserServiceImpl userServiceImpl = new UserServiceImpl();

        MockUserDao mockUserDao = new MockUserDao(this.users);
        userServiceImpl.setUserDao(mockUserDao);

        MockMailSender mockMailSender = new MockMailSender();
        userServiceImpl.setMailSender(mockMailSender);

        userServiceImpl.upgradeLevels();

        List<User> updated = mockUserDao.getUpdated();
        assertThat(updated.size(), is(2));

        checkUserAndLevel(updated.get(0), "sangmin", Level.SILVER);
        checkUserAndLevel(updated.get(1), "yudongyup", Level.GOLD);

        List<String> requests = mockMailSender.getRequests();
        assertThat(requests.size(), is(2));
        assertThat(requests.get(0), is(users.get(1).getEmail()));
        assertThat(requests.get(1), is(users.get(3).getEmail()));
    }

    @Test
    public void upgradeAllOrNothing() throws Exception {
        userDao.deleteAll();
        for (User user : users) {
            userDao.add(user);
        }

        try {
            this.testUserService.upgradeLevels();
            fail("TestUserServiceException expected");
        } catch (TestUserServiceException e) {
        }

        checkLevelUpgraded(users.get(1), false);
    }

    @Test
    public void advisorAutoProxyCreator() {
        assertThat(testUserService, instanceOf(java.lang.reflect.Proxy.class));
    }

    @Test
    public void readOnlyTransactionAttribute() {
        testUserService.getAll();
    }

    @Test
    public void transactionSync() {
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        txDefinition.setReadOnly(true);
        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

        userService.deleteAll();

        userService.add(users.get(0));
        userService.add(users.get(1));

        transactionManager.commit(txStatus);
    }

    @Test
    public void tranactionSyncRollback() {
        userDao.deleteAll();
        assertThat(userDao.getCount(), is(0));

        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

        userService.add(users.get(0));
        userService.add(users.get(1));
        assertThat(userDao.getCount(), is(2));

        transactionManager.rollback(txStatus);
        assertThat(userDao.getCount(), is(0));
    }

    @Test
    public void transactionRollback() {
        DefaultTransactionDefinition txDefinition = new DefaultTransactionDefinition();
        TransactionStatus txStatus = transactionManager.getTransaction(txDefinition);

        try {
            userService.deleteAll();
            userService.add(users.get(0));
            userService.add(users.get(1));
        }
        finally {
            transactionManager.rollback(txStatus);
        }
    }

    @Test
    @Transactional(readOnly = true)
    public void transactionAnnotationSyncTest() {
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }

    @Test
    @Transactional
    @Rollback(false)
    public void transactionAnnotationNotRollbackTest() {
        userService.deleteAll();
        userService.add(users.get(0));
        userService.add(users.get(1));
    }

    private void checkUserAndLevel(User updated, String expectedId, Level expectedLevel) {
        assertThat(updated.getId(), is(expectedId));
        assertThat(updated.getLevel(), is(expectedLevel));
    }

    private void checkLevelUpgraded(User user, boolean upgraded) {
        User userUpdate = userDao.get(user.getId());
        if (upgraded) {
            assertThat(userUpdate.getLevel(), is(user.getLevel().nextLevel()));
        } else {
            assertThat(userUpdate.getLevel(), is(user.getLevel()));
        }
    }

    static class TestUserService extends UserServiceImpl {
        private String id = "yudongyup";

        @Override
        protected void upgradeLevel(User user) {
            if (user.getId().equals(this.id))  {
                throw new TestUserServiceException();
            }
            super.upgradeLevel(user);
        }

        public List<User> getAll() {
            for (User user : super.getAll()) {
                super.update(user);
            }
            return null;
        }
    }

    static class MockUserDao implements UserDao {

        private List<User> users;
        private List<User> updated = new ArrayList<>();

        private MockUserDao(List<User> users) {
            this.users = users;
        }

        public List<User> getUpdated() {
            return this.updated;
        }

        @Override
        public List<User> getAll() {
            return this.users;
        }

        @Override
        public void update(User user) {
            updated.add(user);
        }

        @Override
        public void add(User user) {
            throw new UnsupportedOperationException();
        }

        @Override
        public User get(String id) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void deleteAll() {
            throw new UnsupportedOperationException();
        }

        @Override
        public int getCount() {
            throw new UnsupportedOperationException();
        }

    }

    static class MockMailSender implements MailSender {

        private List<String> requests = new ArrayList<>();

        public List<String> getRequests() {
            return requests;
        }

        @Override
        public void send(SimpleMailMessage simpleMailMessage) throws MailException {
            requests.add(simpleMailMessage.getTo()[0]);
        }

        @Override
        public void send(SimpleMailMessage... simpleMailMessages) throws MailException {

        }
    }

}
