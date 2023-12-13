package com.example.shopease.controller;

import com.example.shopease.config.JwtService;
import com.example.shopease.dto.LoginBackDto;
import com.example.shopease.dto.LoginDto;
import com.example.shopease.dto.UserDto;
import com.example.shopease.dto.UserSaveRequestDto;
import com.example.shopease.entity.User;
import com.example.shopease.repository.UserRepository;
import com.example.shopease.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    @Autowired
    private JwtService jwtService;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/save")
    public ResponseEntity<UserDto> save(@RequestBody UserSaveRequestDto dto) {

        UserDto userDto = userService.save(dto);

        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginBackDto> login(@RequestBody LoginDto dto) {
        Optional<User> gecici = Optional.ofNullable(userRepository.findByUsername(dto.getUsername()).orElseThrow(
                () -> new UsernameNotFoundException("User not found")
        ));

        LoginBackDto loginBackDto = userService.login(dto);


        return new ResponseEntity<>(loginBackDto, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        String token = request.getHeader("Authorization");

        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7); // "Bearer " ifadesini çıkartıyoruz
            jwtService.invalidateToken(token); // Tokenı geçersiz kılma
            return new ResponseEntity<>("Logged out successfully", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Invalid token", HttpStatus.BAD_REQUEST);
        }
    }

}
