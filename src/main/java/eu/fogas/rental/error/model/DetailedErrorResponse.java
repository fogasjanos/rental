package eu.fogas.rental.error.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ToString
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class DetailedErrorResponse extends ErrorResponse {
    private final List<Map<String, String>> errors = new ArrayList<>();

    public void addFieldError(String field, String message) {
        addError("field", field, message);
    }

    public void addObjectError(String field, String message) {
        addError("object", field, message);
    }

    private void addError(String propertyName, String property, String message) {
        Map<String, String> details = new HashMap<>();
        details.put(propertyName, property);
        details.put("message", message);
        errors.add(details);
    }
}
