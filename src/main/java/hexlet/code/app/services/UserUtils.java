package hexlet.code.app.services;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserUtils {

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public UserUtils(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public String hash(String email, String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public boolean rehash(String providedPass, String encodedPassword) {
        return passwordEncoder.matches(providedPass, encodedPassword);
    }

}