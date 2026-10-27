package com.vinstore.topup.security;

import com.vinstore.topup.entity.AppUser;
import com.vinstore.topup.repository.AppUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AdminBootstrap implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(AdminBootstrap.class);
    private static final int MIN_PASSWORD_LENGTH = 12;

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final String username;
    private final String password;

    public AdminBootstrap(AppUserRepository userRepository,
                          PasswordEncoder passwordEncoder,
                          @Value("${app.admin.username:}") String username,
                          @Value("${app.admin.password:}") String password) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.username = username;
        this.password = password;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (username.isBlank() || password.isBlank()) {
            log.warn("ADMIN_USERNAME atau ADMIN_PASSWORD belum diisi, akun admin tidak dibuat");
            return;
        }
        if (password.length() < MIN_PASSWORD_LENGTH) {
            log.warn("Password admin kurang dari {} karakter, akun admin tidak dibuat", MIN_PASSWORD_LENGTH);
            return;
        }
        if (userRepository.findByUsername(username).isPresent()) {
            log.info("Akun admin '{}' sudah ada", username);
            return;
        }
        userRepository.save(new AppUser(username, passwordEncoder.encode(password), "ADMIN"));
        log.info("Akun admin '{}' dibuat", username);
    }
}
