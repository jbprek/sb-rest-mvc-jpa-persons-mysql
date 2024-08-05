package com.foo.persons.service;


import com.foo.persons.rest.PersonDto;

import java.util.List;

public interface PersonDaoService {

     PersonDto createPerson(PersonDto personDto);
     PersonDto getPerson(Long id);
     List<PersonDto> getAll();
     PersonDto updatePerson(PersonDto personDto);
     PersonDto patchPerson(Long personId, PersonDto personDto);
     void deletePerson(Long id);

}
