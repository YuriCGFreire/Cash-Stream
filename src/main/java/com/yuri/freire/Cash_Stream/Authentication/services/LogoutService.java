package com.yuri.freire.Cash_Stream.Authentication.services;

import com.yuri.freire.Cash_Stream.Authentication.entities.Token;
import com.yuri.freire.Cash_Stream.Authentication.entities.User;
import com.yuri.freire.Cash_Stream.Authentication.entities.repositories.TokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;


    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Cookie[] cookies = request.getCookies();
        String jwt = null;
        if(cookies != null){
            for(Cookie cookie: cookies){
                if("jwt".equals(cookie.getName())){
                    jwt = cookie.getValue();
                    break;
                }
            }
        }
        String userName = jwtService.extractUsername(jwt);
        User user = (User) userDetailsService.loadUserByUsername(userName);
        List<Token> storedValidTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if(!storedValidTokens.isEmpty()){
            storedValidTokens.forEach(token -> {
                token.setExpired(true);
                token.setRevoked(true);
            });
            tokenRepository.saveAll(storedValidTokens);
            SecurityContextHolder.clearContext();
        }
    }
}

