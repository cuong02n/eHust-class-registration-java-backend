package vn.edu.hust.ehustclassregistrationjavabackend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    final JwtFilter jwtFilter;
    final UserService userService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security) throws Exception {
        security.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(
                        request -> request
                                .requestMatchers("/**").permitAll()
                                .requestMatchers("/admin/**").hasAnyAuthority("ADMIN"))
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
//                .addFilterBefore()// TODO
        ;
        return security.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userService);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
