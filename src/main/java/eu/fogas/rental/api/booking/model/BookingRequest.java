package eu.fogas.rental.api.booking.model;

import eu.fogas.rental.api.booking.model.validator.CheckBookingCountry;
import lombok.Builder;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Data
@Builder
@CheckBookingCountry
public class BookingRequest {

    @NotNull(message = "Property carId cannot be null")
    private final long carId;

    @NotNull(message = "Property rangeFrom cannot be null.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final Date rangeFrom;

    @NotNull(message = "Property rangeTo cannot be null.")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private final Date rangeTo;

    @NotNull(message = "Property usage cannot be null.")
    private final Usage usage;

    private final List<String> targetCountries;
}
