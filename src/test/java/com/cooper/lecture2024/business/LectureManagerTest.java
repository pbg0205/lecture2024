package com.cooper.lecture2024.business;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.business.repository.LectureRepository;

@ExtendWith(MockitoExtension.class)
class LectureManagerTest {

	@InjectMocks
	private LectureManager lectureManager;

	@Mock
	private LectureRepository lectureRepository;

	@DisplayName("[성공] 검색 강의 시작 시간과 일치하는 강의 존재시, 목록 반환")
	@Test
	void findLecturesByStartAtBetween() {
		// given
		final LocalDateTime searchDateTime = LocalDateTime.of(2024, 12, 24, 0, 0);
		final LocalDateTime deadLineToStart = LocalDateTime.of(2024, 12, 24, 0, 0);

		when(lectureRepository.findLecturesByStartAtBetween(any(), any())).thenReturn(
			List.of(
				new LectureQueryResult(1L, "강의 이름1", 30, "강연자1", LocalDateTime.of(2024, 12, 24, 9, 0)),
				new LectureQueryResult(2L, "강의 이름2", 15, "강연자2", LocalDateTime.of(2024, 12, 24, 12, 0)),
				new LectureQueryResult(3L, "강의 이름2", 15, "강연자3", LocalDateTime.of(2024, 12, 24, 13, 0)),
				new LectureQueryResult(4L, "강의 이름2", 15, "강연자4", LocalDateTime.of(2024, 12, 24, 14, 0))
			));

		// when
		final List<LectureQueryResult> lectureQueryResultList = lectureManager.findLecturesByStartAtBetween(
			searchDateTime, deadLineToStart);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(lectureQueryResultList.size()).isEqualTo(4);

			boolean startDateMatch = lectureQueryResultList.stream()
				.allMatch(lecture -> lecture.startAt().toLocalDate().isEqual(LocalDate.of(2024, 12, 24)));
			softAssertions.assertThat(startDateMatch).isTrue();
		});
	}

	@DisplayName("[성공] 검색 날짜와 일치하는 강의 미존재시, 빈 값 반환")
	@Test
	void LecturesZeroByStartTimeAtBetween() {
		// given
		final LocalDateTime searchDateTime = LocalDateTime.of(2024, 12, 28, 0, 0);
		final LocalDateTime deadLineToStart = LocalDateTime.of(2024, 12, 28, 23, 59);

		when(lectureRepository.findLecturesByStartAtBetween(any(), any())).thenReturn(List.of());

		// when
		final List<LectureQueryResult> lectureQueryResultList =
			lectureManager.findLecturesByStartAtBetween(searchDateTime, deadLineToStart);

		// then
		assertThat(lectureQueryResultList).hasSize(0);
	}
}
