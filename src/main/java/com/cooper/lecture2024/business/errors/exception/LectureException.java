package com.cooper.lecture2024.business.errors.exception;

import lombok.Getter;

import com.cooper.lecture2024.business.errors.LectureErrorType;

@Getter
public abstract class LectureException extends RuntimeException {

	private final LectureErrorType errorType;

	public LectureException(final LectureErrorType errorType) {
		super(errorType.getMessage());
		this.errorType = errorType;
	}
}
