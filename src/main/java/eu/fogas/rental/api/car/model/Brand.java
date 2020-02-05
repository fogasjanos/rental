package eu.fogas.rental.api.car.model;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Brand {
    AUDI("Audi"),
    TOYOTA("Toyota"),
    TESLA("Tesla"),
    SKODA("Skoda"),
    OPEL("Opel"),
    MAZDA("Mazda"),
    VOLVO("Volvo");

    private final String label;

    Brand(String label) {
        this.label = label;
    }

    @JsonValue
    public String getLabel() {
        return label;
    }
}
