package jun.spring.v4.exception;

public class RetryFailedException extends RuntimeException {
    public RetryFailedException() {
    }

    public RetryFailedException(Throwable cause) {
        super(cause);
    }
}
