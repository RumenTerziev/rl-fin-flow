package bg.lrsoft.rlfinflow.config.mapper;

import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserRegisterDto;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserResponseDto;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class FinFlowUserMapperTest {

    private final FinFlowUserMapper finFlowUserMapper = new FinFlowUserMapperImpl();

    @Test
    void testMapToEntity_whenGivenUserRegisterDto_shouldMapToEntity() {
        //Given
        String username = "johnd";
        String password = "1234";
        String email = "johndoe@example.com";
        FinFlowUserRegisterDto userRegisterDto = new FinFlowUserRegisterDto(username, password, email);

        //When
        FinFlowUser result = finFlowUserMapper.mapToEntity(userRegisterDto);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getPassword()).isNull();
        assertThat(result.getEmail()).isEqualTo(email);
    }

    @Test
    void testMapToResponseDto_whenGivenUserEntity_shouldMapToDto() {
        //Given
        String username = "johnd";
        String password = "1234";
        String email = "johndoe@example.com";
        FinFlowUser finFlowUser = new FinFlowUser(username, password, email, new ArrayList<>());

        //When
        FinFlowUserResponseDto result = finFlowUserMapper.mapToResponseDto(finFlowUser);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo(username);
        assertThat(result.email()).isEqualTo(email);
    }
}
