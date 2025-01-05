package me.elaamiri.MockitoTutorial.controllers;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import me.elaamiri.MockitoTutorial.dtos.PersonDto;
import me.elaamiri.MockitoTutorial.entities.Person;
import me.elaamiri.MockitoTutorial.services.PersonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/persons")
@AllArgsConstructor
@NoArgsConstructor
public class PersonController {
    private PersonService personService;

    @GetMapping("/")
    public List<Person> getAllPersons(){
        return personService.getAllPersons();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Person> getPerson(@PathVariable Long id){
        return ResponseEntity.ok(personService.getPersonById(id).orElse(null));
    }

    @PostMapping("/")
    //@ResponseStatus(HttpStatus.CREATED)
    /*
    @ResponseStatus is Typically Used for Void Methods:
    It is most useful when a method does not return a ResponseEntity
     */
    public ResponseEntity<Person> createPerson(@RequestBody PersonDto personDto){
        return new ResponseEntity<>(personService.createPerson(personDto), HttpStatus.CREATED);
    }

    @PutMapping("/")
    public ResponseEntity<Person> updatePerson(@RequestBody PersonDto personDto){
        return new ResponseEntity<>(personService.updatePerson(personDto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deletePerson(@PathVariable Long id){
        return new ResponseEntity<>(personService.deletePersonById(id), HttpStatus.OK);
    }
}