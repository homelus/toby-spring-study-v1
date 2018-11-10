package jun.spring.ch4.exception;

public class RetryFailedException extends RuntimeException {
    public RetryFailedException() {
    }

    public RetryFailedException(Throwable cause) {
        super(cause);
    }
}
