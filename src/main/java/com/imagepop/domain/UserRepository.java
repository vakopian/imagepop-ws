package com.imagepop.domain;

import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}