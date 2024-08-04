package com.foo.app.rest;

import com.foo.app.service.PersonDaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import java.util.List;

@RequiredArgsConstructor
@Slf4j
@Validated
@RestController
@RequestMapping("/persons")
public class PersonApiController {

    private final PersonDaoService daoService;
    @PostMapping
    @Validated(OnCreateValidation.class)
    public PersonOutDto create(@RequestBody @Valid final PersonInDto dto) {
       return daoService.createPerson(dto);
    }

    @GetMapping("/{id}")
    public PersonOutDto read(@PathVariable @Min(1) final Long id) {
       return daoService.getPerson(id);
    }

    @GetMapping(path = "/all")
    public List<PersonOutDto> readAll() {
        return daoService.getAll();
    }

    @PutMapping(path = "/{id}")
    @Validated(OnUpdateValidation.class)
    public PersonOutDto update(@PathVariable @Min(1) Long id, @RequestBody @Valid PersonInDto dto) {
        return daoService.updatePerson(id, dto);
    }

    @DeleteMapping(path = "/{id}")
    public void delete(@PathVariable @Min(1) Long id) {
        daoService.deletePerson(id);
    }


}