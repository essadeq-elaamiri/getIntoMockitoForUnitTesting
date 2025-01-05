package me.elaamiri.MockitoTutorial.services;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import me.elaamiri.MockitoTutorial.dtos.PersonDto;
import me.elaamiri.MockitoTutorial.entities.Person;
import me.elaamiri.MockitoTutorial.repositories.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

@Service
@AllArgsConstructor @NoArgsConstructor
@Slf4j
public class PersonService {
    PersonRepository personRepository;

    public Optional<Person> getPersonById(long id){
        log.info("Get Person By Id | {}", id);
        return personRepository.findById(id);
    }

    public List<Person> getAllPersons(){
        log.info("Get All Persons ");
        return personRepository.findAll();
    }

    public Person createPerson(PersonDto person){
        log.info("create Person | {}", person.getFirstName());
        Person personToCreate= Person.builder().firstName(person.getFirstName())
                .lastName(person.getLastName())
                .birthDate(person.getBirthDate())
                .creationDate(new Date())
                .lastModificationDate(new Date()).build();
        return personRepository.save(personToCreate);
    }

    public Person updatePerson(PersonDto person){
        log.info("update Person | {}", person.getFirstName());
        if(Objects.isNull(person.getId())){
            log.error("You are trying to edit a person with no id. Name:{}", person.getFirstName());
            return null;
        }
        Person retrieved = personRepository.findById(person.getId()).orElse(null);
        if(Objects.isNull(retrieved)){
            log.error("No person with this id. ID:{}", person.getId());
            return null;
        }

        retrieved.setFirstName(person.getFirstName());
        retrieved.setLastName(person.getLastName());
        retrieved.setBirthDate(person.getBirthDate());
        retrieved.setCreationDate(person.getCreationDate());
        retrieved.setLastModificationDate(new Date());

        return personRepository.save(retrieved);
    }

    public boolean deletePersonById(long id){
        log.info("delete Person | ID:{}", id);
        Person retrieved = personRepository.findById(id).orElse(null);
        if(Objects.isNull(retrieved)){
            log.error("No person with this id. ID:{}", id);
            return false;
        }
        try{personRepository.deleteById(id);}
        catch (Exception exception){
            log.error("Can not delete person. ID:{}", id);
            log.error("Exception |{}", exception.getMessage());
            return false;
        }
        return true;
    }
}
