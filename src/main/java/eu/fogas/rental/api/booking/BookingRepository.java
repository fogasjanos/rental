package eu.fogas.rental.api.booking;

import eu.fogas.rental.api.booking.model.Booking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface BookingRepository extends CrudRepository<Booking, Long> {

    Boolean isCarAvailable(@Param("carId") long carId, @Param("rangeFrom") LocalDate rangeFrom, @Param("rangeTo") LocalDate rangeTo);

}
