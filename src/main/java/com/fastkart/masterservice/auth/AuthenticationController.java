package com.fastkart.masterservice.auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Locale;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService service;

    private LinkedHashMap<String, Object> responseMap = new LinkedHashMap<>();

    @PostMapping("/signup")
    public ResponseEntity<Object> register(
            @RequestBody RegisterRequest request
    ) {
        responseMap.clear();
        responseMap.put("successMessage","signup successful new account for "+request.getRole().toString().toLowerCase(Locale.ROOT)+" created");
        responseMap.put("accessToken", service.register(request).getAccessToken());
        responseMap.put("statusCode", HttpStatus.CREATED.value());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseMap);
    }
    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest request
    ) {
        return ResponseEntity.ok(service.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        service.refreshToken(request, response);
    }

}
