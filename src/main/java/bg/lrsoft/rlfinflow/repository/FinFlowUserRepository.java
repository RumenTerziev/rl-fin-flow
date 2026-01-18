package bg.lrsoft.rlfinflow.repository;

import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class FinFlowUserRepository {

    private final Map<String, FinFlowUser> finFlowUsers = new HashMap<>();

    public Optional<FinFlowUser> findByUsername(String username) {
        if (finFlowUsers.containsKey(username)) {
            return Optional.of(finFlowUsers.get(username));
        }
        return Optional.empty();
    }

    public boolean existsByUsername(String username) {
        return finFlowUsers.containsKey(username);
    }

    public List<FinFlowUser> findAll() {
        return List.copyOf(finFlowUsers.values());
    }

    public FinFlowUser add(FinFlowUser finFlowUser) {
        finFlowUsers.put(finFlowUser.getUsername(), finFlowUser);
        return finFlowUser;
    }

    public void remove(String username) {
        finFlowUsers.remove(username);
    }

    public FinFlowUser save(FinFlowUser finFlowUser) {
        finFlowUsers.put(finFlowUser.getUsername(), finFlowUser);
        return finFlowUsers.get(finFlowUser.getUsername());
    }

    public Optional<FinFlowUser> findByEmail(String email) {
        return finFlowUsers.values()
                .stream()
                .filter(user -> email.equals(user.getEmail()))
                .findFirst();
    }
}
