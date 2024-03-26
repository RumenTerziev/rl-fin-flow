package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.repository.FinFlowUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserDetailsService implements UserDetailsService {

    private final FinFlowUserRepository managerRepository;

    private final PasswordEncoder passwordEncoder = Pbkdf2PasswordEncoder.defaultsForSpringSecurity_v5_8();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return managerRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username %s not found!".formatted(username)));
    }

    public void initialize() {
        String firstUserUsername = "bryan";
        String firstUserPassword = "4321a";
        String firstUserFirstName = "John";
        String firstLastName = "Doe";
        String firstUserEmail = "bryansk@fin.com";
        String firstUserPhoneNumber = "0888888881";
        managerRepository.add(new FinFlowUser(firstUserUsername, passwordEncoder.encode(firstUserPassword),
                firstUserFirstName, firstLastName, firstUserEmail, firstUserPhoneNumber));
    }
}
