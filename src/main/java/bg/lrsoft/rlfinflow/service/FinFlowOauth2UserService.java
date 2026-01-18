package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.config.mapper.FinFlowUserMapper;
import bg.lrsoft.rlfinflow.config.properties.AppSecurityProps;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.repository.FinFlowUserRepository;
import bg.lrsoft.rlfinflow.security.FinFlowOath2User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FinFlowOauth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {


    private final FinFlowUserRepository userRepository;
    private final FinFlowUserMapper userMapper;
    private final AppSecurityProps securityProperties;
    private final DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        List<String> adminEmails = List.of(securityProperties.getAdminUserEmail());

        OAuth2User oauth2User = delegate.loadUser(userRequest);
        String email = oauth2User.getAttribute("email");

        if (email == null) {
            throw new OAuth2AuthenticationException("Email missing");
        }

        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (adminEmails.contains(email)) {
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

        FinFlowOath2User principal = new FinFlowOath2User(oauth2User, authorities, email);
        persistOrUpdateUser(principal);
        return principal;
    }

    private void persistOrUpdateUser(FinFlowOath2User principal) {
        userRepository.findByEmail(principal.getEmail())
                .ifPresentOrElse(
                        user -> updateUser(user, principal),
                        () -> userRepository.save(userMapper.fromPrincipal(principal))
                );
    }

    private void updateUser(FinFlowUser user, FinFlowOath2User principal) {
        user.updateFromPrincipal(principal);
        userRepository.save(user);
    }
}