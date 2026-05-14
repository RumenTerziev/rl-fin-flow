package bg.lrsoft.rlfinflow.config.mapper;

import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserResponseDto;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import bg.lrsoft.rlfinflow.security.FinFlowOath2User;
import org.mapstruct.Mapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR, unmappedTargetPolicy = IGNORE)
public interface FinFlowUserMapper {

    default FinFlowUserResponseDto mapToResponseDto(FinFlowUser finFlowUser) {
        return new FinFlowUserResponseDto(
                finFlowUser.getUsername(),
                finFlowUser.getEmail(),
                finFlowUser.getPictureUrl());
    }

    default FinFlowUserResponseDto mapToResponseDto(OAuth2User oAuth2User) {
        return new FinFlowUserResponseDto(
                oAuth2User.getAttribute("name"),
                oAuth2User.getAttribute("email"),
                oAuth2User.getAttribute("picture"));
    }

    default FinFlowUser fromPrincipal(FinFlowOath2User principal) {
        Set<String> authorities = principal.getAuthorities() == null ? new HashSet<>()
                : principal.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toCollection(HashSet::new));
        return new FinFlowUser(
                principal.getAttribute("name"),
                principal.getEmail(),
                principal.getAttribute("picture"),
                authorities);
    }
}
