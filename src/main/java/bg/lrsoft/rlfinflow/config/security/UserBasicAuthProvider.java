package bg.lrsoft.rlfinflow.config.security;

import bg.lrsoft.rlfinflow.domain.model.FinFlowBasicAuthentication;
import bg.lrsoft.rlfinflow.service.FinFlowUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

@Slf4j
@RequiredArgsConstructor
class UserBasicAuthProvider implements AuthenticationProvider {

    private final FinFlowUserService basicUserDetailsService;

    public final PasswordEncoder passwordEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = basicUserDetailsService.loadUserByUsername((String) authentication.getPrincipal());
        if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect username or password!");
        }
        log.info("Authenticated user: {}", userDetails.getUsername());
        return FinFlowBasicAuthentication.authenticated(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities(), userDetails);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
