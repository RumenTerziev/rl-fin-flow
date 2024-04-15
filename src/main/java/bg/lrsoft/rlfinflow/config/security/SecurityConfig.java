package bg.lrsoft.rlfinflow.config.security;

import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.util.Collections;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final FinFlowUserService basicUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/users/register").permitAll()
                        .requestMatchers("/users/authenticate").permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/users/profile").hasAuthority("ROLE_USER")
                        .requestMatchers("/finances").hasAuthority("ROLE_USER")
                        .requestMatchers("/finances/converter").hasAuthority("ROLE_USER")
                        .requestMatchers("/swagger-ui/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/v3/api-docs/**").hasAuthority("ROLE_USER")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(IF_REQUIRED)
                        .maximumSessions(2)
                        .expiredUrl("/login?expired"))
                .authenticationManager(authManager(basicUserDetailsService))
                .formLogin(formLogin -> formLogin.loginProcessingUrl("/users/authenticate")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler())
                        .failureHandler(loginFailureHandler()))
                .logout(logout -> logout.logoutUrl("/users/logout")
                        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(SC_OK))
                        .invalidateHttpSession(true)
                        .permitAll())
                .build();
    }

    @Bean
    public AuthenticationManager authManager(FinFlowUserService basicUserDetailsService) {
        return new ProviderManager(Collections.singletonList(new UserBasicAuthProvider(basicUserDetailsService)));
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }
}
