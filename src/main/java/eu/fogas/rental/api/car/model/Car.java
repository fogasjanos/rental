package eu.fogas.rental.api.car.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@SqlResultSetMapping(name = "Car.mapping",
        entities = @EntityResult(entityClass = Car.class, fields = {
                @FieldResult(name = "carId", column = "cCarId"),
                @FieldResult(name = "brand", column = "cBrand"),
                @FieldResult(name = "model", column = "cModel"),
                @FieldResult(name = "plate", column = "cPlate"),
                @FieldResult(name = "price", column = "cPrice")
        }))
@NamedNativeQuery(name = "Car.findAllAvailableCars",
        resultSetMapping = "Car.mapping",
        query = "SELECT C.car_id as cCarId, C.brand as cBrand, C.model as cModel, C.plate as cPlate, C.price as cPrice " +
                "FROM CAR C " +
                "LEFT JOIN (SELECT * FROM BOOKING WHERE :date = range_from OR :date BETWEEN range_from AND range_to) B ON C.car_Id = B.car_Id " +
                "WHERE B.car_id IS NULL"
)
@NamedNativeQuery(name = "Car.findAvailableCarById",
        resultSetMapping = "Car.mapping",
        query = "SELECT C.car_id as cCarId, C.brand as cBrand, C.model as cModel, C.plate as cPlate, C.price as cPrice " +
                "FROM CAR C " +
                "   LEFT JOIN (SELECT * FROM BOOKING WHERE :date = range_from OR :date BETWEEN range_from AND range_to) B " +
                "   ON C.car_Id = B.car_Id " +
                "WHERE C.car_id = :carId AND B.car_id IS NULL"
)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long carId;
    private Brand brand;
    private String model;
    private String plate;
    private long price;

    public Car(Brand brand, String model, String plate, long price) {
        this.brand = brand;
        this.model = model;
        this.plate = plate;
        this.price = price;
    }
}
