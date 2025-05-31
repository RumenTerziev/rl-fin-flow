package bg.lrsoft.rlfinflow.config.mapper;

import bg.lrsoft.rlfinflow.domain.dto.ConversionResponseDto;
import bg.lrsoft.rlfinflow.domain.model.Conversion;
import org.mapstruct.Mapper;

import java.time.format.DateTimeFormatter;

import static org.mapstruct.InjectionStrategy.CONSTRUCTOR;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;
import static org.mapstruct.ReportingPolicy.IGNORE;

@Mapper(componentModel = SPRING, injectionStrategy = CONSTRUCTOR, unmappedTargetPolicy = IGNORE)
public interface ConversionMapper {

    default ConversionResponseDto mapToResponseDto(Conversion conversion) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return new ConversionResponseDto(
                conversion.getBaseCurrency(),
                conversion.getCurrencyToConvertTo(),
                conversion.getSumToConvert(),
                conversion.getResultSum(),
                conversion.getCurrencyRate(),
                dateTimeFormatter.format(conversion.getCreatedAt())
        );
    }
}
