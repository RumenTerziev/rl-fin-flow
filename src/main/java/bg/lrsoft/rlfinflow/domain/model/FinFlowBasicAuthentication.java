package bg.lrsoft.rlfinflow.domain.model;

import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FinFlowBasicAuthentication implements Authentication {

    private final Object principal;

    private final Object credentials;

    private final List<GrantedAuthority> authorities;

    private final boolean isAuthenticated;

    private final UserDetails userDetails;

    public FinFlowBasicAuthentication(Object principal, Object credentials, List<GrantedAuthority> authorities, UserDetails userDetails) {
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities;
        this.isAuthenticated = this.credentials == null;
        this.userDetails = userDetails;
    }

    public static FinFlowBasicAuthentication unauthenticated(Object principal, Object credentials, UserDetails userDetails) {
        return new FinFlowBasicAuthentication(principal, credentials, Collections.emptyList(), userDetails);
    }

    public static FinFlowBasicAuthentication authenticated(Object principal, Object credentials, List<GrantedAuthority> authorities, UserDetails userDetails) {
        return new FinFlowBasicAuthentication(principal, credentials, authorities, userDetails);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableList(authorities);
    }

    @Override
    public Object getCredentials() {
        return credentials;
    }

    @Override
    public Object getDetails() {
        return userDetails;
    }

    @Override
    public Object getPrincipal() {
        return userDetails.toString();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("This operation is not supported!");
    }

    @Override
    public boolean equals(Object another) {
        if (this == another) return true;
        if (another == null || getClass() != another.getClass()) return false;
        FinFlowBasicAuthentication user = (FinFlowBasicAuthentication) another;
        return userDetails.equals(user.userDetails);
    }

    @Override
    public String toString() {
        return principal.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(principal);
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }
}
