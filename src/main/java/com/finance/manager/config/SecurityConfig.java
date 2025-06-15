package com.finance.manager.config;

import com.finance.manager.security.JwtAuthenticationFilter;
import com.finance.manager.security.JwtBlacklistService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

/**
 * Spring Security configuration class.
 * Configures authentication, authorization, and security filters for the application.
 *
 * @author Finance Manager Team
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    public final JwtAuthenticationFilter jwtAuthenticationFilter;
    public final JwtBlacklistService jwtBlacklistService;
    public final UserDetailsService userDetailsService;

    /**
     * Constructs a new SecurityConfig with required dependencies.
     *
     * @param jwtAuthenticationFilter Filter for JWT authentication
     * @param jwtBlacklistService Service for managing blacklisted tokens
     * @param userDetailsService Service for loading user details
     */
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          JwtBlacklistService jwtBlacklistService,
                          UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.jwtBlacklistService = jwtBlacklistService;
        this.userDetailsService = userDetailsService;
    }

    /**
     * Creates a BCrypt password encoder bean.
     *
     * @return PasswordEncoder instance using BCrypt
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates an AuthenticationManager bean.
     *
     * @param config Authentication configuration
     * @return AuthenticationManager instance
     * @throws Exception if authentication manager creation fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures the security filter chain with JWT authentication and authorization rules.
     *
     * @param http HttpSecurity instance to configure
     * @return Configured SecurityFilterChain
     * @throws Exception if security configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/register", "/auth/login", "/auth/logout").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authenticationProvider(authenticationProvider())
            .formLogin(login -> login.disable())
            .httpBasic(httpBasic -> httpBasic.disable())
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Creates a DaoAuthenticationProvider bean for user authentication.
     *
     * @return Configured DaoAuthenticationProvider
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * Configures CORS settings for the application.
     *
     * @return CorsConfigurationSource with allowed origins, methods, and headers
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
