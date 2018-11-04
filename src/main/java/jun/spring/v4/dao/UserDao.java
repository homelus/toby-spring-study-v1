package jun.spring.v4.dao;

import jun.spring.v4.model.User;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

public interface UserDao {

    void add(User user);

    User get(String id);

    List<User> getAll();

    void deleteAll();

    int getCount();

}
