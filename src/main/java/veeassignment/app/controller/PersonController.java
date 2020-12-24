package veeassignment.app.controller;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Person p = service.get(id);

        // Check if person exists in DB
        if (p != null)
            return new ResponseEntity<>(p, HttpStatus.OK);

        // Person with given id NOT in DB
        Map<String, String> map = new HashMap<>();
        map.put("message", "No such element with this id.");
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);

    }

    @PostMapping("/persons")
    public ResponseEntity<?> add(@RequestBody Person p) {
        int comp = p.getDateOfInfection().compareTo(p.getDateOfRecovery());
        // Date validation (infection < recovery)
        if(comp < 0) {
            service.save(p);
            return new ResponseEntity<>(p, HttpStatus.OK);
        }

        // Date is not valid
        Map<String, String> map = new HashMap<>();
        map.put("message", "Date of recovery must be later than infection date.");
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @PutMapping("/persons/{id}")
    public ResponseEntity<?> updateRecoveryDate(@RequestBody Person p, @PathVariable Integer id) {
        Person candidate = service.get(id);
        // Check if candidate exists in DB
        if (candidate != null) {
            int comp = candidate.getDateOfInfection().compareTo(p.getDateOfRecovery());
            // Date validation (infection < recovery)
            if(comp < 0) {
                candidate.setDateOfRecovery(p.getDateOfRecovery());
                service.save(candidate);
                return new ResponseEntity<>(candidate, HttpStatus.OK);
            }
            // Date is not valid
            Map<String, String> map = new HashMap<>();
            map.put("message", "Date of recovery must be later than infection date.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        // Candidate NOT exists in DB
        Map<String, String> map = new HashMap<>();
        map.put("message", "No such element with this id.");
        return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/persons/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        Map<String, String> map = new HashMap<>();
        if (!service.delete(id)) {
            map.put("message", "No such element with this id.");
            return new ResponseEntity<>(map, HttpStatus.NOT_FOUND);
        }
        map.put("message", "Person with ID: " + id + " has been deleted successfully.");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

}
