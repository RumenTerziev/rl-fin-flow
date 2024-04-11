package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.config.mapper.FinFlowUserMapper;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserImportDto;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.repository.FinFlowUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FinFlowUserService implements UserDetailsService {

    @Value("${first.fin-flow.user.username}")
    private String firstUserUsername;

    @Value("${first.fin-flow.user.password}")
    private String firstUserPassword;

    @Value("${first.fin-flow.user.first-name}")
    private String firstUserFirstName;

    @Value("${first.fin-flow.user.last-name}")
    private String firstUserLastName;

    @Value("${first.fin-flow.user.email}")
    private String firstUserEmail;

    @Value("${first.fin-flow.user.phone-number}")
    private String firstUserPhoneNumber;

    @Value("${first.fin-flow.user.authorities}")
    private List<String> firstUserAuthorities;

    private final FinFlowUserRepository finFlowUserRepository;

    private final PasswordEncoder passwordEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    private final FinFlowUserMapper mapper;

    public void registerUser(FinFlowUserImportDto finFlowUserImportDto) {
        FinFlowUser finFlowUser = mapper.mapToEntity(finFlowUserImportDto);
        finFlowUser.updateAuthorities("ROLE_USER");
        finFlowUser.updatePassword(passwordEncoder.encode(finFlowUserImportDto.password()));
        finFlowUserRepository.add(finFlowUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return finFlowUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username %s not found!".formatted(username)));
    }

    public void initialize() {
        FinFlowUser finFlowUser = new FinFlowUser(
                firstUserUsername,
                passwordEncoder.encode(firstUserPassword),
                firstUserFirstName,
                firstUserLastName,
                firstUserEmail,
                firstUserPhoneNumber,
                AuthorityUtils.createAuthorityList(firstUserAuthorities));
        finFlowUserRepository.add(finFlowUser);
        log.info("Added first user to db!!! {}", finFlowUser);
    }
}