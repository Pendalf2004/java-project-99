package hexlet.code.app.controller;

import hexlet.code.app.model.Authentication;
import hexlet.code.app.utils.JWTUtils;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/login")
public class AuthController {

    @Autowired
    private JWTUtils utils;

    @Autowired
    private AuthenticationManager manager;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public String login(@Valid @RequestBody Authentication authPair) {
        UsernamePasswordAuthenticationToken authorize;
        authorize = new UsernamePasswordAuthenticationToken(
                authPair.getUsername(), authPair.getPassword());
        manager.authenticate(authorize);

        return utils.generateToken(authPair.getUsername());
    }
}
