package bg.lrsoft.rlfinflow.config.mapper;

import bg.lrsoft.rlfinflow.domain.dto.FinFlowUserImportDto;
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
    FinFlowUser mapToEntity(FinFlowUserImportDto finFlowUserImportDto);
}
