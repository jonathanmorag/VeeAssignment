package veeassignment.app.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import veeassignment.app.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> { }
