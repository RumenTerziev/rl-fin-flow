package bg.lrsoft.rlfinflow.security;

import bg.lrsoft.rlfinflow.config.properties.AppSecurityProps;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CsrfFilter;

import java.util.HashSet;
import java.util.Set;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;
import static org.springframework.security.config.http.SessionCreationPolicy.IF_REQUIRED;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final FinFlowUserService basicUserDetailsService;
    private final AppSecurityProps appSecurityProps;
    private final Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;
    private final Oauth2LoginFailureHandler oauth2LoginFailureHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

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
                        .requestMatchers("/users/me").hasAuthority("ROLE_USER")
                        .requestMatchers("/converter/open-api-rates").hasAuthority("ROLE_USER")
                        .requestMatchers("/converter/bnb-rates").hasAuthority("ROLE_USER")
                        .requestMatchers("/converter/conversions/mine/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/converter/conversions").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/assistant/chat").hasAuthority("ROLE_USER")
                        .requestMatchers("/swagger-ui/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/v3/api-docs/**").hasAuthority("ROLE_USER")
                        .anyRequest().authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(IF_REQUIRED)
                        .maximumSessions(2)
                        .expiredUrl("/login?expired"))
                .formLogin(formLogin -> formLogin.loginProcessingUrl("/auth/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(loginSuccessHandler())
                        .failureHandler(loginFailureHandler())
                        .permitAll())
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo
                                .userService(customOAuth2UserService())
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

    @Bean
    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
        return new OAuth2UserService<>() {
            private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

            @Override
            public OAuth2User loadUser(OAuth2UserRequest userRequest) {
                OAuth2User user = delegate.loadUser(userRequest);

                Set<SimpleGrantedAuthority> authorities = new HashSet<>();
                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

                String email = user.getAttribute("email");

                if (appSecurityProps.getAdminUserEmail().equals(email)) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                }

                return new DefaultOAuth2User(authorities, user.getAttributes(), "email");
            }
        };
    }
}
