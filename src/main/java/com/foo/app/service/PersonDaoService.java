package com.foo.app.service;


import com.foo.app.rest.PersonInDto;
import com.foo.app.rest.PersonOutDto;

import java.util.List;

public interface PersonDaoService {

     PersonOutDto createPerson(PersonInDto personDto);
     PersonOutDto getPerson(Long id);
     List<PersonOutDto> getAll();
     PersonOutDto updatePerson(Long personId, PersonInDto personDto);
     void deletePerson(Long id);

}
