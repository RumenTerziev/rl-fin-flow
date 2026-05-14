package bg.lrsoft.rlfinflow.repository;

import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinFlowUserRepository extends JpaRepository<FinFlowUser, Long> {

    Optional<FinFlowUser> findByEmail(String email);

    void deleteByEmail(String email);
}
