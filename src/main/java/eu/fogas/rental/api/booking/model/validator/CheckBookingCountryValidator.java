package eu.fogas.rental.api.booking.model.validator;

import eu.fogas.rental.api.booking.model.BookingRequest;
import eu.fogas.rental.api.booking.model.Usage;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import static eu.fogas.rental.api.booking.model.Usage.DOMESTIC;
import static eu.fogas.rental.api.booking.model.Usage.FOREIGN;

@Slf4j
public class CheckBookingCountryValidator implements ConstraintValidator<CheckBookingCountry, BookingRequest> {
    private static final String COUNTRIES_SHOULD_BE_EMPTY = "The field targetCountries should be empty or null when usage is domestic!";
    private static final String COUNTRIES_IS_REQUIRED = "The field targetCountries is required when usage is foreign!";
    private static final String TARGET_COUNTRIES = "targetCountries";

    @Override
    public boolean isValid(BookingRequest bookingRequest, ConstraintValidatorContext context) {
        Usage usage = bookingRequest.usage();
        boolean isCountriesEmpty = CollectionUtils.isEmpty(bookingRequest.targetCountries());
        if (usage == DOMESTIC && !isCountriesEmpty) {
            markAsInvalid(context, COUNTRIES_SHOULD_BE_EMPTY);
            return false;
        }
        if (usage == FOREIGN && isCountriesEmpty) {
            markAsInvalid(context, COUNTRIES_IS_REQUIRED);
            return false;
        }
        return true;
    }

    private void markAsInvalid(ConstraintValidatorContext context, String message) {
        log.debug("Invalid RentRequest! Message: {}", message);
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addPropertyNode(TARGET_COUNTRIES)
                .addConstraintViolation();
    }
}
