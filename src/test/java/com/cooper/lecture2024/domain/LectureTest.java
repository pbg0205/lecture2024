package com.cooper.lecture2024.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.cooper.lecture2024.business.errors.LectureErrorCode;
import com.cooper.lecture2024.business.errors.LectureErrorType;
import com.cooper.lecture2024.business.errors.exception.LectureConstraintsViolationException;

class LectureTest {

	@DisplayName("[실패] 최대 신청 가능 학생 수 < 신청 가능 학생 수, 객체 생성 실패")
	@Test
	void remainingCountGreaterThanCapacity() {
		// given
		int capacity = 30;
		int remainingCount = 31;

		// when, then
		Assertions.assertThatThrownBy(
				() -> new Lecture("title", LocalDateTime.of(2024, 1, 1, 1, 0), 1L, remainingCount, capacity))
			.isInstanceOf(LectureConstraintsViolationException.class)
			.extracting("errorType")
			.isInstanceOf(LectureErrorType.class)
			.satisfies(
				errorType -> assertThat(((LectureErrorType)errorType).getErrorCode()).isEqualTo(
					LectureErrorCode.LECTURE03));
	}

	@DisplayName("[실패] 신청 가능 학생 수 < 0, 객체 생성 실패")
	@Test
	void remainingCountNegative() {
		// given
		int capacity = 30;
		int remainingCount = -1;

		// when, then
		Assertions.assertThatThrownBy(
				() -> new Lecture("title", LocalDateTime.of(2024, 1, 1, 1, 0), 1L, remainingCount, capacity))
			.isInstanceOf(LectureConstraintsViolationException.class)
			.extracting("errorType")
			.isInstanceOf(LectureErrorType.class)
			.satisfies(
				errorType -> assertThat(((LectureErrorType)errorType).getErrorCode()).isEqualTo(
					LectureErrorCode.LECTURE04));
	}
}
