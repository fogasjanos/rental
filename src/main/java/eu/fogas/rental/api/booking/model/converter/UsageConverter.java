package eu.fogas.rental.api.booking.model.converter;

import eu.fogas.rental.api.booking.model.Usage;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

@Converter(autoApply = true)
public class UsageConverter implements AttributeConverter<Usage, String> {

    @Override
    public String convertToDatabaseColumn(Usage usage) {
        if (usage == null) {
            return null;
        }
        return usage.getLabel();
    }

    @Override
    public Usage convertToEntityAttribute(String usageLabel) {
        if (usageLabel == null) {
            return null;
        }
        return Arrays.stream(Usage.values())
                .filter(usage -> usage.getLabel().equals(usageLabel))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
