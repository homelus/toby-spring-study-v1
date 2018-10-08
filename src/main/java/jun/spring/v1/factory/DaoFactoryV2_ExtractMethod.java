package jun.spring.v1.factory;

import jun.spring.v1.dao.AccountDao;
import jun.spring.v1.dao.MessageDao;
import jun.spring.v1.dao.UserDaoV3_3RelationSeparation;
import jun.spring.v1.dao.support.client.DConnectionMaker;
import jun.spring.v1.dao.support.ConnectionMaker;

public class DaoFactoryV2_ExtractMethod {

    public UserDaoV3_3RelationSeparation userDao() {
        return new UserDaoV3_3RelationSeparation(connectionMaker());
    }

    public AccountDao accountDao() {
        return new AccountDao(connectionMaker());
    }

    public MessageDao messageDao() {
        return new MessageDao(connectionMaker());
    }

    public ConnectionMaker connectionMaker() {
        return new DConnectionMaker();
    }

}
