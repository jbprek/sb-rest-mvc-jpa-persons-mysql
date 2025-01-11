package com.foo.persons.rest;

import com.foo.persons.service.PersonDaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class PersonApiControllerRestTemplateTest {
    // Fixing TestRestTemplate issue with PATCH
    @TestConfiguration
    static class PersonApiControllerTestConfiguration {
        public TestRestTemplate testRestTemplate(RestTemplateBuilder builder) {
            return new TestRestTemplate(builder
                    .setConnectTimeout(Duration.ofSeconds(10))
                    .setReadTimeout(Duration.ofSeconds(10)));
        }
    }

    @Autowired
    private TestRestTemplate restTemplate;


    @MockBean
    private PersonDaoService service;

    @Test
    void createPersonSuccessfully() {
        PersonDto personDto = new PersonDto(null, "John", "Doe", LocalDate.of(1990, 1, 1), "USA");

        when(service.createPerson(any(PersonDto.class))).thenReturn(personDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PersonDto> request = new HttpEntity<>(personDto, headers);

        ResponseEntity<PersonDto> response = restTemplate.postForEntity("/persons", request, PersonDto.class);

        assertThat(response.getStatusCode().value()).isEqualTo(201);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().firstName()).isEqualTo("John");
        assertThat(response.getBody().lastName()).isEqualTo("Doe");
        assertThat(response.getBody().birthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(response.getBody().country()).isEqualTo("USA");
    }

    @Test
    void readPersonSuccessfully() {
        PersonDto personDto = new PersonDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "USA");

        when(service.getPerson(1L)).thenReturn(personDto);

        ResponseEntity<PersonDto> response = restTemplate.getForEntity("/persons/1", PersonDto.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(1L);
        assertThat(response.getBody().firstName()).isEqualTo("John");
        assertThat(response.getBody().lastName()).isEqualTo("Doe");
        assertThat(response.getBody().birthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(response.getBody().country()).isEqualTo("USA");
    }

    @Test
    void readAllPersonsSuccessfully() {
        PersonDto person1 = new PersonDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "USA");
        PersonDto person2 = new PersonDto(2L, "Jane", "Doe", LocalDate.of(1995, 5, 5), "USA");

        List<PersonDto> persons = Arrays.asList(person1, person2);

        when(service.getAll()).thenReturn(persons);

        ResponseEntity<PersonDto[]> response = restTemplate.getForEntity("/persons/all", PersonDto[].class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);
        assertThat(response.getBody()[0].id()).isEqualTo(1L);
        assertThat(response.getBody()[0].firstName()).isEqualTo("John");
        assertThat(response.getBody()[0].lastName()).isEqualTo("Doe");
        assertThat(response.getBody()[0].birthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(response.getBody()[0].country()).isEqualTo("USA");
        assertThat(response.getBody()[1].id()).isEqualTo(2L);
        assertThat(response.getBody()[1].firstName()).isEqualTo("Jane");
        assertThat(response.getBody()[1].lastName()).isEqualTo("Doe");
        assertThat(response.getBody()[1].birthDate()).isEqualTo(LocalDate.of(1995, 5, 5));
        assertThat(response.getBody()[1].country()).isEqualTo("USA");
    }

    @Test
    void updatePersonSuccessfully() {
        PersonDto personDto = new PersonDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "USA");

        when(service.updatePerson(any(PersonDto.class))).thenReturn(personDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PersonDto> request = new HttpEntity<>(personDto, headers);

        ResponseEntity<PersonDto> response = restTemplate.exchange("/persons", HttpMethod.PUT, request, PersonDto.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(1L);
        assertThat(response.getBody().firstName()).isEqualTo("John");
        assertThat(response.getBody().lastName()).isEqualTo("Doe");
        assertThat(response.getBody().birthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(response.getBody().country()).isEqualTo("USA");
    }

    @Test
    void patchPersonSuccessfully() {
        PersonDto personDto = new PersonDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1), "USA");

        when(service.patchPerson(eq(1L), any(PersonDto.class))).thenReturn(personDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PersonDto> request = new HttpEntity<>(personDto, headers);

        ResponseEntity<PersonDto> response = restTemplate.exchange("/persons/1", HttpMethod.PATCH, request, PersonDto.class);

        assertThat(response.getStatusCode().value()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().id()).isEqualTo(1L);
        assertThat(response.getBody().firstName()).isEqualTo("John");
        assertThat(response.getBody().lastName()).isEqualTo("Doe");
        assertThat(response.getBody().birthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
        assertThat(response.getBody().country()).isEqualTo("USA");
    }

    @Test
    void deletePersonSuccessfully() {
        ResponseEntity<Void> response = restTemplate.exchange("/persons/1", HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatusCode.valueOf(204));
    }
}