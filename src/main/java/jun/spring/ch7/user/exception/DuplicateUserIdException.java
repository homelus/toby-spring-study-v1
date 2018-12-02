package jun.spring.ch7.user.exception;

import org.springframework.dao.DataAccessException;

public class DuplicateUserIdException extends DataAccessException {
    public DuplicateUserIdException(String msg) {
        super(msg);
    }
}
