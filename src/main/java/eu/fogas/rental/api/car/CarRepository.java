package eu.fogas.rental.api.car;

import eu.fogas.rental.api.car.model.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface CarRepository extends CrudRepository<Car, Long> {

    List<Car> findAllAvailableCars(@Param("date") LocalDate date);

    Optional<Car> findAvailableCarById(@Param("carId") long carId, @Param("date") LocalDate date);

}
