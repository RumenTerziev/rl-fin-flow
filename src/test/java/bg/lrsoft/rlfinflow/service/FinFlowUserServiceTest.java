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
        String email = "test@test.com";
        FinFlowUserRegisterDto finFlowUserRegisterDto = new FinFlowUserRegisterDto(
                username,
                password,
                email);

        FinFlowUser finFlowUser = new FinFlowUser(
                username,
                password,
                email,
                AuthorityUtils.createAuthorityList("USER"));

        when(finflowUserMapper.mapToEntity(finFlowUserRegisterDto)).thenReturn(finFlowUser);
        when(finFlowUserRepository.add(finFlowUser)).thenReturn(finFlowUser);
        when(finflowUserMapper.mapToResponseDto(finFlowUser))
                .thenReturn(new FinFlowUserResponseDto(
                        username,
                        email));

        //When
        FinFlowUserResponseDto responseDto = finFlowUserService.registerUser(finFlowUserRegisterDto);

        //Then
        assertThat(responseDto.username()).isEqualTo(finFlowUser.getUsername());
        assertThat(responseDto.email()).isEqualTo(finFlowUser.getEmail());

        verify(finflowUserMapper).mapToEntity(finFlowUserRegisterDto);
        verify(finFlowUserRepository).add(finFlowUser);
    }
}
