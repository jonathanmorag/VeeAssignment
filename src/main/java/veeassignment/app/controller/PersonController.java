package veeassignment.app.controller;

import java.util.*;
import java.util.stream.Collectors;

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
        // Check if ID already in DB
        List<Person> list = service.getAll()
                .stream()
                .filter(e -> e.getId().equals(p.getId()))
                .collect(Collectors.toList());

        if (list.size() > 0) {
            return new ResponseEntity<>("ID already exists in DB.", HttpStatus.BAD_REQUEST);
        }

        // Adding person - date validation (infection < recovery)
        int comp = p.getDateOfInfection().compareTo(p.getDateOfRecovery());
        if (comp < 0) {
            service.save(p);
            return new ResponseEntity<>(p, HttpStatus.CREATED);
        }

        // Date is not valid
        return new ResponseEntity<>("Date of recovery must be later than infection date.",
                HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/persons/{id}")
    public ResponseEntity<?> updateRecoveryDate(@RequestBody Person p, @PathVariable Integer id) {
        try {
            Person candidate = service.get(id);

            // Date validation (infection < recovery)
            int comp = candidate.getDateOfInfection().compareTo(p.getDateOfRecovery());
            if (comp < 0) {
                candidate.setDateOfRecovery(p.getDateOfRecovery());
                service.save(candidate);
                return new ResponseEntity<>(candidate, HttpStatus.OK);
            }
            // Date is not valid
            return new ResponseEntity<>("Date of recovery must be later than infection date.",
                    HttpStatus.BAD_REQUEST);
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