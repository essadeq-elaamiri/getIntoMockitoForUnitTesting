package me.elaamiri.MockitoTutorial.RepositoriesTests;

import me.elaamiri.MockitoTutorial.entities.Person;
import me.elaamiri.MockitoTutorial.repositories.PersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@RunWith(SpringRunner.class) // Resolved the issue of NullPointerException in personRepository
public class PersonRepositoryTest {
    @Autowired
    private PersonRepository personRepository;

    @Test
    public void PersonRepository_saveAll_returnSavedPerson(){
        // Arrange
        Person person = Person.builder()
                .firstName("Essadeq")
                .lastName("EL AAMIRI")
                .birthDate(new Date(Date.valueOf("1999-01-07").getTime()))
                .build();

        // Act
        Person savedPerson = personRepository.save(person);

        // Assert
        Assertions.assertThat(savedPerson).isNotNull();
        Assertions.assertThat(savedPerson.getId()).isPositive();
        Assertions.assertThat(savedPerson.getFirstName()).isEqualTo(person.getFirstName());

    }

}
