package bg.lrsoft.rlfinflow.domain.model;

import bg.lrsoft.rlfinflow.security.FinFlowOath2User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FinFlowUser implements UserDetails {

    private String username;

    private String password;

    private String email;

    private List<GrantedAuthority> authorities;

    public FinFlowUser(String username, String password, String email, List<GrantedAuthority> authorities) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.authorities = authorities;
    }

    public void updateAuthorities(String... authorities) {
        this.authorities = new ArrayList<>();
        this.authorities.addAll(AuthorityUtils.createAuthorityList(authorities));
    }

    public void updatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be blank!");
        }
        this.password = password;
    }

    public void updateEmail(String email) {
        if (password == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank!");
        }
        this.email = email;
    }

    public void updateFromPrincipal(FinFlowOath2User principal) {
        this.username = principal.getAttribute("name");

        authorities.clear();
        principal.getAuthorities()
                .forEach(a -> authorities.add(new SimpleGrantedAuthority(a.getAuthority())));
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
