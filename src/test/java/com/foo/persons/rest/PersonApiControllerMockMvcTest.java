package com.foo.persons.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foo.persons.service.PersonDaoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PersonApiController.class)
class PersonApiControllerMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonDaoService service;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void createPersonSuccessfully() throws Exception {
        PersonDto personDto = new PersonDto(null, "John", "Doe", LocalDate.of(1990, 1, 1), "USA");

        when(service.createPerson(any(PersonDto.class))).thenReturn(personDto);

        mockMvc.perform(post("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.country").value("USA"));
    }

    @ParameterizedTest
    @MethodSource("providePersons") void readPersonSuccessfullyParameterized(Long id, String firstName, String lastName, String birthDate, String country) throws Exception {

        when(service.getPerson(id)).thenReturn(new PersonDto(id, firstName, lastName, LocalDate.parse(birthDate), country));

        mockMvc.perform(get("/persons/"+id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.firstName").value(firstName))
                .andExpect(jsonPath("$.lastName").value(lastName))
                .andExpect(jsonPath("$.birthDate").value(birthDate))
                .andExpect(jsonPath("$.country").value(country));
    }

    public static Stream<Arguments> providePersons() {
        return Stream.of(
                Arguments.of(1L, "John", "Doe", "1990-01-01", "USA"),
                Arguments.of(2L, "Jane", "Smith", "1985-05-05", "Canada")
        );
    }

    @Test
    void readAllPersonsSuccessfully() throws Exception {
        PersonDto person1 = new PersonDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "USA");
        PersonDto person2 = new PersonDto(2L, "Jane", "Doe", LocalDate.of(1995, 5, 5), "USA");

        List<PersonDto> persons = Arrays.asList(person1, person2);

        when(service.getAll()).thenReturn(persons);

        mockMvc.perform(get("/persons/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[0].birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$[0].country").value("USA"))
                .andExpect(jsonPath("$[1].id").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].birthDate").value("1995-05-05"))
                .andExpect(jsonPath("$[1].country").value("USA"));
    }

    @Test
    void updatePersonSuccessfully() throws Exception {
        PersonDto personDto = new PersonDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "USA");

        when(service.updatePerson(any(PersonDto.class))).thenReturn(personDto);

        mockMvc.perform(put("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.country").value("USA"));
    }

    @Test
    void patchPersonSuccessfully() throws Exception {
        PersonDto personDto = new PersonDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "USA");

        when(service.patchPerson(eq(1L), any(PersonDto.class))).thenReturn(personDto);

        mockMvc.perform(patch("/persons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$.country").value("USA"));
    }

    @Test
    void deletePersonSuccessfully() throws Exception {
        mockMvc.perform(delete("/persons/1"))
                .andExpect(status().isNoContent());
    }
}