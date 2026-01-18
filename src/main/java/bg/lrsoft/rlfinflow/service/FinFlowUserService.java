package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.config.mapper.FinFlowUserMapper;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserRegisterDto;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserUpdateRequestDto;
import bg.lrsoft.rlfinflow.domain.exception.NoUserLoggedInException;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.repository.FinFlowUserRepository;
import bg.lrsoft.rlfinflow.security.FinFlowOath2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinFlowUserService implements UserDetailsService {

    @Value("${first.fin-flow.user.username}")
    private String firstUserUsername;

    @Value("${first.fin-flow.user.password}")
    private String firstUserPassword;


    @Value("${first.fin-flow.user.email}")
    private String firstUserEmail;

    @Value("${first.fin-flow.user.authorities}")
    private List<String> firstUserAuthorities;

    private final FinFlowUserRepository finFlowUserRepository;

    private final PasswordEncoder passwordEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    private final FinFlowUserMapper mapper;

    public FinFlowUserResponseDto registerUser(FinFlowUserRegisterDto finFlowUserImportDto) {
        FinFlowUser finFlowUser = mapper.mapToEntity(finFlowUserImportDto);
        finFlowUser.updateAuthorities("ROLE_USER");
        finFlowUser.updatePassword(passwordEncoder.encode(finFlowUserImportDto.password()));
        FinFlowUser savedUser = finFlowUserRepository.add(finFlowUser);
        return mapper.mapToResponseDto(savedUser);
    }

    public FinFlowUserResponseDto getMyProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        if (principal instanceof FinFlowUser finFlowUser) {
            return mapper.mapToResponseDto(finFlowUser);
        } else if (principal instanceof FinFlowOath2User oAuth2User) {
            return mapper.mapToResponseDto(oAuth2User);
        }
        throw new NoUserLoggedInException();
    }

    public void initialize() {
        FinFlowUser finFlowUser = new FinFlowUser(
                firstUserUsername,
                passwordEncoder.encode(firstUserPassword),
                firstUserEmail,
                AuthorityUtils.createAuthorityList(firstUserAuthorities));
        finFlowUserRepository.add(finFlowUser);
        log.info("Added first user to db!!! {}", finFlowUser);
    }

    public FinFlowUser getAuthenticatedUser() {
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .map(Authentication::getPrincipal)
                .filter(FinFlowUser.class::isInstance)
                .map(FinFlowUser.class::cast)
                .orElseThrow(NoUserLoggedInException::new);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return finFlowUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username %s not found!".formatted(username)));
    }

    public void updateProfile(FinFlowUserUpdateRequestDto finFlowUserUpdateRequestDto) {
        FinFlowUser authenticatedUser = getAuthenticatedUser();
        authenticatedUser.updatePassword(this.passwordEncoder.encode(finFlowUserUpdateRequestDto.password()));
        authenticatedUser.updateEmail(finFlowUserUpdateRequestDto.email());
        finFlowUserRepository.save(authenticatedUser);
    }

    public FinFlowUser saveLoggedUser(@AuthenticationPrincipal FinFlowOath2User principal) {
        if (principal == null) {
            log.debug("No authenticated user to save, returning null");
            return null;
        }

        log.info("Persisting user: {}", principal.getEmail());

        return finFlowUserRepository.findByEmail(principal.getEmail())
                .orElseGet(() -> finFlowUserRepository.save(mapper.fromPrincipal(principal)));
    }
}
