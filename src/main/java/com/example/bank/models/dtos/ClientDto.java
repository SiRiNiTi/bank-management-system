package com.example.bank.models.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import static io.swagger.v3.oas.annotations.media.Schema.RequiredMode.REQUIRED;

@Data
public class ClientDto {

    @Schema(description = "Beneficiary name", example = "Ivanov Ivan Ivanovich", requiredMode = REQUIRED)
    @NotBlank(message = "Beneficiary name is mandatory")
    private String name;

    @Schema(description = "PIN code (4 numbers)", example = "2345", requiredMode = REQUIRED)
    @NotNull
    @Min(1000)
    @Max(9999)
    private Integer pin;

}
