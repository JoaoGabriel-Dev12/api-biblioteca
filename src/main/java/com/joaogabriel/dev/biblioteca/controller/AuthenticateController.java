package com.joaogabriel.dev.biblioteca.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.joaogabriel.dev.biblioteca.service.AuthenticationService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping
public class AuthenticateController {
    private final AuthenticationService service;

    public AuthenticateController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping("authenticate")
    public String authenticate(Authentication authentication) {
        return service.authenticate(authentication);
    }
    
}
