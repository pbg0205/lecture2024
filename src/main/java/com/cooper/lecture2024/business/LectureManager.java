package com.cooper.lecture2024.business;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.dto.ApplyCreationResult;
import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.business.errors.LectureErrorType;
import com.cooper.lecture2024.business.errors.exception.LectureNotFoundException;
import com.cooper.lecture2024.business.repository.LectureRepository;
import com.cooper.lecture2024.common.annotations.Manager;
import com.cooper.lecture2024.domain.Lecture;

@Manager
@RequiredArgsConstructor
public class LectureManager {

	private final LectureRepository lectureRepository;

	@Transactional(readOnly = true)
	public List<LectureQueryResult> findAllLectureQueryByStartAtBetween(
		final LocalDateTime startDateTime,
		final LocalDateTime deadLineToStart) {
		return lectureRepository.findAllLectureQueryByStartAtBetween(startDateTime, deadLineToStart);
	}

	@Transactional
	public ApplyCreationResult applyLecture(final Long studentId, final Long lectureId) {
		final Lecture lecture = findLectureById(lectureId);
		lecture.decreaseRemainingCount();

		lectureRepository.saveLectureApply(studentId, lectureId);

		return new ApplyCreationResult(lecture.getTitle());
	}

	private Lecture findLectureById(final Long lectureId) {
		return Optional.ofNullable(lectureRepository.findLectureById(lectureId))
			.orElseThrow(() -> new LectureNotFoundException(LectureErrorType.LECTURE_NOT_FOUND));
	}
}
