package bg.lrsoft.rlfinflow.controller.handler;

import bg.lrsoft.rlfinflow.domain.dto.ErrorPayloadDto;
import bg.lrsoft.rlfinflow.domain.exception.NoResponseFromExternalApiWasReceived;
import bg.lrsoft.rlfinflow.domain.exception.NoUserLoggedInException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalHandler {

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(NoResponseFromExternalApiWasReceived.class)
    public ErrorPayloadDto handleNoResponseFromExternalApiWasReceived(NoResponseFromExternalApiWasReceived exception) {
        return new ErrorPayloadDto(
                exception.getMessage(),
                LocalDateTime.now()
        );
    }

    @ResponseStatus(BAD_REQUEST)
    @ExceptionHandler(NoUserLoggedInException.class)
    public ErrorPayloadDto handleNoUserLoggedInException(NoUserLoggedInException exception) {
        return new ErrorPayloadDto(
                exception.getMessage(),
                LocalDateTime.now()
        );
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NoResourceFoundException.class)
    public ErrorPayloadDto handleNoResourceFoundException(NoResourceFoundException exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorPayloadDto("No resource found", LocalDateTime.now());
    }

    @ResponseStatus(INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public ErrorPayloadDto handleException(Exception exception) {
        log.error(exception.getMessage(), exception);
        return new ErrorPayloadDto("Unexpected error", LocalDateTime.now());
    }
}
