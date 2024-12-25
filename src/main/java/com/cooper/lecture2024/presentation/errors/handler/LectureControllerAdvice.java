package com.cooper.lecture2024.presentation.errors.handler;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.cooper.lecture2024.business.errors.LectureErrorType;
import com.cooper.lecture2024.business.errors.StudentErrorType;
import com.cooper.lecture2024.business.errors.exception.LectureException;
import com.cooper.lecture2024.business.errors.exception.StudentException;
import com.cooper.lecture2024.common.api.errors.ErrorResponse;

@RestControllerAdvice
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class LectureControllerAdvice {

	@ExceptionHandler(LectureException.class)
	public ResponseEntity<ErrorResponse> handleLectureException(LectureException exception) {
		final LectureErrorType errorType = exception.getErrorType();
		return ResponseEntity.badRequest()
			.body(new ErrorResponse(errorType.getErrorCode().name(), errorType.getMessage()));
	}

	@ExceptionHandler(StudentException.class)
	public ResponseEntity<ErrorResponse> handleStudentException(StudentException exception) {
		final StudentErrorType errorType = exception.getErrorType();
		return ResponseEntity.badRequest()
			.body(new ErrorResponse(errorType.getErrorCode().name(), errorType.getMessage()));
	}
}
