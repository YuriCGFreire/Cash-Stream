package com.yuri.freire.Cash_Stream.utils;

import jakarta.servlet.http.Cookie;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class CookieUtils {
    public static Cookie createCookie(String name, String value, int maxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        return cookie;
    }
}