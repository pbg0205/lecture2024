package com.cooper.lecture2024.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.lecture2024.business.dto.ApplyCreationResult;
import com.cooper.lecture2024.business.dto.response.ApplySuccessResult;
import com.cooper.lecture2024.business.dto.response.LectureApplyResult;
import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.business.dto.response.StudentQueryResult;
import com.cooper.lecture2024.business.errors.LectureErrorCode;
import com.cooper.lecture2024.business.errors.LectureErrorType;
import com.cooper.lecture2024.business.errors.exception.InvalidLectureSearchConditionException;

@ExtendWith(MockitoExtension.class)
class LectureApplyFacadeTest {

	@InjectMocks
	private LectureApplyFacade lectureApplyFacade;

	@Mock
	private LectureManager lectureManager;

	@Mock
	private StudentManager studentManager;

	@Test
	@DisplayName("[실패] 검색 강의 시작 데드라인 < 검색 강의 시작 시간인 경우, 요청 실패")
	void validateStartDeadLineBeforeStartDateTime() {
		// given
		final LocalDateTime startDateTime = LocalDateTime.of(2024, 12, 24, 0, 0);
		final LocalDateTime deadLineToStart = LocalDateTime.of(2024, 12, 20, 0, 0);

		// when, then
		assertThatThrownBy(() -> lectureApplyFacade.findAllLectureQueryByStartAtBetween(startDateTime, deadLineToStart))
			.isInstanceOf(InvalidLectureSearchConditionException.class)
			.extracting("errorType")
			.isInstanceOf(LectureErrorType.class)
			.satisfies(
				errorType -> assertThat(((LectureErrorType)errorType).getErrorCode()).isEqualTo(
					LectureErrorCode.LECTURE01));
	}

	@Test
	@DisplayName("[성공] 검색 날짜와 일치하는 강의 시작 존재 시, 목록 반환")
	void findAllLectureQueryByStartAtBetween() {
		// given
		final LocalDateTime searchDateTime = LocalDateTime.of(2024, 12, 24, 0, 0);
		final LocalDateTime deadLineToStart = LocalDateTime.of(2024, 12, 24, 0, 0);

		when(lectureManager.findAllLectureQueryByStartAtBetween(any(), any())).thenReturn(
			List.of(
				new LectureQueryResult(1L, "강의 이름1", 30, "강연자1", LocalDateTime.of(2024, 12, 24, 9, 0)),
				new LectureQueryResult(2L, "강의 이름2", 15, "강연자2", LocalDateTime.of(2024, 12, 24, 12, 0)),
				new LectureQueryResult(3L, "강의 이름2", 15, "강연자3", LocalDateTime.of(2024, 12, 24, 13, 0)),
				new LectureQueryResult(4L, "강의 이름2", 15, "강연자4", LocalDateTime.of(2024, 12, 24, 14, 0))
			));

		// when
		final List<LectureQueryResult> lectureQueryResultList =
			lectureApplyFacade.findAllLectureQueryByStartAtBetween(searchDateTime, deadLineToStart);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(lectureQueryResultList.size()).isEqualTo(4);

			boolean startDateMatch = lectureQueryResultList.stream()
				.allMatch(lecture -> lecture.startAt().toLocalDate().isEqual(LocalDate.of(2024, 12, 24)));
			softAssertions.assertThat(startDateMatch).isTrue();
		});
	}

	@Test
	@DisplayName("[실패] 검색 날짜와 일치하는 강의 시작 존재 시, 목록 반환")
	void notFoundStudent() {
		// given
		final LocalDateTime searchDateTime = LocalDateTime.of(2024, 12, 24, 0, 0);
		final LocalDateTime deadLineToStart = LocalDateTime.of(2024, 12, 24, 0, 0);

		when(lectureManager.findAllLectureQueryByStartAtBetween(any(), any())).thenReturn(
			List.of(
				new LectureQueryResult(1L, "강의 이름1", 30, "강연자1", LocalDateTime.of(2024, 12, 24, 9, 0)),
				new LectureQueryResult(2L, "강의 이름2", 15, "강연자2", LocalDateTime.of(2024, 12, 24, 12, 0)),
				new LectureQueryResult(3L, "강의 이름2", 15, "강연자3", LocalDateTime.of(2024, 12, 24, 13, 0)),
				new LectureQueryResult(4L, "강의 이름2", 15, "강연자4", LocalDateTime.of(2024, 12, 24, 14, 0))
			));

		// when
		final List<LectureQueryResult> lectureQueryResultList =
			lectureApplyFacade.findAllLectureQueryByStartAtBetween(searchDateTime, deadLineToStart);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(lectureQueryResultList.size()).isEqualTo(4);

			boolean startDateMatch = lectureQueryResultList.stream()
				.allMatch(lecture -> lecture.startAt().toLocalDate().isEqual(LocalDate.of(2024, 12, 24)));
			softAssertions.assertThat(startDateMatch).isTrue();
		});
	}

	@Test
	@DisplayName("[성공] 특강 신청 성공")
	void applyLecture() {
		// given
		final Long userId = 1L;
		final Long lectureId = 100L;

		when(studentManager.findStudentQueryById(any())).thenReturn(new StudentQueryResult(1L, "학생 이름1"));
		when(lectureManager.applyLecture(any(), any())).thenReturn(new ApplyCreationResult("강의명1"));

		// when
		final LectureApplyResult lectureApplyResult = lectureApplyFacade.applyLecture(userId, lectureId);

		// then
		SoftAssertions.assertSoftly(softAssertions -> {
			softAssertions.assertThat(lectureApplyResult.studentName()).isEqualTo("학생 이름1");
			softAssertions.assertThat(lectureApplyResult.lectureName()).isEqualTo("강의명1");
		});
	}

	@Test
	@DisplayName("[성공] 학생 수강 신청 성공 목록 조회")
	void findAllApplySuccessByStudentId() {
		// given
		final Long userId = 1L;

		when(studentManager.findStudentQueryById(any())).thenReturn(new StudentQueryResult(1L, "학생 이름1"));
		when(lectureManager.findAllApplySuccessByStudentId(any())).thenReturn(List.of(
			new ApplySuccessResult(1L, "강의명1", "강연자1"),
			new ApplySuccessResult(2L, "강의명2", "강연자2"),
			new ApplySuccessResult(3L, "강의명3", "강연자3"),
			new ApplySuccessResult(4L, "강의명4", "강연자4"),
			new ApplySuccessResult(5L, "강의명5", "강연자5")
		));

		// when
		final List<ApplySuccessResult> applySuccessResults = lectureApplyFacade.findAllApplySuccessByStudentId(userId);

		// then
		assertThat(applySuccessResults).hasSize(5);
	}
}
