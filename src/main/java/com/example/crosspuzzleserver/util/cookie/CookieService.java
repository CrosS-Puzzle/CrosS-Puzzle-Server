package com.example.crosspuzzleserver.util.cookie;

import java.time.Duration;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CookieService {

    public String getCookie() {
        String uuid = UUID.randomUUID().toString();
        ResponseCookie responseCookie = ResponseCookie
                .from("userCookie", uuid)
                .domain("api.cross-word.online")
//                .domain("localhost")
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(14))
                .sameSite("Strict")
                .build();
        return responseCookie.toString();
    }


}
