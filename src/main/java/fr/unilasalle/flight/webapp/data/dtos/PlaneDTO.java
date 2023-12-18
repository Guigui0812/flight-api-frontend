package fr.unilasalle.flight.webapp.data.dtos;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class PlaneDTO implements Serializable {
    private Long id;

    private String operator;

    private String model;

    private String registration;

    private Long capacity;
}
