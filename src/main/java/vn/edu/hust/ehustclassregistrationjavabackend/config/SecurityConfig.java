package vn.edu.hust.ehustclassregistrationjavabackend.config;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.response.BaseResponse;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;

import java.lang.reflect.Array;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    //    final JwtFilter jwtFilter;
    final UserService userService;
    final Logger logger = LoggerFactory.getLogger(SecurityConfig.class);

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity security, JwtFilter jwtFilter) throws Exception {
        security.csrf(AbstractHttpConfigurer::disable)
                .cors(cors->cors.configurationSource(request ->{
                    CorsConfiguration conf = new CorsConfiguration();
                    conf.setAllowedMethods(List.of("GET","POST","PUT","PATCH","DELETE"));
                    conf.setAllowedOrigins(List.of("*"));
                    conf.setAllowedHeaders(List.of("*"));
                    return conf;
                }))
//                .authorizeHttpRequests(
//                        request -> request
//                                .requestMatchers("/admin/**").hasRole("ADMIN")
//                                .requestMatchers("/v3/api-docs/**").permitAll()
//                                .requestMatchers("/swagger-ui/**").permitAll()
//                                .requestMatchers("/api/v1/auth/**").permitAll()
//                                .requestMatchers("/student/**").hasAnyRole("STUDENT")
////                                .requestMatchers("/").permitAll()
//                                .anyRequest().permitAll()
//                )
                .addFilterAt(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer -> {
//                    httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint((request, response, authException) -> {
//                        authException.printStackTrace();
//                        response.getWriter().write(new BaseResponse.ErrorResponse(401, authException.getMessage()).toString());
//                        response.setStatus(401);
//                    });
                    httpSecurityExceptionHandlingConfigurer.accessDeniedHandler((request, response, accessDeniedException) -> {
                        response.getWriter().write(new BaseResponse.ErrorResponse(403, accessDeniedException.getMessage()).toString());
                        response.setStatus(403);
                    });
                })
////                .addFilterBefore()// TODO
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
        logger.info("Created Password Encoder");
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
