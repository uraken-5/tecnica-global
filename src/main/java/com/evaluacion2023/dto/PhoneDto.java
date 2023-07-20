package com.evaluacion2023.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;


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