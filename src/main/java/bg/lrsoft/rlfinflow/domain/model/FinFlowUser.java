package bg.lrsoft.rlfinflow.domain.model;

import lombok.Getter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.List;

@Getter
@ToString
public class FinFlowUser implements UserDetails {

    private final String username;

    @ToString.Exclude
    private final String password;

    private final String firstName;

    private final String lastName;

    private final String email;

    private final String phoneNumber;

    private final List<GrantedAuthority> authorities;

    public FinFlowUser(String username, String password, String firstName, String lastName, String email, String phoneNumber, List<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
    }

    @Override
    public List<GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableList(authorities);
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
