package eu.fogas.rental.api.booking;

import eu.fogas.rental.api.booking.model.Booking;
import eu.fogas.rental.api.booking.model.BookingRequest;
import eu.fogas.rental.api.car.CarRepository;
import eu.fogas.rental.api.car.model.Car;
import eu.fogas.rental.error.exception.CarNotAvailableException;
import eu.fogas.rental.error.exception.CarNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DefaultBookingService implements BookingService {
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;

    @Override
    public void create(BookingRequest bookingRequest) {
        long carId = bookingRequest.carId();
        Optional<Car> car = carRepository.findById(carId);
        car.orElseThrow(() ->
                new CarNotFoundException(carId));

        LocalDate from = convertToLocalDate(bookingRequest.rangeFrom());
        LocalDate to = convertToLocalDate(bookingRequest.rangeTo());
        boolean isCarAvailable = bookingRepository.isCarAvailable(carId, from, to);
        if (!isCarAvailable) {
            log.debug("Car with {} carId is not available.", carId);
            throw new CarNotAvailableException(carId);
        }

        Booking booking = new Booking(car.get(), from, to, bookingRequest.usage(), bookingRequest.targetCountries());
        log.debug("Saving booking: {}.", booking);
        bookingRepository.save(booking);
    }

    private LocalDate convertToLocalDate(Date date) {
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
