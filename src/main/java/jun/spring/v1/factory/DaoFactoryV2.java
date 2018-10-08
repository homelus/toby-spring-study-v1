package jun.spring.v1.factory;

import jun.spring.v1.dao.AccountDao;
import jun.spring.v1.dao.MessageDao;
import jun.spring.v1.dao.UserDaoV3_3RelationSeparation;
import jun.spring.v1.dao.support.client.DConnectionMaker;

/*
    다른 여러 Dao를 생성하는 기능이 추가되는 경우 구현 클래스를 선정하고 생성하는 기능이 중복
*/
public class DaoFactoryV2 {

    public UserDaoV3_3RelationSeparation userDao() {
        return new UserDaoV3_3RelationSeparation(new DConnectionMaker());
    }

    public AccountDao accountDao() {
        return new AccountDao(new DConnectionMaker());
    }

    public MessageDao messageDao() {
        return new MessageDao(new DConnectionMaker());
    }

}
