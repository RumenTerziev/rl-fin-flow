package bg.lrsoft.rlfinflow.domain.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

public class User implements Authentication {

    private final String name;

    private final UserDetails userDetails;

    private final Set<GrantedAuthority> authorities;

    private final boolean isAuthenticated;

    public User(String name, UserDetails userDetails, Set<GrantedAuthority> authorities, boolean isAuthenticated) {
        this.name = name;
        this.userDetails = userDetails;
        this.authorities = authorities;
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableSet(authorities);
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return userDetails;
    }

    @Override
    public Object getPrincipal() {
        return getName();
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
        User user = (User) another;
        return name.equals(user.name) && userDetails.equals(user.userDetails);
    }

    @Override
    public String toString() {
        return name.concat(userDetails.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String getName() {
        return name;
    }
}
