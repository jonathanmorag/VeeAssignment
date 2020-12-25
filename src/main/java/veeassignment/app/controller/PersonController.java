package veeassignment.app.controller;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import veeassignment.app.service.PersonService;
import veeassignment.app.model.Person;

@RestController
public class PersonController {

    private final PersonService service;

    @Autowired
    public PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping("/persons")
    public List<Person> list(@RequestParam(value = "sort", required = false) Integer sort) {
        return sort == null ? service.getAll() : service.getAllSortedByInfectionDate();
    }

    @GetMapping("/persons/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id) {
        try {
            Person p = service.get(id);
            return new ResponseEntity<>(p, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/persons")
    public ResponseEntity<?> add(@RequestBody Person p) {
        try {
            service.save(p);
            return new ResponseEntity<>(p, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/persons/{id}")
    public ResponseEntity<?> updateRecoveryDate(@RequestBody Person p, @PathVariable Integer id) {
        try {
            Person updatedPerson = service.updateRecoveryDate(p.getDateOfRecovery(), id);
            return new ResponseEntity<>(updatedPerson, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            service.delete(id);
            return new ResponseEntity<>("Person with ID: " + id + " has been deleted successfully.",
                    HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

}