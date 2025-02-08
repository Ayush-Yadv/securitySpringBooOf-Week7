package com.codingshuttle.anuj.prod_ready_features.prod_ready_features.services;

import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.entities.Session;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.entities.User;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.repositories.SessionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SessionService {
    private final SessionRepo sessionRepo;
    private final int SESSION_LIMIT=2;

    public  void generateNewSession(User user, String refreshToken){
        List<Session> userSession=sessionRepo.findByUser(user);
        if(userSession.size()==SESSION_LIMIT){
            userSession.sort(Comparator.comparing(Session::getLastUsedAt));


            Session leastRecentlyUsedSession=userSession.getFirst();
            sessionRepo.delete(leastRecentlyUsedSession);
        }

        Session newSession= Session.builder()
                .user(user)
                .refreshToken(refreshToken)
                .build();
        sessionRepo.save(newSession);

    }
    public  void validateSession(String refreshToken){
        Session session=sessionRepo.findByRefreshToken(refreshToken)
                .orElseThrow(()-> new SessionAuthenticationException("session can not create with this credential"));

    }


}
