package bg.lrsoft.rlfinflow.repository;

import bg.lrsoft.rlfinflow.domain.model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, UUID> {

    Optional<Manager> findByUsername(String username);
}
