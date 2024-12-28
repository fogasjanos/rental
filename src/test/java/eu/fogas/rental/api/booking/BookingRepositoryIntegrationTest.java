package eu.fogas.rental.api.booking;

import eu.fogas.rental.api.booking.model.Booking;
import eu.fogas.rental.api.car.model.Car;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static eu.fogas.rental.api.booking.model.Usage.DOMESTIC;
import static eu.fogas.rental.api.car.model.Brand.MAZDA;
import static eu.fogas.rental.api.car.model.Brand.SKODA;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookingRepositoryIntegrationTest {
    private static final LocalDate TODAY = LocalDate.now();

    private final TestEntityManager entityManager;
    private final BookingRepository bookingRepository;

    @Autowired
    public BookingRepositoryIntegrationTest(TestEntityManager entityManager, BookingRepository bookingRepository) {
        this.entityManager = entityManager;
        this.bookingRepository = bookingRepository;
    }

    @Test
    public void isCarAvailable_shouldReturnTrue_whenCarIsAvailable() {
        Car available = entityManager.persistFlushFind(Car.builder()
                .brand(SKODA)
                .plate("CAR-345")
                .build());

        Boolean result = bookingRepository.isCarAvailable(available.getCarId(), TODAY, TODAY);

        assertTrue(result);
    }

    @Test
    public void isCarAvailable_shouldReturnFalse_whenCarIsNotAvailable() {
        Car rented = entityManager.persistFlushFind(Car.builder()
                .brand(MAZDA)
                .plate("MMM-555")
                .build());
        entityManager.persistAndFlush(Booking.builder()
                .car(rented)
                .rangeFrom(TODAY)
                .rangeTo(TODAY)
                .usage(DOMESTIC)
                .build());

        Boolean result = bookingRepository.isCarAvailable(rented.getCarId(), TODAY, TODAY);

        assertFalse(result);
    }
}