package com.codingshuttle.anuj.prod_ready_features.prod_ready_features.services;

import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.dto.SignUpDto;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.dto.UserDto;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.entities.User;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.exceptions.ResourceNotFoundException;
import com.codingshuttle.anuj.prod_ready_features.prod_ready_features.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@ToString
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(()-> new BadCredentialsException("user with email " + username + " not found"));
    }

    public User getUserById(Long userId){
        return userRepository.findById(userId)
                .orElseThrow(()-> new ResourceNotFoundException("user with id " + userId + " not found"));
    }

    public ResponseEntity<UserDto> signUp(SignUpDto signUpDto) {
        Optional<User> newUser = userRepository.findByEmail(signUpDto.getEmail());
        if(newUser.isPresent()) {
            throw new BadCredentialsException("user with email " + signUpDto.getEmail() + " already exists");
        }

        User userToBeCreated = modelMapper.map(signUpDto, User.class);
        userToBeCreated.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        User savedUser = userRepository.save(userToBeCreated);
        UserDto signedDto = modelMapper.map(savedUser, UserDto.class);

        return ResponseEntity.ok(signedDto);
    }
}
