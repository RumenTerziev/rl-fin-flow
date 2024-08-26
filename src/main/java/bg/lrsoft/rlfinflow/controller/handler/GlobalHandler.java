package bg.lrsoft.rlfinflow.controller.handler;

import bg.lrsoft.rlfinflow.domain.dto.ErrorPayloadDto;
import bg.lrsoft.rlfinflow.service.exception.NoResponseFromExternalApiWasReceived;
import bg.lrsoft.rlfinflow.service.exception.NoUserLoggedInException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.result.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Slf4j
@RestControllerAdvice
public class GlobalHandler extends ResponseEntityExceptionHandler {

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

    @Override
    protected Mono<ResponseEntity<Object>> handleExceptionInternal(Exception ex, @Nullable Object body, @Nullable HttpHeaders headers, HttpStatusCode status, ServerWebExchange exchange) {
        log.error("--------------------ERROR ADDRESS----------------------");
        log.error(exchange.getRequest().getURI().toString());
        return super.handleExceptionInternal(ex, body, headers, status, exchange);
    }
}
