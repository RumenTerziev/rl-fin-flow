package bg.lrsoft.rlfinflow.repository;

import bg.lrsoft.rlfinflow.domain.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findTop20ByUsernameOrderByTimestampDesc(String username);
}
