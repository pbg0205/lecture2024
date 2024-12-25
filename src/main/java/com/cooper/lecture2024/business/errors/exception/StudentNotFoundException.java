package com.cooper.lecture2024.business.errors.exception;

import com.cooper.lecture2024.business.errors.StudentErrorType;

public class StudentNotFoundException extends StudentException {
	public StudentNotFoundException(final StudentErrorType studentErrorType) {
		super(studentErrorType);
	}
}
