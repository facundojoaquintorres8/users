package com.ar.users.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ar.users.dto.SignUpRequestDTO;
import com.ar.users.service.IUserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    @Autowired
    private IUserService service;
    
    @PostMapping("/sign-up")
    public ResponseEntity<Object> signUp(@RequestBody SignUpRequestDTO request) throws Exception {
        return new ResponseEntity<>(service.signUp(request), HttpStatus.OK);
    }

    @GetMapping("/login")
    public ResponseEntity<Object> login(@RequestHeader(name = "token") String token) throws Exception {
        return new ResponseEntity<>(service.login(token), HttpStatus.OK);
    }

}
