package me.elaamiri.MockitoTutorial.repositories;

import me.elaamiri.MockitoTutorial.entities.Person;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.List;


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

    @Test
    public void PersonRepository_findAll_returnMoreThanOnePerson(){
        // Arrange // prepare test data
        Person person1 = Person.builder()
                .firstName("person1fn")
                .lastName("person1ln")
                .birthDate(new Date(Date.valueOf("1999-01-07").getTime()))
                .build();
        Person person2 = Person.builder()
                .firstName("person2fn")
                .lastName("person2ln")
                .birthDate(new Date(Date.valueOf("2004-01-07").getTime()))
                .build();

        personRepository.saveAll(Lists.list(person1, person2));
        // Act // Perform test action

        List<Person> personList = personRepository.findAll();

        // Assert
        //Assertions.assertThat(personList).isNotNull();
        //Assertions.assertThat(personList).hasSize(2);
        // We can use them in one chain
        Assertions.assertThat(personList).isNotNull().hasSize(2);


    }

    @Test
    public void PersonRepository_findById_returnCorrectPerson(){
        // Arrange
         Person person2 = Person.builder()
                 .firstName("person2fn")
                 .lastName("person2ln")
                 .birthDate(new Date(Date.valueOf("2004-01-07").getTime()))
                 .build();

         personRepository.save(person2); // person2 will get its id

         // Act
         Person retrievedPerson = personRepository.findById(person2.getId()).get();

         // Assert
         Assertions.assertThat(person2).isNotNull().isEqualTo(retrievedPerson);
    }

    @Test
    public void PersonRepository_getPersonByFirstNameContaining_returnListPersonWithFirstNameContainingKeyword(){
        // Arrange // prepare test data
        Person person1 = Person.builder()
                .firstName("person1fn")
                .lastName("person1ln")
                .birthDate(new Date(Date.valueOf("1999-01-07").getTime()))
                .build();
        Person person2 = Person.builder()
                .firstName("person2fn")
                .lastName("person2ln")
                .birthDate(new Date(Date.valueOf("2004-01-07").getTime()))
                .build();

        Person person3 = Person.builder()
                .firstName("differentFirstName")
                .lastName("person2ln")
                .birthDate(new Date(Date.valueOf("2004-01-07").getTime()))
                .build();

        personRepository.saveAll(Lists.list(person1, person2, person3));
        // Act // Perform test action

        List<Person> personList = personRepository.getPersonsByFirstNameContaining("per");

        // Assert
        Assertions.assertThat(personList).isNotNull().hasSize(2);
    }

    @Test
    public void PersonRepository_update_returnPersonNotNull(){
        Person person1 = Person.builder()
                .firstName("person1fn")
                .lastName("person1ln")
                .birthDate(new Date(Date.valueOf("1999-01-07").getTime()))
                .build();
        personRepository.save(person1);
        Person personFromDb = personRepository.findById(person1.getId()).get();
        personFromDb.setFirstName("differentName");

        Person savedAfterUpdate = personRepository.save(personFromDb);

        Assertions.assertThat(savedAfterUpdate).isNotNull().isEqualTo(personFromDb);

    }

    @Test
    public void PersonRepository_delete_returnPersonNull(){
        Person person1 = Person.builder()
                .firstName("person1fn")
                .lastName("person1ln")
                .birthDate(new Date(Date.valueOf("1999-01-07").getTime()))
                .build();

        personRepository.save(person1);

        personRepository.deleteById(person1.getId());

        Person personFromDb = personRepository.findById(person1.getId()).orElse(null);

        Assertions.assertThat(personFromDb).isNull();
    }
}
