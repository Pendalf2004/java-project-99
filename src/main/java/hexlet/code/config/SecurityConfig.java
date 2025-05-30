package hexlet.code.config;

import hexlet.code.services.UserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
    @Autowired
    private JwtDecoder jwtDecoder;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userService;

    public SecurityConfig(JwtDecoder jwtDecoder,
                          PasswordEncoder passwordEncoder,
                          UserDetailsService userService) {
        this.jwtDecoder = jwtDecoder;
        this.passwordEncoder = passwordEncoder;
        this.userService = userService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, HandlerMappingIntrospector introspector)
            throws Exception {
        var mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(mvcMatcherBuilder.pattern("/api/login")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/")).permitAll()
                        .requestMatchers("/assets/**").permitAll()

                        .requestMatchers(mvcMatcherBuilder.pattern("/exception")).permitAll()
                        .requestMatchers(mvcMatcherBuilder.pattern("/index.html")).permitAll()

                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.GET, "/welcome")).permitAll()

                        .requestMatchers(mvcMatcherBuilder.pattern(HttpMethod.POST, "/api/users")).permitAll()

                        .requestMatchers("/doc_ui.html/**").permitAll()
                        .requestMatchers("/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer((rs) ->
                        rs.jwt((jwt) -> jwt.decoder(jwtDecoder)))
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .build();
    }

    @Bean
    public AuthenticationProvider daoAuthProvider(AuthenticationManagerBuilder auth) {
        var provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}
