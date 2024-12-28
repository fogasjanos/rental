package eu.fogas.rental.api.booking.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum Country {
    AUSTRIA("Austria"),
    HUNGARY("Hungary");

    private final String label;

    Country(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }

    @JsonCreator
    public Country parse(String label) {
        if (label == null) {
            return null;
        }
        return Arrays.stream(Country.values())
                .filter(country -> country.label.equals(label))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
