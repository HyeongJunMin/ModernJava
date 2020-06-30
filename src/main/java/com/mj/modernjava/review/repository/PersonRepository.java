package com.mj.modernjava.review.repository;

import com.mj.modernjava.review.domain.Person;
import org.springframework.data.repository.CrudRepository;


public interface PersonRepository extends CrudRepository<Person, String> {
    Person findByName(String name);
}
