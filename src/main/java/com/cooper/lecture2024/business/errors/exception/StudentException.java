package com.cooper.lecture2024.business.errors.exception;

import lombok.Getter;

import com.cooper.lecture2024.business.errors.StudentErrorType;

@Getter
public abstract class StudentException extends RuntimeException {

	private final StudentErrorType errorType;

	public StudentException(final StudentErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}
}
