package com.codingshuttle.anuj.prod_ready_features.prod_ready_features.repositories;

import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.entities.Session;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface  SessionRepo extends JpaRepository<Session,Long> {

    List<Session> findByUser(User user);

    Optional<Session> findByRefreshToken(String refreshToken);
}
