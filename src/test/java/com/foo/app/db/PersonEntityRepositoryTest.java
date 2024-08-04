package com.foo.app.db;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;



@DataJpaTest
/* Note the following is needed to use a MySQL test datasource, otherwise H2 is used by default */
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
/* Note the following is needed to override the main @Bean configured DataSource */
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:mysql://localhost:3306/test_persons", // Configure your custom DataSource properties
        "spring.datasource.username=test_persons",
        "spring.datasource.password=test_persons"
})
class PersonEntityRepositoryTest {

    @Autowired // Note this is needed Lombok constructors do not work
    private PersonEntityRepository repository;

    private PersonEntity testPerson;


    @BeforeEach
    public void setUp() {
        // Initialize test data before each test method
        repository.truncateTable();

        var person = PersonEntity.builder()
                .firstName("John")
                .lastName("Doe")
                .birthDate(LocalDate.of(1980, 1, 1))
                .country("USA").build();

        testPerson = repository.save(person);
    }

    @AfterEach
    public void tearDown() {
        // Release test data after each test method
        repository.delete(testPerson);
    }

    @DisplayName("PersonEntityRepository Test - test save method")
    @Test
    void testCreate() {
        var anotherPerson = PersonEntity.builder()
                .firstName("Peter")
                .lastName("Smith")
                .birthDate(LocalDate.of(1985, 1, 1))
                .country("USA").build();
        var person = repository.save(anotherPerson);
        assertSameEntity(anotherPerson, person);
    }

    @DisplayName("PersonEntityRepository Test - findById method")
    @Test
    void testRead() {
        var person = repository.findById(testPerson.getId());
        assertTrue(person.isPresent());
        assertSameEntity(testPerson, person.get());
    }

    @DisplayName("PersonEntityRepository Test - findAll method")
    @Test
    void testReadAll() {
        var personList = repository.findAll();
        assertNotNull(personList);
        assertEquals(1,personList.size());
        assertSameEntity(testPerson, personList.get(0));
    }

    private void assertSameEntity(PersonEntity expected, PersonEntity actual) {
        assertAll(
                () -> assertEquals(expected.getFirstName(), actual.getFirstName()),
                () -> assertEquals(expected.getLastName(), actual.getLastName()),
                () -> assertEquals(expected.getBirthDate(), actual.getBirthDate()),
                () -> assertEquals(expected.getCountry(), actual.getCountry())
        );
    }

}