package bg.lrsoft.rlfinflow.domain.exception;

public class NoResponseFromExternalApiWasReceived extends RuntimeException {

    public NoResponseFromExternalApiWasReceived(String message) {
        super(message);
    }
}
