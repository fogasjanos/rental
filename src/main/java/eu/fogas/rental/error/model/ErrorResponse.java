package eu.fogas.rental.error.model;

import lombok.Data;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Data
@SuperBuilder
public class ErrorResponse {
    protected final HttpStatus status;
    protected final String message;
}
