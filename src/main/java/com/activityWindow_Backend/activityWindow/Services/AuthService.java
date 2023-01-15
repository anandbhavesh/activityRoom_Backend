package com.activityWindow_Backend.activityWindow.Services;


import com.activityWindow_Backend.activityWindow.Dto.AuthenticationResponse;
import com.activityWindow_Backend.activityWindow.Dto.LoginRequest;
import com.activityWindow_Backend.activityWindow.Dto.RefreshTokenRequest;
import com.activityWindow_Backend.activityWindow.Dto.RegisterRequest;
import com.activityWindow_Backend.activityWindow.JwtHelper.JwtUtil;
import com.activityWindow_Backend.activityWindow.Model.User;
import com.activityWindow_Backend.activityWindow.Model.VerificationToken;
import com.activityWindow_Backend.activityWindow.Repository.UserRepository;
import com.activityWindow_Backend.activityWindow.Repository.VerificationTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static java.time.Instant.now;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService UserDetailsService;
    private final JwtUtil jwtutil;
    private final RefreshTokenService refreshTokenService;


    @Transactional
    public void signup(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setCreated(now());
        user.setEnabled(false);

        userRepository.save(user);

        String token = generateVerificationToken(user);
        mailService.send(registerRequest.getEmail(),token);
    }



    private String generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = new VerificationToken();
        verificationToken.setToken(token);
        verificationToken.setUser(user);
        verificationTokenRepository.save(verificationToken);
        return token;
    }


    public void verifyAccount(String token) {
        Optional<VerificationToken> verificationTokenOptional = verificationTokenRepository.findByToken(token);
        try {
            verificationTokenOptional.orElseThrow(() -> new Exception());
        } catch (Exception e) {
            throw new RuntimeException("Invalid Token");
        }
        fetchUserAndEnable(verificationTokenOptional.get());
    }

    @Transactional
    private void fetchUserAndEnable(VerificationToken verificationToken) {
        String username = verificationToken.getUser().getUsername();
        User user = null;
            user = userRepository.findByUsername(username).get();

        user.setEnabled(true);
        userRepository.save(user);
    }


    @Transactional
    User getCurrentUser() {
        org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.
                getContext().getAuthentication().getPrincipal();
        try {
            return userRepository.findByUsername(principal.getUsername())
                    .orElseThrow(() -> new Exception());
        } catch (Exception e) {
            throw new RuntimeException("User name not found - " + principal.getUsername());
        }
    }




    public AuthenticationResponse login(LoginRequest loginRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        UserDetails userDetails = UserDetailsService.loadUserByUsername(loginRequest.getUsername());
        String authenticationToken = jwtutil.generateToken(userDetails);
        String refreshToken = refreshTokenService.generateRefreshToken().getToken();
        return new AuthenticationResponse(authenticationToken, refreshToken, loginRequest.getUsername());
    }

    public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {

        refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
        UserDetails userDetails = UserDetailsService.loadUserByUsername(refreshTokenRequest.getUsername());
        String authenticationToken = jwtutil.generateToken(userDetails);
        return new AuthenticationResponse(authenticationToken, refreshTokenRequest.getRefreshToken(), refreshTokenRequest.getUsername());
    }


}