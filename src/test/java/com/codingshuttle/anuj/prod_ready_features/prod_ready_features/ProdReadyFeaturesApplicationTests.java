package com.codingshuttle.anuj.prod_ready_features.prod_ready_features;

import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.entities.User;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.services.JwtServices;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProdReadyFeaturesApplicationTests {

    @Autowired
    private JwtServices jwtService;

    @Test
    void contextLoads(){

        User user = new User(4L, "xyz", "q@gmail.com", "1234");
        String token = jwtService.generateAccessToken(user);
        System.out.println(token);
        Long id = jwtService.getUserIdFromToken(token);
        System.out.println(id);

    }
}
