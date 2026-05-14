package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.config.mapper.FinFlowUserMapper;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserResponseDto;
import bg.lrsoft.rlfinflow.domain.exception.NoUserLoggedInException;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.repository.FinFlowUserRepository;
import bg.lrsoft.rlfinflow.security.FinFlowOath2User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FinFlowUserServiceTest {

    private final FinFlowUserMapper mapper = mock(FinFlowUserMapper.class);
    private final FinFlowUserRepository repository = mock(FinFlowUserRepository.class);
    private final FinFlowUserService service = new FinFlowUserService(repository, mapper);

    @AfterEach
    void clearContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getMyProfile_returnsMappedResponseForAuthenticatedOAuthUser() {
        String email = "tommy@example.com";
        FinFlowUser user = new FinFlowUser("Tommy", email, "https://pic", Set.of("ROLE_USER"));
        authenticateAs(email);
        when(repository.findByEmail(email)).thenReturn(Optional.of(user));
        when(mapper.mapToResponseDto(user))
                .thenReturn(new FinFlowUserResponseDto("Tommy", email, "https://pic"));

        FinFlowUserResponseDto response = service.getMyProfile();

        assertThat(response.email()).isEqualTo(email);
        assertThat(response.username()).isEqualTo("Tommy");
    }

    @Test
    void getAuthenticatedFinFlowUser_throwsWhenAnonymous() {
        assertThatThrownBy(service::getAuthenticatedFinFlowUser)
                .isInstanceOf(NoUserLoggedInException.class);
    }

    @Test
    void saveLoggedUser_persistsNewUserOnFirstLogin() {
        String email = "new@example.com";
        FinFlowOath2User principal = principal(email);
        FinFlowUser fresh = new FinFlowUser("New", email, null, new HashSet<>());

        when(repository.findByEmail(email)).thenReturn(Optional.empty());
        when(mapper.fromPrincipal(principal)).thenReturn(fresh);
        when(repository.save(fresh)).thenReturn(fresh);

        FinFlowUser saved = service.saveLoggedUser(principal);

        assertThat(saved).isSameAs(fresh);
        verify(repository).save(fresh);
    }

    @Test
    void saveLoggedUser_returnsNullForNullPrincipal() {
        assertThat(service.saveLoggedUser(null)).isNull();
    }

    private void authenticateAs(String email) {
        FinFlowOath2User principal = principal(email);
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(principal, "n/a", principal.getAuthorities()));
    }

    private FinFlowOath2User principal(String email) {
        OAuth2UserStub stub = new OAuth2UserStub(email);
        return new FinFlowOath2User(stub, Set.of(new SimpleGrantedAuthority("ROLE_USER")), email);
    }

    /** Minimal OAuth2User stand-in to avoid coupling tests to Spring's nested builder. */
    private record OAuth2UserStub(String email) implements OAuth2User {
        @Override public Map<String, Object> getAttributes() {
            return Map.of("email", email, "name", "Test", "picture", "https://pic");
        }
        @Override public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of();
        }
        @Override public String getName() { return email; }
    }
}


