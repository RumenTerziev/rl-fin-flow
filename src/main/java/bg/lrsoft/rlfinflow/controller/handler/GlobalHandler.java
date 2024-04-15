package bg.lrsoft.rlfinflow.controller.handler;

import bg.lrsoft.rlfinflow.domain.dto.ErrorPayloadDto;
import bg.lrsoft.rlfinflow.service.exception.NoResponseFromExternalApiWasReceived;
import bg.lrsoft.rlfinflow.service.exception.NoUserLoggedInException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
}
