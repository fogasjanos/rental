package eu.fogas.rental.api.booking;

import eu.fogas.rental.api.booking.model.Booking;
import eu.fogas.rental.api.booking.model.BookingRequest;
import eu.fogas.rental.api.car.CarRepository;
import eu.fogas.rental.api.car.model.Car;
import eu.fogas.rental.error.exception.CarNotAvailableException;
import eu.fogas.rental.error.exception.CarNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

@Service
@Slf4j
public class DefaultBookingService implements BookingService {
    private final BookingRepository bookingRepository;
    private final CarRepository carRepository;

    @Autowired
    public DefaultBookingService(BookingRepository bookingRepository, CarRepository carRepository) {
        this.bookingRepository = bookingRepository;
        this.carRepository = carRepository;
    }

    @Override
    public void create(BookingRequest bookingRequest) {
        long carId = bookingRequest.getCarId();
        Optional<Car> car = carRepository.findById(carId);
        car.orElseThrow(() ->
                new CarNotFoundException(carId));

        LocalDate from = convertToLocalDate(bookingRequest.getRangeFrom());
        LocalDate to = convertToLocalDate(bookingRequest.getRangeTo());
        boolean isCarAvailable = bookingRepository.isCarAvailable(carId, from, to);
        if (!isCarAvailable) {
            log.debug("Car with {} carId is not available.", carId);
            throw new CarNotAvailableException(carId);
        }

        Booking booking = new Booking(car.get(), from, to, bookingRequest.getUsage(), bookingRequest.getTargetCountries());
        log.debug("Saving booking: {}.", booking);
        bookingRepository.save(booking);
    }

    private LocalDate convertToLocalDate(Date date) {
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }
}
