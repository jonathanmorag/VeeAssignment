package veeassignment.app.service;

import java.sql.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import veeassignment.app.dao.PersonRepository;
import veeassignment.app.model.Person;

@Service
public class PersonService {

    private final PersonRepository repo;

    @Autowired
    public PersonService(PersonRepository repo) {
        this.repo = repo;
    }

    public List<Person> getAll() {
        return repo.findAll();
    }

    public List<Person> getAllSortedByInfectionDate() {
        return repo.findAllByOrderByDateOfInfectionAsc();
    }

    public void save(Person p) throws Exception {
        boolean exists = this.getAll().stream().anyMatch(e -> e.getId().equals(p.getId()));

        if (exists) throw new IllegalAccessException("Person with this ID already exists in DB.");

        if (dateValidator(p.getDateOfInfection(), p.getDateOfRecovery())) {
            repo.save(p);
        }
        else throw new IllegalArgumentException("Date of recovery must be later than infection date.");
    }

    public Person get(Integer id) {
        return repo.findById(id).get();
    }

    public Person updateRecoveryDate(Date recoveryDate, Integer id) {
        Person p = this.get(id);
        if(dateValidator(p.getDateOfInfection(), recoveryDate)) {
            throw new IllegalArgumentException("Date of recovery must be later than infection date.");
        }
        p.setDateOfRecovery(recoveryDate);
        repo.save(p);
        return p;
    }

    public void delete(Integer id) {
        Optional<Person> candidate = repo.findById(id);
        if(candidate.isPresent()) {
            repo.deleteById(id);
        }
        else throw new NoSuchElementException();
    }

    private static boolean dateValidator(Date infectionDate, Date recoveryDate) {
        return infectionDate.compareTo(recoveryDate) < 0;
    }
}
