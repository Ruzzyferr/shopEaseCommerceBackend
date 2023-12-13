package com.example.shopease.config;

import com.example.shopease.entity.User;
import com.example.shopease.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> kullanici = userRepository.findByUsername(username);

        if (userRepository.existsByUsername(username)) {

            List<GrantedAuthority> authorities = new ArrayList<>();
            authorities.add(new SimpleGrantedAuthority(kullanici.get().getRole().toString()));
            return new org.springframework.security.core.userdetails.User(username, kullanici.get().getPassword(), authorities);
        }

        throw new UsernameNotFoundException(username);
    }

}
