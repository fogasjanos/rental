package eu.fogas.rental.api.booking.model.converter;

import eu.fogas.rental.api.booking.model.Country;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

@Converter(autoApply = true)
public class CountryConverter implements AttributeConverter<Country, String> {

    @Override
    public String convertToDatabaseColumn(Country usage) {
        if (usage == null) {
            return null;
        }
        return usage.getLabel();
    }

    @Override
    public Country convertToEntityAttribute(String countryLabel) {
        if (countryLabel == null) {
            return null;
        }
        return Arrays.stream(Country.values())
                .filter(country -> country.getLabel().equals(countryLabel))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
