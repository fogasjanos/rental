package eu.fogas.rental.api.car;

import eu.fogas.rental.api.booking.model.Booking;
import eu.fogas.rental.api.car.model.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static eu.fogas.rental.api.booking.model.Usage.DOMESTIC;
import static eu.fogas.rental.api.car.model.Brand.TOYOTA;
import static eu.fogas.rental.api.car.model.Brand.VOLVO;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CarRepositoryIntegrationTest {
    private static final LocalDate TODAY = LocalDate.now();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CarRepository carRepository;

    @Test
    public void findAllAvailableCars_shouldRetunEmptyList_whenThereIsNoAvailableCar() {
        Car rented = entityManager.persistFlushFind(Car.builder()
                .brand(TOYOTA)
                .plate("JAN-981")
                .build());
        entityManager.persistAndFlush(Booking.builder()
                .car(rented)
                .rangeFrom(TODAY)
                .rangeTo(TODAY)
                .usage(DOMESTIC)
                .build());

        List<Car> cars = carRepository.findAllAvailableCars(TODAY);

        assertNotNull(cars);
        assertTrue(cars.isEmpty());
    }

    @Test
    public void findAllAvailableCars_shouldRetunAvailableCarsOnly() {
        Car expected = entityManager.persistAndFlush(Car.builder()
                .brand(VOLVO)
                .plate("HUB-013")
                .build());
        Car rented = entityManager.persistFlushFind(Car.builder()
                .brand(TOYOTA)
                .plate("ABE-016")
                .build());
        entityManager.persistAndFlush(Booking.builder()
                .car(rented)
                .rangeFrom(TODAY)
                .rangeTo(TODAY)
                .usage(DOMESTIC)
                .build());

        List<Car> cars = carRepository.findAllAvailableCars(TODAY);

        assertNotNull(cars);
        assertEquals(1, cars.size());
        assertEquals(expected, cars.get(0));
    }

    @Test
    public void findAllAvailableCarById_shouldReturn_whenThereIsNoAvailableCar() {
        Car rented = entityManager.persistFlushFind(Car.builder()
                .brand(TOYOTA)
                .plate("TOT-979")
                .build());
        entityManager.persistAndFlush(Booking.builder()
                .car(rented)
                .rangeFrom(TODAY)
                .rangeTo(TODAY)
                .usage(DOMESTIC)
                .build());

        List<Car> cars = carRepository.findAllAvailableCars(TODAY);

        assertNotNull(cars);
        assertTrue(cars.isEmpty());
    }
}