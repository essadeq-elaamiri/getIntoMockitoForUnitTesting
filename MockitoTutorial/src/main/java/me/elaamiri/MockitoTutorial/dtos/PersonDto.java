package me.elaamiri.MockitoTutorial.dtos;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data @Builder
public class PersonDto {
    Long id;
    String firstName;
    String lastName;
    Date birthDate;
    Date creationDate;

}
