package com.cooper.lecture2024.business.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StudentErrorType {
	STUDENT_NOT_FOUND(StudentErrorCode.STUDENT01, "해당 학생을 찾을 수 없습니다.");

	private final StudentErrorCode errorCode;
	private final String message;
}
