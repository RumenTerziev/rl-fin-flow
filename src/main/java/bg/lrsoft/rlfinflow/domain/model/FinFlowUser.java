package bg.lrsoft.rlfinflow.domain.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static lombok.AccessLevel.PRIVATE;

@Getter
@NoArgsConstructor(access = PRIVATE)
public class FinFlowUser implements UserDetails {

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private List<GrantedAuthority> authorities;

    public FinFlowUser(String username, String password, String firstName, String lastName, String email, String phoneNumber, List<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.authorities = authorities;
    }

    public void updateAuthorities(String... authorities) {
        this.authorities = new ArrayList<>();
        this.authorities.addAll(AuthorityUtils.createAuthorityList(authorities));
    }

    public void updatePassword(String password) {
        if (password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank!");
        }
        this.password = password;
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
