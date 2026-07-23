package eu.fogas.rental;

import eu.fogas.rental.api.booking.BookingRepository;
import eu.fogas.rental.api.booking.model.Booking;
import eu.fogas.rental.api.booking.model.Country;
import eu.fogas.rental.api.car.CarRepository;
import eu.fogas.rental.api.car.model.Car;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import static eu.fogas.rental.api.booking.model.Country.AUSTRIA;
import static eu.fogas.rental.api.booking.model.Usage.DOMESTIC;
import static eu.fogas.rental.api.booking.model.Usage.FOREIGN;
import static eu.fogas.rental.api.car.model.Brand.*;

@Configuration
@Slf4j
public class DemoDataBean {

    @Bean
    public CommandLineRunner createDemoData(CarRepository carRepository, BookingRepository bookingRepository) {
        return args -> {
            saveCars(carRepository);

            Iterator<Car> carIterator = carRepository.findAll().iterator();
            saveBookings(bookingRepository, carIterator);
        };
    }

    private void saveCars(CarRepository carRepository) {
        List<Car> sampleCars = Arrays.asList(
                Car.builder()
                        .brand(AUDI)
                        .model("A6")
                        .plate("AUA-001")
                        .price(71_000L)
                        .build(),
                Car.builder()
                        .brand(OPEL)
                        .model("Astra")
                        .plate("OPA-002")
                        .price(68_300L)
                        .build(),
                Car.builder()
                        .brand(TOYOTA)
                        .model("Yaris")
                        .plate("TOY-003")
                        .price(32_500L)
                        .build(),
                Car.builder()
                        .brand(TESLA)
                        .model("Cybertruck")
                        .plate("TEC-004")
                        .price(199_990L)
                        .build(),
                Car.builder()
                        .brand(SKODA)
                        .model("Octavia")
                        .plate("SKO-005")
                        .price(49_200L)
                        .build(),
                Car.builder()
                        .brand(MAZDA)
                        .model("MX-5")
                        .plate("MAM-006")
                        .price(55_200L)
                        .build(),
                Car.builder()
                        .brand(VOLVO)
                        .model("V90")
                        .plate("VOV-007")
                        .price(59_900L)
                        .build());

        log.info("Saving sample cars to DB! Cars: {}", sampleCars);
        carRepository.saveAll(sampleCars);
    }

    private void saveBookings(BookingRepository bookingRepository, Iterator<Car> carIterator) {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        List<Booking> sampleBookings = Arrays.asList(
                Booking.builder()
                        .car(carIterator.next())
                        .rangeFrom(today)
                        .rangeTo(tomorrow)
                        .usage(DOMESTIC)
                        .targetCountries(EnumSet.noneOf(Country.class))
                        .build(),
                Booking.builder()
                        .car(carIterator.next())
                        .rangeFrom(today)
                        .rangeTo(tomorrow)
                        .usage(FOREIGN)
                        .targetCountries(EnumSet.of(AUSTRIA))
                        .build()
        );

        log.info("Saving sample bookings to DB! Bookings: {}", sampleBookings);
        bookingRepository.saveAll(sampleBookings);
    }
}
