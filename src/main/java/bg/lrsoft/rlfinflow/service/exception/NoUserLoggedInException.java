package bg.lrsoft.rlfinflow.service.exception;

public class NoUserLoggedInException extends RuntimeException {

    public NoUserLoggedInException() {
        super("No user logged in!");
    }
}
