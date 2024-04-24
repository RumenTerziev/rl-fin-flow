package bg.lrsoft.rlfinflow.repository;

import bg.lrsoft.rlfinflow.domain.model.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Long> {

    List<Conversion> findAllByLoggedUsername(String loggedUsername);
}
