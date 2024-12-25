package com.cooper.lecture2024.common.api.handler;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.extern.slf4j.Slf4j;

import com.cooper.lecture2024.common.api.errors.CommonErrorType;
import com.cooper.lecture2024.common.api.errors.ErrorResponse;

@RestControllerAdvice
@Slf4j
@Order
public class GlobalControllerAdvice {

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception exception) {
		final CommonErrorType errorType = CommonErrorType.UNKNOWN;
		loggingError(exception);
		return ResponseEntity.internalServerError()
			.body(new ErrorResponse(errorType.getErrorCode().name(), errorType.getMessage()));
	}

	@ExceptionHandler(TypeMismatchException.class)
	public ResponseEntity<ErrorResponse> handleException(TypeMismatchException exception) {
		final CommonErrorType errorType = CommonErrorType.TYPE_MISS_MATCH;
		loggingError(exception);
		return ResponseEntity.badRequest()
			.body(new ErrorResponse(errorType.getErrorCode().name(), errorType.getMessage()));
	}

	private void loggingError(final Exception exception) {
		log.error("exception class : {}, exception message: {}", exception.getClass(), exception.getMessage());
	}

}
