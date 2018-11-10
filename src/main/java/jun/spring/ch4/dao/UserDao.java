package jun.spring.ch4.dao;

import jun.spring.ch4.model.User;

import java.util.List;

public interface UserDao {

    void add(User user);

    User get(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();

}
