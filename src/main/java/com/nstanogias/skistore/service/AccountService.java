package com.nstanogias.skistore.service;

import com.nstanogias.skistore.domain.User;
import com.nstanogias.skistore.dtos.ApiResponse;
import com.nstanogias.skistore.dtos.LoginDto;
import com.nstanogias.skistore.dtos.RegisterDto;
import com.nstanogias.skistore.dtos.UserDto;
import com.nstanogias.skistore.repository.UserRepository;
import com.nstanogias.skistore.security.JwtTokenProvider;
import com.nstanogias.skistore.security.UserPrincipal;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class AccountService {
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;
    private final ModelMapper modelMapper;

    @NotNull
    private final UserRepository userRepository;

    public UserDto getCurrentUser() {
        UserPrincipal userPrincipal = (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<User> user = userRepository.findByEmail(userPrincipal.getUsername());

        String token = tokenProvider.generateToken(SecurityContextHolder.getContext().getAuthentication());

        return new UserDto(userPrincipal.getUsername(), user.get().getDisplayName(), token);
    }

    public ResponseEntity<UserDto> authenticateUser(LoginDto loginDto) {
        Optional<User> user = userRepository.findByEmail(loginDto.getEmail());
        if(!user.isPresent()) {
            return new ResponseEntity(new ApiResponse(false, "User not found!"),
                    HttpStatus.BAD_REQUEST);
        }

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getEmail(),
                        loginDto.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = tokenProvider.generateToken(authentication);

        UserDto userDto = modelMapper.map(user, UserDto.class);
        userDto.setToken(jwt);
        return ResponseEntity.ok(userDto);
    }

    public ResponseEntity<?> registerUser(RegisterDto registerDto) {
        if (userRepository.existsByEmail(registerDto.getEmail())) {
            return new ResponseEntity(new ApiResponse(false, "Email already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        // Creating user's account
        User userToCreate = modelMapper.map(registerDto, User.class);
        userToCreate.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userRepository.save(userToCreate);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        registerDto.getEmail(),
                        registerDto.getPassword()
                )
        );

        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new UserDto(registerDto.getEmail(), registerDto.getDisplayName(), token));
    }
}
