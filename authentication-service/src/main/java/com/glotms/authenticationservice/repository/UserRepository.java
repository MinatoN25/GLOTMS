package com.glotms.authenticationservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.glotms.authenticationservice.model.User1;

@Repository
public interface UserRepository extends JpaRepository<User1, String> {

}
