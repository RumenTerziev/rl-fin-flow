package bg.lrsoft.rlfinflow.security.config;

import bg.lrsoft.rlfinflow.domain.model.User;
import bg.lrsoft.rlfinflow.service.BasicUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;

@RequiredArgsConstructor
public class UserBasicAuthProvider implements AuthenticationProvider {

    private final BasicUserDetailsService basicUserDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UserDetails userDetails = basicUserDetailsService.loadUserByUsername(authentication.getPrincipal().toString());
        if (authentication.getCredentials().equals(userDetails.getPassword())) {
            return new User(authentication.getName(), userDetails, new HashSet<>(userDetails.getAuthorities()), true);
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
