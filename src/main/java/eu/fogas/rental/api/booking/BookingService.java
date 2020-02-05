package eu.fogas.rental.api.booking;

import eu.fogas.rental.api.booking.model.BookingRequest;

public interface BookingService {
    void create(BookingRequest bookingRequest);
}
