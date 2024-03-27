package org.eventmanagement.controller;


import jakarta.validation.Valid;

import java.util.Optional;

import org.eventmanagement.dto.LoginRequest;
import org.eventmanagement.dto.MessageResponse;
import org.eventmanagement.dto.TokenResponse;
import org.eventmanagement.dto.UserRegistration;
import org.eventmanagement.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/signup") 
    public ResponseEntity<?> registerUser(@RequestBody @Valid UserRegistration userRegistrationRequest,
                                          BindingResult bindingResult) throws Exception {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(new MessageResponse("Input JSON is invalid"));
        }

        if (!userRegistrationRequest.getPassword().equals(userRegistrationRequest.getConfirmPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Password and confirm password does not " +
                    "match"));
        }

        Optional<UserRegistration> savedUser = this.authService.signUp(userRegistrationRequest);
        return new ResponseEntity(savedUser.get(), HttpStatus.CREATED);
    }

    @PostMapping("/signin") 
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest,
                                              BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult);
        }

        /*

         */
        Optional<TokenResponse> tokenResponse = this.authService.signIn(loginRequest);
        return new ResponseEntity<>(tokenResponse.get(), HttpStatus.CREATED);
    }


}
