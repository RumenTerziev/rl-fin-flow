package bg.lrsoft.rlfinflow.service.exception;

public class NoResponseFromExternalApiWasReceived extends RuntimeException {

    public NoResponseFromExternalApiWasReceived(String message) {
        super(message);
    }
}
