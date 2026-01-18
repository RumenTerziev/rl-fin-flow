package bg.lrsoft.rlfinflow.security;

import bg.lrsoft.rlfinflow.service.FinFlowOauth2UserService;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final FinFlowUserService basicUserDetailsService;
    private final Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;
    private final Oauth2LoginFailureHandler oauth2LoginFailureHandler;
    private final FinFlowOauth2UserService finFlowOauth2UserService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, HttpSession httpSession) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .addFilterBefore(getFilter(), CsrfFilter.class)
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/auth/register").permitAll()
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/auth/google").permitAll()
                        .requestMatchers("/users/me").permitAll()
                        .requestMatchers("/converter/open-api-rates").hasAuthority("ROLE_USER")
                        .requestMatchers("/converter/bnb-rates").hasAuthority("ROLE_USER")
                        .requestMatchers("/converter/conversions/mine/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/converter/conversions").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/assistant/chat").hasAuthority("ROLE_USER")
                        .requestMatchers("/swagger-ui/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/v3/api-docs/**").hasAuthority("ROLE_USER")
                        .anyRequest().authenticated())
                .formLogin(formLogin -> formLogin.loginProcessingUrl("/auth/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler())
                        .failureHandler(loginFailureHandler())
                        .permitAll())
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo
                                .userService(finFlowOauth2UserService)
                        )
                        .successHandler(oauth2LoginSuccessHandler)
                        .failureHandler(oauth2LoginFailureHandler))
                .logout(logout -> logout.logoutUrl("/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(SC_OK))
                        .invalidateHttpSession(true)
                        .permitAll())
                .build();
    }

    @Bean
    public UserBasicAuthProvider userBasicAuthProvider() {
        return new UserBasicAuthProvider(basicUserDetailsService);
    }

    @Bean
    public AuthenticationSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler();
    }

    @Bean
    public AuthenticationFailureHandler loginFailureHandler() {
        return new LoginFailureHandler();
    }

    private Filter getFilter() {
        return (servletRequest, servletResponse, filterChain) -> {
            if (servletRequest instanceof HttpServletRequest request) {
                log.info("REQUEST -> {}", request.getRequestURI());
            }
            filterChain.doFilter(servletRequest, servletResponse);
        };
    }
}
