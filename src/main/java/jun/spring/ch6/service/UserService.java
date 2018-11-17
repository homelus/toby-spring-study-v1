package jun.spring.ch6.service;

import jun.spring.ch6.model.User;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface UserService {

    void add(User user);

    void deleteAll();

    void update(User user);

    void upgradeLevels();

    @Transactional(readOnly = true, propagation = Propagation.REQUIRED)
    User get(String id);

    @Transactional(readOnly = true)
    List<User> getAll();

}
