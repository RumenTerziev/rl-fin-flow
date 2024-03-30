package bg.lrsoft.rlfinflow.controller.handler;

import bg.lrsoft.rlfinflow.domain.dto.ErrorPayloadDto;
import bg.lrsoft.rlfinflow.service.exception.NoResponseFromExternalApiWasReceived;
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
}
