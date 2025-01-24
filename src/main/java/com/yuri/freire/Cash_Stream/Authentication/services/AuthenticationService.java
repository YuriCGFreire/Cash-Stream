package com.yuri.freire.Cash_Stream.Authentication.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yuri.freire.Cash_Stream.Authentication.controllers.model.AuthenticationRequest;
import com.yuri.freire.Cash_Stream.Authentication.controllers.model.AuthenticationResponse;
import com.yuri.freire.Cash_Stream.Authentication.controllers.model.RegisterRequest;
import com.yuri.freire.Cash_Stream.Authentication.entities.Token;
import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.entities.entity_enum.TokenType;
import com.yuri.freire.Cash_Stream.Authentication.entities.repositories.TokenRepository;
import com.yuri.freire.Cash_Stream.Authentication.entities.repositories.UserRepository;
import com.yuri.freire.Cash_Stream.Exceptions.ExceptionDetails;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    public AuthenticationResponse register(RegisterRequest request){
        User user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        User savedUser = userRepository.save(user);

        String jwtToken = jwtService.generateToken(user);
        savedUserToken(savedUser, jwtToken, TokenType.ACCESS);

        String refreshToken = jwtService.generateRefreshToken(user);
        savedUserToken(savedUser, refreshToken, TokenType.REFRESH);

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        User user = userRepository.findByUsername(authenticationRequest.getUsername()).orElseThrow();
        String jwtToken = jwtService.generateToken(user);
        revokeAllTokensUser(user, "AUTHENTICATE");
        savedUserToken(user, jwtToken, TokenType.ACCESS);
        String refreshToken = jwtService.generateRefreshToken(user);
        savedUserToken(user, refreshToken, TokenType.REFRESH);
        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .build();
    }

    public AuthenticationResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try{
            String refreshToken = null;
            String userName = null;
            Cookie[] cookies = request.getCookies();
            AuthenticationResponse authenticationResponse = null;
            if(cookies != null){
                for(Cookie cookie: cookies){
                    if("refresh_token".equals(cookie.getName())){
                        refreshToken = cookie.getValue();
                        break;
                    }
                }
            }
            if(refreshToken == null){
                return null;
            }
            userName = jwtService.extractUsername(refreshToken);
            if(userName != null){
                var userDetails = this.userRepository.findByUsername(userName).orElseThrow();
                if(jwtService.validateToken(refreshToken, userDetails)){
                    String accessToken = jwtService.generateToken(userDetails);
                    revokeAllTokensUser(userDetails, "REFRESH");
                    savedUserToken(userDetails, accessToken, TokenType.ACCESS);
                    authenticationResponse = AuthenticationResponse.builder()
                            .accessToken(accessToken)
                            .refreshToken(refreshToken)
                            .build();

                }
            }
            return authenticationResponse;
        }catch (MalformedJwtException | UnsupportedJwtException | ExpiredJwtException | SignatureException | NullPointerException |
                DecodingException e){
            handleJwtException(response, e);
            return null;
        }
    }

//  if revokeArgument = REFRESH, revoke only the token wich the token_type = ACCESS
//  if revokeArgument = AUTHETICATE, revoke all types of tokens
    private void revokeAllTokensUser(User user, String revokeArgument){
        List<Token> validTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(validTokens.isEmpty()){
            return;
        }
        validTokens.forEach(token -> {
            if("REFRESH".equals(revokeArgument) && token.getTokenType() == TokenType.ACCESS){
                token.setExpired(true);
                token.setRevoked(true);
            }else if("AUTHENTICATE".equals(revokeArgument)){
                token.setExpired(true);
                token.setRevoked(true);
            }
        });
    }
    private void savedUserToken(User user, String jwtToken, TokenType tokenType){
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(tokenType)
                .isExpired(false)
                .isRevoked(false)
                .build();
        tokenRepository.save(token);;
    }

    private void handleJwtException(HttpServletResponse response, Exception exception) throws IOException {
        ExceptionDetails exceptionDetails = ExceptionDetails.builder()
                .title("JWT Token Validation Error")
                .status(HttpStatus.FORBIDDEN.value())
                .details("Error validating JWT token")
                .developerMessage(exception.getMessage())
                .timestamp(LocalDateTime.now())
                .build();

        if (exception instanceof MalformedJwtException) {
            exceptionDetails.setTitle("Malformed JWT Token");
            exceptionDetails.setDetails("The JWT token is malformed.");
        } else if (exception instanceof UnsupportedJwtException) {
            exceptionDetails.setTitle("Unsupported JWT Token");
            exceptionDetails.setDetails("The JWT token is unsupported.");
        } else if (exception instanceof ExpiredJwtException) {
            exceptionDetails.setTitle("Expired JWT Token");
            exceptionDetails.setDetails("The JWT token has expired.");
        } else if (exception instanceof SignatureException) {
            exceptionDetails.setTitle("Invalid Signature");
            exceptionDetails.setDetails("The JWT token signature is invalid.");
        } else if (exception instanceof NullPointerException) {
            exceptionDetails.setTitle("Null Pointer Exception");
            exceptionDetails.setDetails("A null pointer exception occurred.");
        } else if (exception instanceof DecodingException) {
            exceptionDetails.setTitle("Decoding Exception");
            exceptionDetails.setDetails("An error occurred while decoding the JWT token.");
        } else {
            exceptionDetails.setTitle("Unknown Error");
            exceptionDetails.setDetails("An unknown error occurred.");
        }

        ObjectMapper objectMapper = new ObjectMapper();

        objectMapper.registerModule(new JavaTimeModule());

        String jsonResponse = objectMapper.writeValueAsString(exceptionDetails);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        response.setStatus(exceptionDetails.getStatus());
        response.getWriter().write(jsonResponse);
    }
} 
