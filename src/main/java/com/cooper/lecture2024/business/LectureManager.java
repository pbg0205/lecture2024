package com.cooper.lecture2024.business;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.business.repository.LectureRepository;
import com.cooper.lecture2024.common.annotations.Manager;

@Manager
@RequiredArgsConstructor
public class LectureManager {

	private final LectureRepository lectureRepository;

	@Transactional(readOnly = true)
	public List<LectureQueryResult> findLecturesByStartAtBetween(
		final LocalDateTime startDateTime,
		final LocalDateTime deadLineToStart) {
		return lectureRepository.findLecturesByStartAtBetween(startDateTime, deadLineToStart);
	}
}
