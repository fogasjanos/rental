package eu.fogas.rental.api.booking.model.converter;

import eu.fogas.rental.api.booking.model.Usage;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
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
    public Usage convertToEntityAttribute(String usage) {
        if (usage == null) {
            return null;
        }
        return Arrays.stream(Usage.values())
                .filter(brand -> brand.getLabel().equals(usage))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
