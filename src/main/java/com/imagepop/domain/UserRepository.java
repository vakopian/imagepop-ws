package com.imagepop.domain;

import java.util.List;

import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findByFirstName(String name);

    User findByEmail(String email);
}