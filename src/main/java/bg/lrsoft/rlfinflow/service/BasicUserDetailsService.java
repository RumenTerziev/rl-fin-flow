package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.repository.FinFlowUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserDetailsService implements UserDetailsService {

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

    private final FinFlowUserRepository finFlowUserRepository;

    private final PasswordEncoder passwordEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

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
                firstUserPhoneNumber);
        finFlowUserRepository.add(finFlowUser);
        System.out.println("Successfully registered user" + finFlowUser);
    }
}
