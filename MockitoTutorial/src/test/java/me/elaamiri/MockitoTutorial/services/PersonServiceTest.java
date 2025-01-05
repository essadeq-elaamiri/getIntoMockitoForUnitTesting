package me.elaamiri.MockitoTutorial.services;

import me.elaamiri.MockitoTutorial.dtos.PersonDto;
import me.elaamiri.MockitoTutorial.entities.Person;
import me.elaamiri.MockitoTutorial.repositories.PersonRepository;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Date;
import java.util.Optional;

//@ExtendWith(MockitoExtension.class)
@RunWith(MockitoJUnitRunner.class) // Resolved NullPointerException In @Mock objects
public class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;  // I dont want the real behavior of the repo, so I mock it

    @InjectMocks // Assuming PersonService uses PersonRepository
    private PersonService personService;

    @Test
    public void PersonService_createPerson_returnPerson(){
        Person person1 = Person.builder().firstName("salima").lastName("Gau").birthDate(new Date(Date.valueOf("1999-01-07").getTime())).build();
        PersonDto personDto = PersonDto.builder().firstName("salima").lastName("Gau").birthDate(new Date(Date.valueOf("1999-01-07").getTime())).build();
        // Apply mock: telling to Mockito, when personRepository.save() call on any Person object, return the specific object
        // So we mock the real behavior of the repo by this
        Mockito.when(personRepository.save(Mockito.any(Person.class))).thenReturn(person1);
        // Now we will test the service without touching the real behavior of the Repository
        Person created = personService.createPerson(personDto);

        Assertions.assertThat(created).isNotNull();
        Assertions.assertThat(created.getFirstName()).isEqualTo(person1.getFirstName());

    }

    @Test
    public void PersonService_getPersonById_returnPerson(){
        Person person1 = Person.builder().id(550L).firstName("salima").lastName("Gau").birthDate(new Date(Date.valueOf("1999-01-07").getTime())).build();
        PersonDto personDto = PersonDto.builder().id(550L).firstName("salima").lastName("Gau").birthDate(new Date(Date.valueOf("1999-01-07").getTime())).build();
        Mockito.when(personRepository.save(Mockito.any(Person.class))).thenReturn(person1);
        Person saved = personService.createPerson(personDto);
        Mockito.when(personRepository.findById(person1.getId())).thenReturn(Optional.of(person1));
        Person retrieved = personService.getPersonById(person1.getId()).get();
        Assertions.assertThat(retrieved).isNotNull().isEqualTo(person1);

    }

    @Test
    public void PersonService_getPersonById_2_returnPerson(){
       // Alternative approach: Mock the Person object directly instead of using a real Person object.
        Person personMock = Mockito.mock(Person.class); //Create a mocked Person object to simulate its behavior.
        Mockito.when(personRepository.findById(personMock.getId())).thenReturn(Optional.of(personMock));
        Person retrievedPerson = personService.getPersonById(personMock.getId()).get();
        Assertions.assertThat(retrievedPerson).isNotNull().isEqualTo(personMock);
        // So we tested the service getPersonById method without problems with the repo
    }

    @Test
    public void PersonService_deletePersonById_returnTrueIfDeleted(){
        Person personMock = Mockito.mock(Person.class);

        Mockito.doReturn(Optional.of(personMock)).when(personRepository).findById(1L);
        // Mock the delete method to do nothing
        Mockito.doNothing().when(personRepository).deleteById(Mockito.anyLong());
        // Call the method under test
        boolean deleted = personService.deletePersonById(1L);
        // Verify the method was called 1 time
        Mockito.verify(personRepository, Mockito.times(1)).deleteById(1L);
        // Verify the return is true
        Assertions.assertThat(deleted).isTrue();
    }

    @Test
    public void PersonService_deletePersonById_deleteByIdThrowsException(){
        Person personMock = Mockito.mock(Person.class);

        Mockito.doReturn(Optional.of(personMock)).when(personRepository).findById(1L);
        // Mock the findById method to throw an exception
        //Mockito.when(personRepository.deleteById(1L)).thenThrow(new RuntimeException("Database error")); // Does not work because deleteById return nothing its void
        Mockito.doThrow(new RuntimeException("Database error")).when(personRepository).deleteById(1L);

        // Verify that the exception is thrown
       Assertions.assertThatThrownBy(()-> personService.deletePersonById(1L))
                        .isInstanceOf(RuntimeException.class)
                       .hasMessage("Database error");

    }

    @Test
    public void PersonService_deletePersonById_returnFalseWhenFindByIdThrowsException(){
        Person personMock = Mockito.mock(Person.class);

        Mockito.doReturn(Optional.of(personMock)).when(personRepository).findById(1L);
        // Mock the findById method to throw an exception
        //Mockito.when(personRepository.deleteById(1L)).thenThrow(new RuntimeException("Database error")); // Does not work because deleteById return nothing its void
        Mockito.doThrow(new RuntimeException("Database error")).when(personRepository).deleteById(1L);

        // Verify
        Assertions.assertThat(personService.deletePersonById(1L)).isFalse();

    }

}
