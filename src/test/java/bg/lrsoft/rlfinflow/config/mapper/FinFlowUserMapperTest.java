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
        String firstName = "John";
        String lastName = "Doe";
        String phoneNumber = "123456789";
        FinFlowUserRegisterDto userRegisterDto = new FinFlowUserRegisterDto(username, password, firstName, lastName,
                email, phoneNumber);

        //When
        FinFlowUser result = finFlowUserMapper.mapToEntity(userRegisterDto);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.getUsername()).isEqualTo(username);
        assertThat(result.getPassword()).isNull();
        assertThat(result.getFirstName()).isEqualTo(firstName);
        assertThat(result.getLastName()).isEqualTo(lastName);
        assertThat(result.getEmail()).isEqualTo(email);
        assertThat(result.getPhoneNumber()).isEqualTo(phoneNumber);
    }

    @Test
    void testMapToResponseDto_whenGivenUserEntity_shouldMapToDto() {
        //Given
        String username = "johnd";
        String password = "1234";
        String email = "johndoe@example.com";
        String firstName = "John";
        String lastName = "Doe";
        String phoneNumber = "123456789";
        FinFlowUser finFlowUser = new FinFlowUser(username, password, firstName, lastName, email, phoneNumber,
                new ArrayList<>());

        //When
        FinFlowUserResponseDto result = finFlowUserMapper.mapToResponseDto(finFlowUser);

        //Then
        assertThat(result).isNotNull();
        assertThat(result.username()).isEqualTo(username);
        assertThat(result.firstName()).isEqualTo(firstName);
        assertThat(result.lastName()).isEqualTo(lastName);
        assertThat(result.email()).isEqualTo(email);
        assertThat(result.phoneNumber()).isEqualTo(phoneNumber);
    }
}
