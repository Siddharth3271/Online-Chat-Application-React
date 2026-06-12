package com.siddh.chat_app_backend.config;

import com.siddh.chat_app_backend.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Configuration
public class ApplicationConfig {
    private final UserRepository userRepository;

    public ApplicationConfig(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    //why map?
    /*
    * Optional<DatabaseUser>
        |
        | map()
        v
      Optional<SpringSecurityUserDetails>
    * */
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> userRepository.findByEmail(username)
                .map(user -> User
                        .withUsername(user.getEmail())
                        .password(user.getPassword())
                        .build())
                .orElseThrow(()->new UsernameNotFoundException("User not found"));
    }

    //helps in verifying login email and password
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
}
