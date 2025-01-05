package me.elaamiri.MockitoTutorial.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import me.elaamiri.MockitoTutorial.dtos.PersonDto;
import me.elaamiri.MockitoTutorial.entities.Person;
import me.elaamiri.MockitoTutorial.services.PersonService;
import org.assertj.core.util.Lists;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.sql.Date;
import java.util.Optional;


// We can use WebMvcTest instead for JUnit5
// @RunWith(SpringRunner.class)
// @WebMvcTest(PersonController.class)
/*
@WebMvcTest is a Spring Boot annotation that focuses only on testing the web layer (controllers). It auto-configures MockMvc and loads only the relevant components.
It’s faster than loading the entire application context and is ideal for unit testing controllers.
 */
@RunWith(MockitoJUnitRunner.class) // JUnit4
public class PersonControllerTest {
    private MockMvc mockMvc; // Used to simulate HTTP requests

    //@MockBean // with @WebMvcTest JUnit 5
    @Mock
    private PersonService personService;
    @InjectMocks
    private PersonController personController;

    private Person person1;
    private PersonDto personDto;

    private static final String API_BASE_URL = "/api/persons";

    @Before
    public void setup(){
        // Initialize MockMvc with the controller
        mockMvc = MockMvcBuilders.standaloneSetup(personController).build();

        person1 = Person.builder().firstName("salima").lastName("Gau").birthDate(new Date(Date.valueOf("1999-01-07").getTime())).build();
        personDto = PersonDto.builder().firstName("salima").lastName("Gau").birthDate(new Date(Date.valueOf("1999-01-07").getTime())).build();
    }

    @Test
    public void PersonController_getAllPersons_returnNotEmptyList() throws Exception{
        // Mock the service Behavior
        Mockito.when(personService.getAllPersons()).thenReturn(Lists.list(person1));
        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get(API_BASE_URL+"/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(person1.getFirstName()));
    }

    @Test
    public void PersonController_getPerson_returnPerson() throws Exception{
        // Arrange
        Long id = 1L;
        // Mock the service Behavior
        Mockito.when(personService.getPersonById(Mockito.anyLong())).thenReturn(Optional.ofNullable(person1));

        mockMvc.perform(MockMvcRequestBuilders.get(API_BASE_URL+"/"+id))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(person1.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("gh"));

        // Test Failed
        /*
        Result output :
        java.lang.AssertionError: JSON path "$.lastName"
        Expected :gh
        Actual   :Gau
         */
    }

    @Test
    public void PersonController_createPerson_returnPerson() throws Exception{
        // Mock the service Behavior
        Mockito.when(personService.createPerson(Mockito.any(PersonDto.class))).thenReturn(person1);

        mockMvc.perform(MockMvcRequestBuilders.post(API_BASE_URL+"/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content((new ObjectMapper()).writeValueAsString(person1)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(person1.getFirstName()));
    }
}
