package eu.fogas.rental.api.booking.model;

import eu.fogas.rental.api.car.model.Car;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@NamedNativeQuery(name = "Booking.isCarAvailable",
        query = "SELECT COUNT(car_id) = 0 " +
                "FROM Booking " +
                "WHERE car_id = :carId AND " +
                "   (:rangeFrom = range_from OR :rangeFrom BETWEEN range_from AND range_to OR :rangeTo BETWEEN range_from AND range_to)"
)
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingId;

    @ManyToOne
    @JoinColumn(name = "carId")
    private Car car;

    private LocalDate rangeFrom;

    private LocalDate rangeTo;

    private Usage usage;

    @ElementCollection(targetClass = String.class)
    private List<String> targetCountries;

    public Booking(Car car, LocalDate rangeFrom, LocalDate rangeTo, Usage usage, List<String> targetCountries) {
        this.car = car;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
        this.usage = usage;
        this.targetCountries = targetCountries;
    }
}
