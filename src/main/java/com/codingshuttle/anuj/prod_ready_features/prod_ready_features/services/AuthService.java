package com.codingshuttle.anuj.prod_ready_features.prod_ready_features.services;

import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.dto.LoginDto;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.dto.LoginResponseDto;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.entities.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtServices jwtServices;
    private final UserService userService;

    public LoginResponseDto login(LoginDto loginDto) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getEmail(), loginDto.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String accessToken = jwtServices.generateAccessToken(user);
        String refreshToken = jwtServices.generateRefreshToken(user);

        return new LoginResponseDto(user.getId(), accessToken, refreshToken);
    }

    public LoginResponseDto refreshToken(String refreshToken) {
        Long userId = jwtServices.getUserIdFromToken(refreshToken);
        User user = userService.getUserById(userId);
        String accessToken = jwtServices.generateAccessToken(user);

        return new LoginResponseDto(userId, accessToken, refreshToken);
    }
}
