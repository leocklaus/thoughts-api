package io.github.leocklaus.thoughtsapi.api.exceptionhandler;

import io.github.leocklaus.thoughtsapi.domain.exceptions.NotAuthorizedException;
import io.github.leocklaus.thoughtsapi.domain.exceptions.ThoughtNotFoundException;
import io.github.leocklaus.thoughtsapi.domain.exceptions.UserNotFoundException;
import io.github.leocklaus.thoughtsapi.domain.exceptions.UserWrongPasswordException;
import org.aspectj.weaver.ast.Not;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    public static final String GENERIC_ERROR_MESSAGE
            = "Ocorreu um erro interno inesperado no sistema. Tente novamente e se "
            + "o problema persistir, entre em contato com o administrador do sistema.";

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException ex, WebRequest request){
        var status = HttpStatus.NOT_FOUND;
        var exceptionType = ExceptionType.USER_NOT_FOUND;
        String detail = ex.getMessage();
        ExceptionModel exceptionModel = exceptionBuilder(status, exceptionType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, exceptionModel, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(ThoughtNotFoundException.class)
    public ResponseEntity<?> handleThoughtNotFoundException(ThoughtNotFoundException ex, WebRequest request){
        var status = HttpStatus.NOT_FOUND;
        var exceptionType = ExceptionType.THOUGHT_NOT_FOUND;
        String detail = ex.getMessage();
        ExceptionModel exceptionModel = exceptionBuilder(status, exceptionType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, exceptionModel, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(NotAuthorizedException.class)
    public ResponseEntity<?> handleNotAuthorizedException(NotAuthorizedException ex, WebRequest request){
        var status = HttpStatus.UNAUTHORIZED;
        var exceptionType = ExceptionType.NOT_AUTHORIZED;
        String detail = ex.getMessage();
        ExceptionModel exceptionModel = exceptionBuilder(status, exceptionType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, exceptionModel, new HttpHeaders(), status, request);
    }

    @ExceptionHandler(UserWrongPasswordException.class)
    public ResponseEntity<?> handleWrongPasswordException(UserWrongPasswordException ex, WebRequest request){
        var status = HttpStatus.NOT_FOUND;
        var exceptionType = ExceptionType.WRONG_PASSWORD;
        String detail = ex.getMessage();
        ExceptionModel exceptionModel = exceptionBuilder(status, exceptionType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, exceptionModel, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        var bindingResult = ex.getBindingResult();
        String detail = "Um ou mais campos estão inválidos. Preencha o formulário novamente.";

        List<ExceptionModel.Object> errors = new ArrayList<>();

        errors = bindingResult.getFieldErrors().stream()
                .map(error -> {
                    String message = messageSource.getMessage(error, LocaleContextHolder.getLocale());
                    return ExceptionModel.Object.builder()
                            .name(error.getField())
                            .userMessage(message)
                            .build();
                }).toList();

        var exceptionType = ExceptionType.INVALID_DATA;

        ExceptionModel exceptionModel = exceptionBuilder((HttpStatus) status, exceptionType, detail)
                .userMessage(detail)
                .objects(errors)
                .build();

        return handleExceptionInternal(ex, exceptionModel, new HttpHeaders(), status, request);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {

        var detail = String.format("O recurso '%s' que você tentou acessar é inexistente", ex.getRequestURL());
        var exceptionType = ExceptionType.RESOURCE_NOT_FOUND;

        ExceptionModel exception = exceptionBuilder((HttpStatus) status, exceptionType, detail)
                .userMessage(detail)
                .build();

        return handleExceptionInternal(ex, exception, headers, status, request);

    }



    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        if(body == null){
            body = ExceptionModel.builder()
                    .status(statusCode.value())
                    .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                    .type(ex.getMessage())
                    .title(ex.getMessage())
                    .detail(ex.getMessage())
                    .build();
        }

        return super.handleExceptionInternal(ex, body, headers, statusCode, request);
    }

    private ExceptionModel.ExceptionModelBuilder exceptionBuilder(HttpStatus status, ExceptionType exceptionType, String detail){
        return ExceptionModel.builder()
                .status(status.value())
                .timestamp(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS))
                .type(exceptionType.getURI())
                .title(exceptionType.getTitle())
                .detail(detail);
    }
}
