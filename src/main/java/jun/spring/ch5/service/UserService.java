package jun.spring.ch5.service;

import jun.spring.ch5.dao.UserDao;

public class UserService {

    UserDao userDao;

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

}
