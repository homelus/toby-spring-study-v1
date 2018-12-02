package jun.spring.ch7.user.exception;

public class RetryFailedException extends RuntimeException {
    public RetryFailedException() {
    }

    public RetryFailedException(Throwable cause) {
        super(cause);
    }
}
