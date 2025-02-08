package com.codingshuttle.anuj.prod_ready_features.prod_ready_features.repositories;

import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
