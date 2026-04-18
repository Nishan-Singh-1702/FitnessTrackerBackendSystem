package com.Fitness.security;

import com.Fitness.model.AppRole;
import com.Fitness.model.Role;
import com.Fitness.model.User;
import com.Fitness.repository.RoleRepository;
import com.Fitness.repository.UserRepository;
import com.Fitness.security.jwt.AuthEntryPointJwt;
import com.Fitness.security.jwt.AuthTokenFilter;
import com.Fitness.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashSet;
import java.util.Set;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
//@EnableMethodSecurity
public class WebSecurityConfig {

    @Value("${spring.app.adminEmail}")
    private String adminEmail;
    @Value("${spring.app.adminPassword}")
    private String adminPassword;
    @Value("${spring.app.adminFirstName}")
    private String adminFirstName;
    @Value("${spring.app.adminLastName}")
    private String adminLastName;

    @Value("${spring.app.userEmail}")
    private String userEmail;
    @Value("${spring.app.userPassword}")
    private String userPassword;
    @Value("${spring.app.userFirstName}")
    private String userFirstName;
    @Value("${spring.app.userLastName}")
    private String userLastName;

    @Autowired
    UserDetailsServiceImpl userDetailsService;
    @Autowired
    AuthEntryPointJwt unauthorizedHandler;

    @Bean
    public AuthTokenFilter authenticatedJwtTokenFilter(){
        return new AuthTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }


    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.headers(header->header.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception->exception.authenticationEntryPoint(unauthorizedHandler))
                .authorizeHttpRequests((requests) ->
                        requests.requestMatchers("/h2-console/**").permitAll()
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/public/**").permitAll()
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/test/**").permitAll()
                                .requestMatchers("/images/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .requestMatchers("/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated());
        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticatedJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){ // user to completely bypass the spring security filters at a global level
        return (web -> web.ignoring().requestMatchers("/v2/api-docs",
                    "/configuration/ui",
                    "/swagger-resources/**",
                    "/configuration/security",
                    "/swagger-ui.html",
                    "/webjars/**"));
    }


    @Bean
    public CommandLineRunner initData(UserRepository userRepository, PasswordEncoder passwordEncoder, RoleRepository roleRepository) {
        return args -> {

            Role userRole = roleRepository.findByRole(AppRole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Role USER not found"));

            Role adminRole = roleRepository.findByRole(AppRole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Role ADMIN not found"));

            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = new User(adminFirstName,adminLastName,adminEmail,passwordEncoder.encode(adminPassword));
                admin.setRoles(Set.of(adminRole,userRole));
                userRepository.save(admin);
            }
            if (!userRepository.existsByEmail(userEmail)) {
                User user = new User(userFirstName,userLastName,userEmail,passwordEncoder.encode(userPassword));
                user.setRoles(Set.of(userRole));
                userRepository.save(user);
            }

        };
    }
}
