package com.example.BoatRegistry.controllers;

import com.example.BoatRegistry.dtos.authentication.AuthenticationRequestDto;
import com.example.BoatRegistry.dtos.authentication.AuthenticationResponseDto;
import com.example.BoatRegistry.security.JwtHelper;
import com.example.BoatRegistry.services.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@RestController
public class AccountController {

//    private final AuthenticationManager authenticationManager;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserService userService;

    private final JwtEncoder jwtEncoder;
    private final JwtHelper jwtHelper;

    public AccountController(final AuthenticationManagerBuilder authenticationManagerBuilder, UserService userService, final JwtEncoder jwtEncoder, JwtHelper jwtHelper) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userService = userService;
        this.jwtEncoder = jwtEncoder;
        this.jwtHelper = jwtHelper;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponseDto> login(@Valid @RequestBody AuthenticationRequestDto authenticationRequestDto) {
        var email = authenticationRequestDto.getEmail();
        userService.anyWithEmail(email);
        String token = jwtHelper.generateToken(email);
        var authenticationResponseDto = new AuthenticationResponseDto();
        authenticationResponseDto.setEmail(email);
        authenticationResponseDto.setToken(token);
        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponseDto);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponseDto> authorize(@Valid @RequestBody AuthenticationRequestDto loginVM) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginVM.getEmail(),
                loginVM.getPassword()
        );

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = this.createToken(authentication, false);
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setBearerAuth(jwt);
        var authenticationResponseDto = new AuthenticationResponseDto();
        authenticationResponseDto.setEmail(loginVM.getEmail());
        authenticationResponseDto.setToken(jwt);
        return new ResponseEntity<>(authenticationResponseDto, httpHeaders, HttpStatus.OK);
    }

    public String createToken(Authentication authentication, boolean rememberMe) {

        Instant now = Instant.now();
        Instant validity;
        validity = now.plus(60, ChronoUnit.MINUTES);

        // @formatter:off
        JwtClaimsSet.Builder builder = JwtClaimsSet.builder()
                .issuedAt(now)
                .expiresAt(validity)
                .subject(authentication.getName())
                .claim("auth", "authorities , authorities, piong");

        JwsHeader jwsHeader = JwsHeader.with(MacAlgorithm.HS512).build();
        return this.jwtEncoder.encode(JwtEncoderParameters.from(jwsHeader, builder.build())).getTokenValue();
    }

}
