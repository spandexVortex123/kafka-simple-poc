package com.example.client.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.client.model.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
    
    @Query(value = "select p from Person p where p.email = :email")
    List<Person> getByEmailId(String email);

    @Query(value = "select p from Person p where lower(p.firstName) = :firstName")
    List<Person> getByFirstName(String firstName);
}
