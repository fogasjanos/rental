package eu.fogas.rental.api.booking.model;

import eu.fogas.rental.api.booking.model.validator.CheckBookingCountry;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.Set;

@CheckBookingCountry
public record BookingRequest(
        @NotNull(message = "Property carId cannot be null")
        long carId,

        @NotNull(message = "Property usage cannot be null.")
        Usage usage,

        Set<Country> targetCountries,

        @NotNull(message = "Property rangeFrom cannot be null.")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        Date rangeFrom,

        @NotNull(message = "Property rangeTo cannot be null.")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        Date rangeTo
) {
}
