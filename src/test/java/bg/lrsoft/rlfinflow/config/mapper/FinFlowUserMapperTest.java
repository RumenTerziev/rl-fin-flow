package bg.lrsoft.rlfinflow.config.mapper;

import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserResponseDto;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

class FinFlowUserMapperTest {

    private final FinFlowUserMapper finFlowUserMapper = new FinFlowUserMapperImpl();

    @Test
    void mapToResponseDto_returnsUsernameEmailAndPicture() {
        FinFlowUser finFlowUser = new FinFlowUser(
                "johnd",
                "johndoe@example.com",
                "https://example.com/pic.png",
                Set.of("ROLE_USER"));

        FinFlowUserResponseDto result = finFlowUserMapper.mapToResponseDto(finFlowUser);

        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo("johnd");
        assertThat(result.email()).isEqualTo("johndoe@example.com");
        assertThat(result.pictureUrl()).isEqualTo("https://example.com/pic.png");
    }
}
