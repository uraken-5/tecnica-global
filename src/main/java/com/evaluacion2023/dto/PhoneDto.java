package com.evaluacion2023.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PhoneDto {
	@NotNull
    private long number;
	@NotNull
    private int cityCode;
	@NotNull
    private String countryCode;
}