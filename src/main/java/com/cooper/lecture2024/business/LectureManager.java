package com.cooper.lecture2024.business;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.dto.ApplyCreationResult;
import com.cooper.lecture2024.business.dto.response.ApplySuccessResult;
import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.business.errors.LectureErrorType;
import com.cooper.lecture2024.business.errors.exception.LectureDuplicatedRegistrationException;
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

	@Transactional(timeout = 5)
	public ApplyCreationResult applyLecture(final Long studentId, final Long lectureId) {
		final Lecture lecture = findLectureByIdForUpdate(lectureId);
		lecture.decreaseRemainingCount();

		if (lectureRepository.existLectureApplyByStudentIdAndLectureId(studentId, lectureId)) {
			throw new LectureDuplicatedRegistrationException(LectureErrorType.LECTURE_DUPLICATED_REGISTRATION);
		}
		lectureRepository.saveLectureApply(studentId, lectureId);

		return new ApplyCreationResult(lecture.getTitle());
	}

	private Lecture findLectureByIdForUpdate(final Long lectureId) {
		return Optional.ofNullable(lectureRepository.findLectureByIdForUpdate(lectureId))
			.orElseThrow(() -> new LectureNotFoundException(LectureErrorType.LECTURE_NOT_FOUND));
	}

	@Transactional(readOnly = true)
	public List<ApplySuccessResult> findAllApplySuccessByStudentId(final Long studentId) {
		return lectureRepository.findAllApplySuccessByStudentId(studentId);
	}
}
