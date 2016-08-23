package com.imagepop.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByFirstName(String name);

    User findByEmail(String email);
}