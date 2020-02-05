package eu.fogas.rental.api.booking.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Usage {
    DOMESTIC("domestic"),
    FOREIGN("foreign");

    private final String label;

    Usage(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public Usage parse(String label) {
        if (label == null) {
            return null;
        }
        return Arrays.stream(Usage.values())
                .filter(usage -> usage.label.equals(label))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
