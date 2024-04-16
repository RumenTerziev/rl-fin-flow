package bg.lrsoft.rlfinflow.service;

import bg.lrsoft.rlfinflow.config.mapper.FinFlowUserMapper;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserRegisterDto;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserResponseDto;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.repository.FinFlowUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.AuthorityUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class FinFlowUserServiceTest {

    private final FinFlowUserMapper finflowUserMapper = mock(FinFlowUserMapper.class);

    private final FinFlowUserRepository finFlowUserRepository = mock(FinFlowUserRepository.class);

    private final FinFlowUserService finFlowUserService = new FinFlowUserService(finFlowUserRepository, finflowUserMapper);

    @Test
    void testRegisterUser_whenValidDataIsProvided() {
        //Given
        String username = "test";
        String password = "test";
        String firstName = "test";
        String lastName = "test";
        String email = "test@test.com";
        String phoneNumber = "test";
        FinFlowUserRegisterDto finFlowUserRegisterDto = new FinFlowUserRegisterDto(
                username,
                password,
                firstName,
                lastName,
                email,
                phoneNumber);

        FinFlowUser finFlowUser = new FinFlowUser(
                username,
                password,
                firstName,
                lastName,
                email,
                phoneNumber,
                AuthorityUtils.createAuthorityList("ROLE_USER"));

        when(finflowUserMapper.mapToEntity(finFlowUserRegisterDto)).thenReturn(finFlowUser);
        when(finFlowUserRepository.add(finFlowUser)).thenReturn(finFlowUser);
        when(finflowUserMapper.mapToResponseDto(finFlowUser))
                .thenReturn(new FinFlowUserResponseDto(
                        username,
                        firstName,
                        lastName,
                        email,
                        phoneNumber));

        //When
        FinFlowUserResponseDto responseDto = finFlowUserService.registerUser(finFlowUserRegisterDto);

        //Then
        assertThat(responseDto.username()).isEqualTo(finFlowUser.getUsername());
        assertThat(responseDto.firstName()).isEqualTo(finFlowUser.getFirstName());
        assertThat(responseDto.lastName()).isEqualTo(finFlowUser.getLastName());
        assertThat(responseDto.email()).isEqualTo(finFlowUser.getEmail());
        assertThat(responseDto.phoneNumber()).isEqualTo(finFlowUser.getPhoneNumber());

        verify(finflowUserMapper).mapToEntity(finFlowUserRegisterDto);
        verify(finFlowUserRepository).add(finFlowUser);
    }
}
