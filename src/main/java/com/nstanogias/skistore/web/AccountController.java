package com.nstanogias.skistore.web;

import com.nstanogias.skistore.dtos.LoginDto;
import com.nstanogias.skistore.dtos.RegisterDto;
import com.nstanogias.skistore.dtos.UserDto;
import com.nstanogias.skistore.repository.UserRepository;
import com.nstanogias.skistore.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {


    @NotNull
    private final AccountService accountService;

    @NotNull
    private final UserRepository userRepository;

    @GetMapping()
    public UserDto getCurrentUser() {
        return accountService.getCurrentUser();
    }

    @GetMapping("/emailexists")
    public boolean checkEmailExists(@RequestParam String email) {
        return userRepository.existsByEmail(email);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> authenticateUser(@Valid @RequestBody LoginDto loginDto) {
        return accountService.authenticateUser(loginDto);
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterDto registerDto) {
        return accountService.registerUser(registerDto);
    }
}
