package com.cooper.lecture2024.business.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LectureErrorType {

	START_DEAD_LINE_BEFORE_START_DATE(LectureErrorCode.LECTURE01, "강의 시작 데드 라인은 검색 시작 일자보다 과거일 수 없습니다."),
	LECTURE_NOT_FOUND(LectureErrorCode.LECTURE02, "강의를 찾을 수 없습니다."),
	GREATER_THAN_CAPACITY(LectureErrorCode.LECTURE03, "현재 신청 가능 수는 최대 수강 신청 가능 수를 초과할 수 없습니다"),
	REMAINING_COUNT_NEGATIVE(LectureErrorCode.LECTURE04, "현재 신청 가능 수는 최대 수강 신청 가능 수는 음수일 수 없습니다"),
	LECTURE_REGISTRATION_CLOSED(LectureErrorCode.LECTURE05, "해당 강의의 수강 신청이 마감되었습니다.");

	private final LectureErrorCode errorCode;
	private final String message;
}
