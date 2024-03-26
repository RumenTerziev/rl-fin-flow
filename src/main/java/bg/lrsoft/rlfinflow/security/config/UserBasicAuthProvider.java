package bg.lrsoft.rlfinflow.security.config;

import bg.lrsoft.rlfinflow.domain.model.FinFlowBasicAuthentication;
import bg.lrsoft.rlfinflow.service.BasicUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;

import java.util.HashSet;

@RequiredArgsConstructor
class UserBasicAuthProvider implements AuthenticationProvider {

    private final BasicUserDetailsService basicUserDetailsService;

    public final PasswordEncoder passwordEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = basicUserDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
        if (!passwordEncoder.matches(authentication.getCredentials().toString(), userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect username or password!");
        }
        return new FinFlowBasicAuthentication(authentication.getName(), userDetails, new HashSet<>(userDetails.getAuthorities()), true);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
