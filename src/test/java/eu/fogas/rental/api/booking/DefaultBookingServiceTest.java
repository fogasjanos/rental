package eu.fogas.rental.api.booking;

import eu.fogas.rental.api.booking.model.Booking;
import eu.fogas.rental.api.booking.model.BookingRequest;
import eu.fogas.rental.api.booking.model.Usage;
import eu.fogas.rental.api.car.CarRepository;
import eu.fogas.rental.api.car.model.Brand;
import eu.fogas.rental.api.car.model.Car;
import eu.fogas.rental.error.exception.CarNotAvailableException;
import eu.fogas.rental.error.exception.CarNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DefaultBookingServiceTest {
    private static final Date TODAY = new Date();
    private static final Car CAR = Car.builder()
            .carId(16)
            .brand(Brand.OPEL)
            .plate("BND-007")
            .build();
    private static final BookingRequest BOOKING_RQ = BookingRequest.builder()
            .carId(CAR.getCarId())
            .rangeFrom(TODAY)
            .rangeTo(TODAY)
            .usage(Usage.DOMESTIC)
            .targetCountries(null)
            .build();

    @InjectMocks
    private DefaultBookingService bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CarRepository carRepository;
    @Captor
    private ArgumentCaptor<Booking> bookingCaptor;

    @Test(expected = CarNotFoundException.class)
    public void save_shouldThrowCarNotFoundException_whenCarNotExists() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.empty());

        bookingService.create(BOOKING_RQ);
    }

    @Test(expected = CarNotAvailableException.class)
    public void save_shouldThrowCarNotAvailableException_whenCarNotAvailable() {
        when(carRepository.findById(CAR.getCarId())).thenReturn(Optional.of(CAR));

        bookingService.create(BOOKING_RQ);
    }

    @Test
    public void save_shouldCallCarRepository() {
        when(carRepository.findById(CAR.getCarId())).thenReturn(Optional.of(CAR));
        when(bookingRepository.isCarAvailable(eq(BOOKING_RQ.getCarId()), any(), any())).thenReturn(Boolean.TRUE);

        bookingService.create(BOOKING_RQ);

        verify(bookingRepository).save(bookingCaptor.capture());
        Booking booking = bookingCaptor.getValue();
        assertEquals(CAR, booking.getCar());
        assertEquals(BOOKING_RQ.getTargetCountries(), booking.getTargetCountries());
        assertEquals(BOOKING_RQ.getUsage(), booking.getUsage());
    }
}