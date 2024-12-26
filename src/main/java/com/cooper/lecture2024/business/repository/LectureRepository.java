package com.cooper.lecture2024.business.repository;

import java.time.LocalDateTime;
import java.util.List;

import com.cooper.lecture2024.business.dto.response.ApplySuccessResult;
import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.domain.Lecture;
import com.cooper.lecture2024.domain.LectureApply;

public interface LectureRepository {
	List<LectureQueryResult> findAllLectureQueryByStartAtBetween(
		final LocalDateTime startDateTime, final LocalDateTime deadLineToStart);

	Lecture findLectureByIdForUpdate(Long lectureId);

	LectureApply saveLectureApply(Long studentId, Long lectureId);

	List<ApplySuccessResult> findAllApplySuccessByStudentId(Long studentId);

	boolean existLectureApplyByStudentIdAndLectureId(Long studentId, Long lectureId);
}
