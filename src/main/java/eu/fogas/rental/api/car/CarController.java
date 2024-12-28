package eu.fogas.rental.api.car;

import eu.fogas.rental.api.car.model.Car;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/cars")
public class CarController {
    private final CarService carService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Car> getCars() {
        return carService.getAvailableCars();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Car getCar(@PathVariable long id) {
        return carService.getCarById(id);
    }
}
