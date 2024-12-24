package com.cooper.lecture2024.common.api.errors;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@NoArgsConstructor(force = true)
@RequiredArgsConstructor
public class ErrorResponse {
	private final String code;
	private final String message;
}
