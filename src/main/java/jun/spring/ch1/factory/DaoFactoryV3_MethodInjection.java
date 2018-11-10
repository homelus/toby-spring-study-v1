package jun.spring.ch1.factory;

import jun.spring.ch1.dao.UserDaoV5_MethodInjection;
import jun.spring.ch1.dao.support.client.DConnectionMaker;
import jun.spring.ch1.dao.support.ConnectionMaker;

public class DaoFactoryV3_MethodInjection {

    public UserDaoV5_MethodInjection userDao() {
        UserDaoV5_MethodInjection userDao = new UserDaoV5_MethodInjection();
        userDao.setConnectionMaker(connectionMaker());
        return userDao;
    }

    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

}
