package eu.fogas.rental;

import eu.fogas.rental.api.booking.BookingRepository;
import eu.fogas.rental.api.booking.model.Booking;
import eu.fogas.rental.api.car.CarRepository;
import eu.fogas.rental.api.car.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static eu.fogas.rental.api.booking.model.Usage.DOMESTIC;
import static eu.fogas.rental.api.booking.model.Usage.FOREIGN;
import static eu.fogas.rental.api.car.model.Brand.*;

@Configuration
@Slf4j
public class DemoDataBean {
    @Bean
    @Autowired
    public CommandLineRunner createDemoData(CarRepository carRepository, BookingRepository bookingRepository) {
        return args -> {
            saveCars(carRepository);

            Iterator<Car> carIterator = carRepository.findAll().iterator();
            saveBookings(bookingRepository, carIterator);
        };
    }

    private void saveCars(CarRepository carRepository) {
        List<Car> sampleCars = Arrays.asList(
                new Car(AUDI, "A6", "AUA-001", 71_000L),
                new Car(OPEL, "Astra", "OPA-002", 68_300L),
                new Car(TOYOTA, "Yaris", "TOY-003", 32_500L),
                new Car(TESLA, "Cybertruck", "TEC-004", 199_990L),
                new Car(SKODA, "Octavia", "SKO-005", 49_200L),
                new Car(MAZDA, "MX-5", "MAM-006", 55_200L),
                new Car(VOLVO, "V90", "VOV-007", 59_900L)
        );

        log.info("Saving sample cars to DB! Cars: {}", sampleCars);
        carRepository.saveAll(sampleCars);
    }

    private void saveBookings(BookingRepository bookingRepository, Iterator<Car> carIterator) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Booking> sampleBookings = Arrays.asList(
                new Booking(carIterator.next(), today, tomorrow, DOMESTIC, Collections.emptyList()),
                new Booking(carIterator.next(), today, tomorrow, FOREIGN, Collections.singletonList("Austria"))
        );

        log.info("Saving sample bookings to DB! Bookings: {}", sampleBookings);
        bookingRepository.saveAll(sampleBookings);
    }
}
