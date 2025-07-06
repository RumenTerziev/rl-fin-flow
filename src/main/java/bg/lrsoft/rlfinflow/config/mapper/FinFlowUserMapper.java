package bg.lrsoft.rlfinflow.config.mapper;

import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserRegisterDto;
import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserResponseDto;
import bg.lrsoft.rlfinflow.domain.model.FinFlowUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

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
                finFlowUser.getEmail());
    }
}
