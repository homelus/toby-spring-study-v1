package jun.spring.ch4.exception;

import org.springframework.dao.DataAccessException;

public class DuplicateUserIdException extends DataAccessException {
    public DuplicateUserIdException(String msg) {
        super(msg);
    }
}
