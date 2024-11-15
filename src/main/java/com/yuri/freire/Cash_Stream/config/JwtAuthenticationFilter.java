package com.yuri.freire.Cash_Stream.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.yuri.freire.Cash_Stream.entities.repositories.TokenRepository;
import com.yuri.freire.Cash_Stream.exceptions.ExceptionDetails;
import com.yuri.freire.Cash_Stream.services.JwtService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.DecodingException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    private final TokenRepository tokenRepository;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        try {
            String jwt = null;
            String username = null;
            Cookie[] cookies = request.getCookies();

            String requestURI = request.getRequestURI();
            if (requestURI.startsWith("/auth")) {
                filterChain.doFilter(request, response);
                return;
            }

            if(cookies != null){
                for(Cookie cookie: cookies){
                    if("jwt".equals(cookie.getName())){
                        jwt = cookie.getValue();
                        break;
                    }
                }
            }

            if(jwt == null){
                filterChain.doFilter(request, response);
                return;
            }

            username = jwtService.extractUsername(jwt);
            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                Boolean isTokenValid = this.tokenRepository.findByToken(jwt)
                        .map(t -> !t.isExpired() && !t.isRevoked())
                        .orElse(false);
                if(jwtService.validateToken(jwt, userDetails) && Boolean.TRUE.equals(isTokenValid)){
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }catch (MalformedJwtException | UnsupportedJwtException | ExpiredJwtException | SignatureException | NullPointerException | DecodingException e){
            handleJwtException(response, e);
            return;
        }
        filterChain.doFilter(request, response);
    }
    private void handleJwtException(HttpServletResponse response, Exception exception) throws IOException{
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
