package bt.edu.gcit.usermicroservice.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import java.util.Arrays;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class FloraMartSecurityConfig {

    public FloraMartSecurityConfig() {
        System.out.println("ShopmeSecurityConfig created");
    }

    @Autowired
    private JwtRequestFilter jwtRequestFilter;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public AuthenticationManager customAuthenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList(authProvider()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeHttpRequests(configurer ->
                configurer
                    .requestMatchers(HttpMethod.POST, "/api/auth/login").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/users").permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/flowers/add").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/flowers/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/flowers/getAllflowers").permitAll()
                    .requestMatchers(HttpMethod.GET, "/api/users/checkDuplicateEmail").hasAuthority("Admin")
                    .requestMatchers(HttpMethod.PUT, "/api/users/{id}").hasAuthority("Admin")
                    .requestMatchers(HttpMethod.DELETE, "/api/users/{id}").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/api/users/{id}/enabled").permitAll()
                    .requestMatchers(HttpMethod.PUT, "/flowers/**").permitAll()
            )
            .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
