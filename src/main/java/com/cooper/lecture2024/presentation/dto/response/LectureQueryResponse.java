package com.cooper.lecture2024.presentation.dto.response;

import java.time.LocalDateTime;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.dto.response.LectureQueryResult;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class LectureQueryResponse {
	private final Long lectureId;
	private final String title;
	private final String lecturerName;
	private final Integer remainingCount;
	private final LocalDateTime startAt;

	public static LectureQueryResponse from(final LectureQueryResult lectureQueryResult) {
		return new LectureQueryResponse(
			lectureQueryResult.lectureId(),
			lectureQueryResult.title(),
			lectureQueryResult.lecturer(),
			lectureQueryResult.remainingCount(),
			lectureQueryResult.startAt());
	}
}
