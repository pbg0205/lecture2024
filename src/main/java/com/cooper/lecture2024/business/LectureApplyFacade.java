package com.cooper.lecture2024.business;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.dto.ApplyCreationResult;
import com.cooper.lecture2024.business.dto.response.ApplySuccessResult;
import com.cooper.lecture2024.business.dto.response.LectureApplyResult;
import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.business.dto.response.StudentQueryResult;
import com.cooper.lecture2024.business.errors.LectureErrorType;
import com.cooper.lecture2024.business.errors.exception.InvalidLectureSearchConditionException;
import com.cooper.lecture2024.common.annotations.Facade;

@Facade
@RequiredArgsConstructor
public class LectureApplyFacade {

	private final LectureManager lectureManager;
	private final StudentManager studentManager;

	@Transactional(readOnly = true)
	public List<LectureQueryResult> findAllLectureQueryByStartAtBetween(
		final LocalDateTime startDateTime, final LocalDateTime deadLineToStart) {

		if (deadLineToStart.isBefore(startDateTime)) {
			throw new InvalidLectureSearchConditionException(LectureErrorType.START_DEAD_LINE_BEFORE_START_DATE);
		}

		return lectureManager.findAllLectureQueryByStartAtBetween(startDateTime, deadLineToStart);
	}

	@Transactional
	public LectureApplyResult applyLecture(final Long studentId, final Long lectureId) {
		final StudentQueryResult studentQueryResult = studentManager.findStudentQueryById(studentId);

		final ApplyCreationResult applyCreationResult = lectureManager.applyLecture(studentQueryResult.id(), lectureId);

		return new LectureApplyResult(studentQueryResult.name(), applyCreationResult.lectureTitle());
	}

	@Transactional(readOnly = true)
	public List<ApplySuccessResult> findAllApplySuccessByStudentId(final Long studentId) {
		final StudentQueryResult studentQueryById = studentManager.findStudentQueryById(studentId);
		return lectureManager.findAllApplySuccessByStudentId(studentId);
	}
}
