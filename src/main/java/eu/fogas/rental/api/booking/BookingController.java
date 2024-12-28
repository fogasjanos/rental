package eu.fogas.rental.api.booking;

import eu.fogas.rental.api.booking.model.BookingRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBooking(@Validated @RequestBody BookingRequest bookingRequest) {
        bookingService.create(bookingRequest);
    }
}
