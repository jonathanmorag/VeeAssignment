package veeassignment.app.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

    public void save(Person p) {
        repo.save(p);
    }

    public Person get(Integer id) {
        Optional<Person> candidate = repo.findById(id);
        return candidate.orElse(null);
    }

    public boolean delete(Integer id) {
        Optional<Person> candidate = repo.findById(id);
        if(candidate.isPresent()) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
}
