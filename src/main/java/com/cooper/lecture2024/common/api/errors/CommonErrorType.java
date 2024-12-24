package com.cooper.lecture2024.common.api.errors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommonErrorType {
	UNKNOWN(CommonErrorCode.UNKNOWN, "알 수 없는 에러가 발생했습니다."),
	TYPE_MISS_MATCH(CommonErrorCode.COMMON01, "요청 값을 지원하지 않거나 올바르지 않은 값입니다.");

	private final CommonErrorCode errorCode;
	private final String message;
}
