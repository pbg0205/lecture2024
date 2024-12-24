package com.cooper.lecture2024.business.errors.exception;

import lombok.Getter;

import com.cooper.lecture2024.business.errors.LectureErrorType;

@Getter
public class InvalidLectureSearchConditionException extends LectureException {
	public InvalidLectureSearchConditionException(final LectureErrorType lectureErrorType) {
		super(lectureErrorType);
	}
}
