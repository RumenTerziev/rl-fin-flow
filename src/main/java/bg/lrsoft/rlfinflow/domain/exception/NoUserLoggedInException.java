package bg.lrsoft.rlfinflow.domain.exception;

public class NoUserLoggedInException extends RuntimeException {

    public NoUserLoggedInException() {
        super("No user logged in!");
    }
}
