package bg.lrsoft.rlfinflow.repository;

import bg.lrsoft.rlfinflow.domain.model.Conversion;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Long> {

    long countByLoggedUsername(String loggedUsername);

    Page<Conversion> findAllByLoggedUsername(String loggedUsername, Pageable pageable);

    List<Conversion> findAllByLoggedUsername(String loggedUsername);

    void deleteByLoggedUsername(String loggedUsername);
}
