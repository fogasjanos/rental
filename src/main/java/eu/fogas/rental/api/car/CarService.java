package eu.fogas.rental.api.car;

import eu.fogas.rental.api.car.model.Car;

import java.util.List;

public interface CarService {

    List<Car> getAvailableCars();

    Car getCarById(long id);

}
