package com.foo.persons.db;

import jakarta.persistence.*;
import lombok.*;

//import jakarta.persistence.*;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "persons", uniqueConstraints = {
        @UniqueConstraint(
                name = "uniqueNameBodCountry",
                columnNames = {"first_name", "last_name", "birth_date", "country"})
})
public class PersonEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "person_id",nullable = false)
    private Long id;
    @Column(name = "first_name",nullable = false)
    private String firstName;
    @Column(name = "last_name",nullable = false)
    private String lastName;
    @Column(name = "birth_date",nullable = false)
    private LocalDate birthDate;
    @Column(name = "country",nullable = false)
    private String country;


}