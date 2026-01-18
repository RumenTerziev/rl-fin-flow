package bg.lrsoft.rlfinflow.security;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class FinFlowOath2User implements OAuth2User {

    private final OAuth2User delegate;
    private final Set<SimpleGrantedAuthority> authorities;

    @Getter
    private final String email;

    public FinFlowOath2User(OAuth2User delegate, Set<SimpleGrantedAuthority> authorities, String email) {
        this.delegate = delegate;
        this.authorities = authorities;
        this.email = email;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return email;
    }
}