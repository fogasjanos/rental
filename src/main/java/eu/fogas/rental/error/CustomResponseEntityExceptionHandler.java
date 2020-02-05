package eu.fogas.rental.error;

import eu.fogas.rental.error.exception.CarNotAvailableException;
import eu.fogas.rental.error.exception.CarNotFoundException;
import eu.fogas.rental.error.model.DetailedErrorResponse;
import eu.fogas.rental.error.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Set;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse exceptionHandler(Exception e) {
        log.error("Internal server error: {}", e.getMessage(), e);
        return ErrorResponse.builder()
                .status(INTERNAL_SERVER_ERROR)
                .message("Internal server error.")
                .build();
    }


    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse carNotFoundExceptionHandler(CarNotFoundException e) {
        return ErrorResponse.builder()
                .status(NOT_FOUND)
                .message(e.getMessage())
                .build();
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    public ErrorResponse carNotAvailableExceptionHandler(CarNotAvailableException e) {
        return ErrorResponse.builder()
                .status(CONFLICT)
                .message(e.getMessage())
                .build();
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        DetailedErrorResponse errorResponse = DetailedErrorResponse.builder()
                .status(BAD_REQUEST)
                .message("Invalid request!")
                .build();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errorResponse.addFieldError(error.getField(), error.getDefaultMessage());
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errorResponse.addObjectError(error.getObjectName(), error.getDefaultMessage());
        }
        return handleExceptionInternal(ex, errorResponse, headers, errorResponse.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(BAD_REQUEST)
                .message(ex.getParameterName() + " parameter is missing.")
                .build();
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String message = String.format("No handler found for %s %s", ex.getHttpMethod(), ex.getRequestURL());
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(NOT_FOUND)
                .message(message)
                .build();
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder message = new StringBuilder()
                .append(ex.getMethod())
                .append(" method is not supported for this request.");
        Set<HttpMethod> supportedMethods = ex.getSupportedHttpMethods();
        if (!CollectionUtils.isEmpty(supportedMethods)) {
            message.append(" Supported methods are ");
            supportedMethods.forEach(t ->
                    message.append(t).append(" "));
        }
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(METHOD_NOT_ALLOWED)
                .message(message.toString())
                .build();
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder message = new StringBuilder()
                .append("Error code: '").append(ex.getErrorCode()).append("'.");
        if (ex.getPropertyName() != null) {
            message.append(" Property name: '").append(ex.getPropertyName()).append("'.");
        }
        if (ex.getRequiredType() != null) {
            message.append(" Required type: '").append(ex.getRequiredType()).append("'.");
        }
        if (ex.getValue() != null) {
            message.append(" Value: '").append(ex.getValue()).append("'.");
        }
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(BAD_REQUEST)
                .message(message.toString())
                .build();
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(BAD_REQUEST)
                .message("The http message is not readable. " + ex.getMostSpecificCause().getMessage().split("\\n")[0])
                .build();
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(UNSUPPORTED_MEDIA_TYPE)
                .message("Unsupported media type: " + ex.getContentType())
                .build();
        return new ResponseEntity<>(errorResponse, new HttpHeaders(), errorResponse.getStatus());
    }
}
