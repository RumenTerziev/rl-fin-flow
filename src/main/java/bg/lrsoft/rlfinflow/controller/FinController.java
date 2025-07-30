package bg.lrsoft.rlfinflow.controller;

import bg.lrsoft.rlfinflow.domain.dto.ConversionResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyRequestDto;
import bg.lrsoft.rlfinflow.domain.dto.CurrencyResponseDto;
import bg.lrsoft.rlfinflow.domain.dto.ErrorPayloadDto;
import bg.lrsoft.rlfinflow.domain.model.PageResult;
import bg.lrsoft.rlfinflow.service.CurrencyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

@Tag(name = "Converter", description = "Converter operations APIs")
@RestController
@RequestMapping("/converter")
public class FinController {

    private final CurrencyService openApiRateCurrencyService;
    private final CurrencyService bnbRatecurrencyService;

    public FinController(@Qualifier("OpenApiRateCurrencyService") CurrencyService openApiRateCurrencyService,
                         @Qualifier("BNBRateCurrencyService") CurrencyService bnbRatecurrencyService) {
        this.openApiRateCurrencyService = openApiRateCurrencyService;
        this.bnbRatecurrencyService = bnbRatecurrencyService;
    }

    @Operation(description = "Convert currencies")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CurrencyResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPayloadDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPayloadDto.class)
                    )
            )
    })
    @PostMapping("/open-api-rates")
    public CurrencyResponseDto exchangeByOpenApiRate(@Valid @RequestBody CurrencyRequestDto currencyRequestDto) {
        return openApiRateCurrencyService.processConvertRequest(currencyRequestDto);
    }

    @PostMapping("/bnb-rates")
    public CurrencyResponseDto exchangeByBNBRate(@Valid @RequestBody CurrencyRequestDto currencyRequestDto) {
        return bnbRatecurrencyService.processConvertRequest(currencyRequestDto);
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CurrencyResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPayloadDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPayloadDto.class)
                    )
            )
    })
    @GetMapping("/conversions")
    public List<ConversionResponseDto> getConversions() {
        return bnbRatecurrencyService.findAll();
    }

    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CurrencyResponseDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Bad Request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPayloadDto.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorPayloadDto.class)
                    )
            )
    })
    @GetMapping("/conversions/mine")
    public PageResult<ConversionResponseDto> getMyConversions(
            @PageableDefault(sort = "createdAt", direction = DESC, size = 5) Pageable pageable) {
        return bnbRatecurrencyService.findByAuthenticatedUser(pageable);
    }
}
