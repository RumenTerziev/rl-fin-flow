package bg.lrsoft.rlfinflow.domain.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class FinFlowBasicAuthentication implements Authentication {

    private Object principal;

    private final Object credentials;

    private final List<GrantedAuthority> authorities;

    private boolean isAuthenticated;

    private UserDetails userDetails;


    public FinFlowBasicAuthentication(Object credentials, List<GrantedAuthority> authorities) {
        this.credentials = credentials;
        this.authorities = authorities;
    }

    public FinFlowBasicAuthentication(Object principal, Object credentials, List<GrantedAuthority> authorities, UserDetails userDetails) {
        this.principal = principal;
        this.credentials = credentials;
        this.authorities = authorities;
        this.isAuthenticated = this.credentials == null;
        this.userDetails = userDetails;
    }

    public static FinFlowBasicAuthentication unauthenticated(String password) {
        return new FinFlowBasicAuthentication(password, Collections.emptyList());
    }

    public static FinFlowBasicAuthentication authenticated(Object principal, String password, Collection<? extends GrantedAuthority> authorities, UserDetails userDetails) {
        return new FinFlowBasicAuthentication(principal, password, mapAuthorities(authorities), userDetails);
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
        return userDetails;
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
        return principal == null ? "" : principal.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(principal);
    }

    @Override
    public String getName() {
        return userDetails.getUsername();
    }

    private static List<GrantedAuthority> mapAuthorities(Collection<? extends GrantedAuthority> authorities) {
        return AuthorityUtils.createAuthorityList(authorities.stream().map(GrantedAuthority::toString).toList());
    }
}
