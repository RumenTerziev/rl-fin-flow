package bg.lrsoft.rlfinflow.security;

import bg.lrsoft.rlfinflow.config.properties.CorsProperties;
import bg.lrsoft.rlfinflow.service.FinFlowOauth2UserService;
import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.util.matcher.AnyRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static jakarta.servlet.http.HttpServletResponse.SC_OK;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final Oauth2LoginSuccessHandler oauth2LoginSuccessHandler;
    private final Oauth2LoginFailureHandler oauth2LoginFailureHandler;
    private final FinFlowOauth2UserService finFlowOauth2UserService;
    private final CorsProperties corsProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .addFilterBefore(getFilter(), CsrfFilter.class)
                .exceptionHandling(eh -> eh.defaultAuthenticationEntryPointFor(
                        new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        AnyRequestMatcher.INSTANCE))
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/home").permitAll()
                        .requestMatchers("/auth/google").permitAll()
                        .requestMatchers("/privacy/consent").permitAll()
                        .requestMatchers("/privacy/policy-version").permitAll()
                        .requestMatchers("/converter/open-api-rates").permitAll()
                        .requestMatchers("/converter/bnb-rates").permitAll()
                        .requestMatchers("/converter/conversions/mine/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/converter/conversions").hasAuthority("ROLE_ADMIN")
                        .requestMatchers("/assistant/chat").hasAuthority("ROLE_USER")
                        .requestMatchers("/privacy/profile/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/users/profile").hasAuthority("ROLE_USER")
                        .requestMatchers("/swagger-ui/**").hasAuthority("ROLE_USER")
                        .requestMatchers("/v3/api-docs/**").hasAuthority("ROLE_USER")
                        .anyRequest().authenticated())
                .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo
                                .userService(finFlowOauth2UserService)
                        )
                        .successHandler(oauth2LoginSuccessHandler)
                        .failureHandler(oauth2LoginFailureHandler))
                .logout(logout -> logout.logoutUrl("/auth/logout")
                        .logoutSuccessHandler((request, response, authentication) -> response.setStatus(SC_OK))
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll())
                .build();
    }

    /**
     * Restrictive CORS: only the configured SPA origins, only the verbs we use, credentials
     * allowed so the JSESSIONID cookie can travel with cross-origin requests like
     * {@code POST /privacy/consent} (which must precede the OAuth redirect).
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(corsProperties.getAllowedOrigins());
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        config.setAllowedHeaders(List.of("Content-Type", "Accept", "X-Requested-With"));
        config.setAllowCredentials(true);
        config.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    /**
     * Lightweight access log. Only the request URI is logged — never query strings, headers or
     * bodies, to avoid leaking tokens or PII into log aggregators.
     */
    private Filter getFilter() {
        return (servletRequest, servletResponse, filterChain) -> {
            if (servletRequest instanceof HttpServletRequest request) {
                log.info("REQUEST -> {}", request.getRequestURI());
            }
            filterChain.doFilter(servletRequest, servletResponse);
        };
    }
}
