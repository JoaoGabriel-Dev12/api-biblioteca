package com.joaogabriel.dev.biblioteca.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.joaogabriel.dev.biblioteca.repository.UserRepository;
import com.joaogabriel.dev.biblioteca.security.UserAuthenticated;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
            .map(user -> new UserAuthenticated(user))
            .orElseThrow(() -> new UsernameNotFoundException("User not found. Username: " +username));
    }

}
