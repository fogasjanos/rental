package eu.fogas.rental.api.car.model.converter;

import eu.fogas.rental.api.car.model.Brand;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Arrays;

@Converter(autoApply = true)
public class BrandConverter implements AttributeConverter<Brand, String> {

    @Override
    public String convertToDatabaseColumn(Brand brand) {
        if (brand == null) {
            return null;
        }
        return brand.getLabel();
    }

    @Override
    public Brand convertToEntityAttribute(String label) {
        if (label == null) {
            return null;
        }
        return Arrays.stream(Brand.values())
                .filter(brand -> brand.getLabel().equals(label))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
