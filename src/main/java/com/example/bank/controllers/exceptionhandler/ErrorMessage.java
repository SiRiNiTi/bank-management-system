package com.example.bank.controllers.exceptionhandler;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


@Builder
@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorMessage {

    @Schema(description = "Date time of response", example = "2023-10-20T11:07:08.119Z")
    private LocalDateTime timestamp;

    @Schema(description = "Status code", example = "400")
    private int status;

    @Schema(description = "Pretty description of error", example = "Something went wrong")
    private String message;

    @Schema(description = "Detailed description of error")
    private String detailedMessage;
}
