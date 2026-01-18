package bg.lrsoft.rlfinflow.config.mapper;

import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserRegisterDto;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserResponseDto;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.security.FinFlowOath2User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR, unmappedTargetPolicy = IGNORE)
public interface FinFlowUserMapper {

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "authorities", ignore = true)
    FinFlowUser mapToEntity(FinFlowUserRegisterDto finFlowUserImportDto);

    default FinFlowUserResponseDto mapToResponseDto(FinFlowUser finFlowUser) {
        return new FinFlowUserResponseDto(
                finFlowUser.getUsername(),
                finFlowUser.getEmail(),
                null);
    }

    default FinFlowUserResponseDto mapToResponseDto(OAuth2User oAuth2User) {
        return new FinFlowUserResponseDto(
                oAuth2User.getAttribute("name"),
                oAuth2User.getAttribute("email"),
                oAuth2User.getAttribute("picture"));
    }

    default FinFlowUser fromPrincipal(FinFlowOath2User principal) {
        FinFlowUser user = new FinFlowUser(
                principal.getAttribute("name"),
                null,
                principal.getEmail(),
                new ArrayList<>()
        );

        user.updateFromPrincipal(principal);
        return user;
    }
}
