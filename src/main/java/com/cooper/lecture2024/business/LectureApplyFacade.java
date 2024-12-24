package com.cooper.lecture2024.business;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.business.errors.LectureErrorType;
import com.cooper.lecture2024.business.errors.exception.InvalidLectureSearchConditionException;
import com.cooper.lecture2024.common.annotations.Facade;

@Facade
@RequiredArgsConstructor
public class LectureApplyFacade {

	private final LectureManager lectureManager;

	@Transactional(readOnly = true)
	public List<LectureQueryResult> findLecturesByStartAtBetween(
		final LocalDateTime startDateTime, final LocalDateTime deadLineToStart) {

		if (deadLineToStart.isBefore(startDateTime)) {
			throw new InvalidLectureSearchConditionException(LectureErrorType.START_DEAD_LINE_BEFORE_START_DATE);
		}

		return lectureManager.findLecturesByStartAtBetween(startDateTime, deadLineToStart);
	}
}
