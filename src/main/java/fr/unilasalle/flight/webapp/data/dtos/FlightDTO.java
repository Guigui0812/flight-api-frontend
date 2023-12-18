package fr.unilasalle.flight.webapp.data.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FlightDTO implements Serializable {
    private Long id;

    private String number;

    private String origin;

    private String destination;

    private LocalDate departureDate = LocalDate.now();

    private LocalTime departureTime = LocalTime.now();

    private LocalDate arrivalDate = LocalDate.now();

    private LocalTime arrivalTime = LocalTime.now();

    private PlaneDTO plane;


    public LocalDateTime getDepartureDateTime() {
        return departureDate.atTime(departureTime);
    }

    public LocalDateTime getArrivalDateTime() {
        return arrivalDate.atTime(arrivalTime);
    }
}
