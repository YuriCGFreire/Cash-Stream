package com.yuri.freire.Cash_Stream.Authentication.controllers;

import com.yuri.freire.Cash_Stream.Authentication.controllers.model.AuthenticationRequest;
import com.yuri.freire.Cash_Stream.Authentication.controllers.model.AuthenticationResponse;
import com.yuri.freire.Cash_Stream.Authentication.controllers.model.RegisterRequest;
import com.yuri.freire.Cash_Stream.Authentication.services.AuthenticationService;
import com.yuri.freire.Cash_Stream.Response.ApiResponse;
import com.yuri.freire.Cash_Stream.Response.ResponseUtil;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;

import static com.yuri.freire.Cash_Stream.Utils.CookieUtils.createCookie;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthenticationResponse>> register(@Valid @RequestBody RegisterRequest request, HttpServletRequest servletRequestrequest){
        AuthenticationResponse registeredUser = authenticationService.register(request);
        ApiResponse<AuthenticationResponse> response = ResponseUtil.success(registeredUser, "User registered successfuly", servletRequestrequest.getRequestURI());
        return new ResponseEntity<>(response, HttpStatus.CREATED);
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
