package eu.fogas.rental.api.booking;

import eu.fogas.rental.api.booking.model.Booking;
import eu.fogas.rental.api.booking.model.BookingRequest;
import eu.fogas.rental.api.booking.model.Usage;
import eu.fogas.rental.api.car.CarRepository;
import eu.fogas.rental.api.car.model.Brand;
import eu.fogas.rental.api.car.model.Car;
import eu.fogas.rental.error.exception.CarNotAvailableException;
import eu.fogas.rental.error.exception.CarNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultBookingServiceTest {
    private static final Date TODAY = new Date();
    private static final Car CAR = Car.builder()
            .carId(16)
            .brand(Brand.OPEL)
            .plate("BND-007")
            .build();
    private static final BookingRequest BOOKING_RQ = new BookingRequest(CAR.getCarId(), Usage.DOMESTIC, null, TODAY, TODAY);

    @InjectMocks
    private DefaultBookingService bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CarRepository carRepository;
    @Captor
    private ArgumentCaptor<Booking> bookingCaptor;

    @Test
    public void save_shouldThrowCarNotFoundException_whenCarNotExists() {
        when(carRepository.findById(anyLong())).thenReturn(Optional.empty());

        var e = assertThrows(CarNotFoundException.class,
                () -> bookingService.create(BOOKING_RQ));

        assertEquals("Car not found. Id: 16", e.getMessage());
    }

    @Test
    public void save_shouldThrowCarNotAvailableException_whenCarNotAvailable() {
        when(carRepository.findById(CAR.getCarId())).thenReturn(Optional.of(CAR));

        var e = assertThrows(CarNotAvailableException.class,
                () -> bookingService.create(BOOKING_RQ));

        assertEquals("Car not available. Id: 16", e.getMessage());
    }

    @Test
    public void save_shouldCallCarRepository() {
        when(carRepository.findById(CAR.getCarId())).thenReturn(Optional.of(CAR));
        when(bookingRepository.isCarAvailable(eq(BOOKING_RQ.carId()), any(), any())).thenReturn(Boolean.TRUE);

        bookingService.create(BOOKING_RQ);

        verify(bookingRepository).save(bookingCaptor.capture());
        Booking booking = bookingCaptor.getValue();
        assertEquals(CAR, booking.getCar());
        assertEquals(BOOKING_RQ.targetCountries(), booking.getTargetCountries());
        assertEquals(BOOKING_RQ.usage(), booking.getUsage());
    }
}