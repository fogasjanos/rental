package eu.fogas.rental.api.booking;

import eu.fogas.rental.api.booking.model.BookingRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping(value = "/bookings")
public class BookingController {
    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PutMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createBooking(@Validated @RequestBody BookingRequest bookingRequest) {
        bookingService.create(bookingRequest);
    }
}
