package com.cooper.lecture2024.presentation.dto.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.dto.response.ApplySuccessResult;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ApplySuccessResponse {
	private final Long lectureId;
	private final String title;
	private final String lecturerName;

	public static ApplySuccessResponse from(final ApplySuccessResult applySuccessResult) {
		return new ApplySuccessResponse(
			applySuccessResult.lectureId(),
			applySuccessResult.title(),
			applySuccessResult.lecturerName());
	}
}
