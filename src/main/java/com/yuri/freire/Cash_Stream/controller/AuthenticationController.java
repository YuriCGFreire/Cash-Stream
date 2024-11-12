package com.yuri.freire.Cash_Stream.controller;

import com.yuri.freire.Cash_Stream.controller.model.AuthenticationRequest;
import com.yuri.freire.Cash_Stream.controller.model.AuthenticationResponse;
import com.yuri.freire.Cash_Stream.controller.model.RegisterRequest;
import com.yuri.freire.Cash_Stream.services.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;

import static com.yuri.freire.Cash_Stream.utils.CookieUtils.createCookie;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response){
        AuthenticationResponse authenticationResponse = authenticationService.authenticate(authenticationRequest);
        Cookie jwtCookie = createCookie("jwt", authenticationResponse.getAccessToken(), 60 * 60 * 24);
        Cookie refreshCookie =createCookie("refresh_token", authenticationResponse.getRefreshToken(), 60 * 60 * 24);
        response.addCookie(jwtCookie);
        response.addCookie(refreshCookie);
        return ResponseEntity.ok()
                .body(authenticationResponse);
    }

    @PostMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        AuthenticationResponse authenticationResponse = authenticationService.refreshToken(request, response);

        if (authenticationResponse == null) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Cookie jwtCookie = createCookie("jwt", authenticationResponse.getAccessToken(), 60 * 60 * 24);
        Cookie refreshCookie = createCookie("refresh_token", authenticationResponse.getRefreshToken(), 60 * 60 * 24 * 7);

        response.addCookie(jwtCookie);
        response.addCookie(refreshCookie);

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("Token atualizado com sucesso");
    }
}
