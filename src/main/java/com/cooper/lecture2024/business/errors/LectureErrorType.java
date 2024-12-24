package com.cooper.lecture2024.business.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LectureErrorType {

	START_DEAD_LINE_BEFORE_START_DATE(LectureErrorCode.LECTURE01, "강의 시작 데드 라인은 검색 시작 일자보다 과거일 수 없습니다.");

	private final LectureErrorCode errorCode;
	private final String message;
}
