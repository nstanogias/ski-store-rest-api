package com.nstanogias.skistore.web;

import com.nstanogias.skistore.domain.User;
import com.nstanogias.skistore.domain.order.Address;
import com.nstanogias.skistore.dtos.AddressDto;
import com.nstanogias.skistore.dtos.LoginDto;
import com.nstanogias.skistore.dtos.RegisterDto;
import com.nstanogias.skistore.dtos.UserDto;
import com.nstanogias.skistore.repository.UserRepository;
import com.nstanogias.skistore.security.UserPrincipal;
import com.nstanogias.skistore.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/account")
@RequiredArgsConstructor
public class AccountController {

    @Autowired
    private final ModelMapper modelMapper;

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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/address")
    public ResponseEntity<AddressDto> getUserAddress() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Address address = userRepository.findByEmail(userPrincipal.getUsername()).get().getAddress();
        if (address != null) {
            return ResponseEntity.ok(modelMapper.map(address, AddressDto.class));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/address")
    public ResponseEntity<AddressDto> updateUserAddress(@Valid @RequestBody AddressDto addressDto) {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(userPrincipal.getUsername()).get();
        user.setAddress(modelMapper.map(addressDto, Address.class));
        userRepository.save(user);
        return ResponseEntity.ok(addressDto);
    }
}
