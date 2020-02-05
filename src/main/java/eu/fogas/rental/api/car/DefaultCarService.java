package eu.fogas.rental.api.car;

import eu.fogas.rental.api.car.model.Car;
import eu.fogas.rental.error.exception.CarNotAvailableException;
import eu.fogas.rental.error.exception.CarNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class DefaultCarService implements CarService {
    private final CarRepository carRepository;

    @Autowired
    public DefaultCarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public List<Car> getAvailableCars() {
        log.debug("Getting available cars for today.");
        return carRepository.findAllAvailableCars(LocalDate.now());
    }

    @Override
    public Car getCarById(long id) {
        Optional<Car> car = carRepository.findById(id);
        car.orElseThrow(() ->
                new CarNotFoundException(id)
        );
        log.debug("Find car by id {} for today.", id);
        return carRepository.findAvailableCarById(id, LocalDate.now())
                .orElseThrow(() ->
                        new CarNotAvailableException(id));
    }
}
