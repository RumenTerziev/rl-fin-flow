package bg.lrsoft.rlfinflow.repository;

import bg.lrsoft.rlfinflow.domain.model.ConsentRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConsentRepository extends JpaRepository<ConsentRecord, Long> {

    Optional<ConsentRecord> findTopByEmailAndPolicyVersionOrderByAcceptedAtDesc(String email, String policyVersion);

    void deleteByEmail(String email);
}
