package com.cooper.lecture2024.business.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.cooper.lecture2024.business.dto.response.LectureQueryResult;

public interface LectureRepository {
	List<LectureQueryResult> findLecturesByStartAtBetween(
		final LocalDateTime startDateTime, final LocalDateTime deadLineToStart);
}
