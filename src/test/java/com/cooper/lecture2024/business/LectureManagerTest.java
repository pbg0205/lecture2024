package com.cooper.lecture2024.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

import com.cooper.lecture2024.business.dto.ApplyCreationResult;
import com.cooper.lecture2024.business.dto.response.ApplySuccessResult;
import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.business.errors.LectureErrorCode;
import com.cooper.lecture2024.business.errors.LectureErrorType;
import com.cooper.lecture2024.business.errors.exception.LectureDuplicatedRegistrationException;
import com.cooper.lecture2024.business.errors.exception.LectureNotFoundException;
import com.cooper.lecture2024.business.repository.LectureRepository;
import com.cooper.lecture2024.domain.Lecture;
import com.cooper.lecture2024.domain.LectureApply;

@ExtendWith(MockitoExtension.class)
class LectureManagerTest {

	@InjectMocks
	private LectureManager lectureManager;

	@Mock
	private LectureRepository lectureRepository;

	@DisplayName("[성공] 검색 강의 시작 시간과 일치하는 강의 존재시, 목록 반환")
	@Test
	void findLectureQueryResultsByStartAtBetween() {
		// given
		final LocalDateTime searchDateTime = LocalDateTime.of(2024, 12, 24, 0, 0);
		final LocalDateTime deadLineToStart = LocalDateTime.of(2024, 12, 24, 0, 0);

		when(lectureRepository.findAllLectureQueryByStartAtBetween(any(), any())).thenReturn(
			List.of(
				new LectureQueryResult(1L, "강의 이름1", 30, "강연자1", LocalDateTime.of(2024, 12, 24, 9, 0)),
				new LectureQueryResult(2L, "강의 이름2", 15, "강연자2", LocalDateTime.of(2024, 12, 24, 12, 0)),
				new LectureQueryResult(3L, "강의 이름2", 15, "강연자3", LocalDateTime.of(2024, 12, 24, 13, 0)),
				new LectureQueryResult(4L, "강의 이름2", 15, "강연자4", LocalDateTime.of(2024, 12, 24, 14, 0))
			));

		// when
		final List<LectureQueryResult> lectureQueryResultList = lectureManager.findAllLectureQueryByStartAtBetween(
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

		when(lectureRepository.findAllLectureQueryByStartAtBetween(any(), any())).thenReturn(List.of());

		// when
		final List<LectureQueryResult> lectureQueryResultList =
			lectureManager.findAllLectureQueryByStartAtBetween(searchDateTime, deadLineToStart);

		// then
		assertThat(lectureQueryResultList).hasSize(0);
	}

	@DisplayName("[실패] 강의 아이디와 일치하는 강의가 없는 경우, 요청 실패")
	@Test
	void lectureNotFound() {
		// given
		final long studentId = 1L;
		final long lectureId = 1L;

		when(lectureRepository.findLectureByIdForUpdate(any())).thenReturn(null);

		// when, then
		assertThatThrownBy(() -> lectureManager.applyLecture(studentId, lectureId))
			.isInstanceOf(LectureNotFoundException.class)
			.extracting("errorType")
			.isInstanceOf(LectureErrorType.class)
			.satisfies(
				errorType -> assertThat(((LectureErrorType)errorType).getErrorCode()).isEqualTo(
					LectureErrorCode.LECTURE02));
	}

	@DisplayName("[성공] 강의 아이디를 통해 강의 조회")
	@Test
	void findLectureByIdForUpdate() {
		// given
		final long studentId = 1L;
		final long lectureId = 1L;

		when(lectureRepository.findLectureByIdForUpdate(any())).thenReturn(
			new Lecture("lecture_title01", LocalDateTime.now(), 2L, 30, 30));

		// when
		final ApplyCreationResult applyCreationResult = lectureManager.applyLecture(studentId, lectureId);

		// then
		assertThat(applyCreationResult.lectureTitle()).isEqualTo("lecture_title01");
	}

	@DisplayName("[성공] 강의 신청 저장")
	@Test
	void saveLectureApply() {
		// given
		final Long studentId = 1L;
		final Long lectureId = 1L;

		when(lectureRepository.saveLectureApply(any(), any())).thenReturn(
			new LectureApply(studentId, lectureId));
		when(lectureRepository.findLectureByIdForUpdate(any()))
			.thenReturn(new Lecture("lecture_title", LocalDateTime.of(2024, 12, 24, 12, 0), 1L, 30, 30));

		// when
		final ApplyCreationResult applyCreationResult = lectureManager.applyLecture(studentId, lectureId);

		// then
		assertThat(applyCreationResult.lectureTitle()).isEqualTo("lecture_title");
	}

	@DisplayName("[실패] 중복 수강 신청 실패")
	@Test
	void isDisableToRegisterDuplicatedLectureApply() {
		// given
		final Long studentId = 1L;
		final Long lectureId = 1L;

		when(lectureRepository.findLectureByIdForUpdate(any())).thenReturn(
			new Lecture("lecture_title01", LocalDateTime.now(), 2L, 30, 30));
		when(lectureRepository.existLectureApplyByStudentIdAndLectureId(any(), any())).thenReturn(true);

		// when, then
		assertThatThrownBy(() -> lectureManager.applyLecture(studentId, lectureId))
			.isInstanceOf(LectureDuplicatedRegistrationException.class)
			.extracting("errorType")
			.isInstanceOf(LectureErrorType.class)
			.satisfies(
				errorType -> assertThat(((LectureErrorType)errorType).getErrorCode()).isEqualTo(
					LectureErrorCode.LECTURE06));
	}

	@DisplayName("[성공] 강의 신청 성공 목록 조회 ")
	@Test
	void findAllApplySuccessByStudentId() {
		// given
		final Long studentId = 1L;

		when(lectureRepository.findAllApplySuccessByStudentId(any()))
			.thenReturn(List.of(
				new ApplySuccessResult(1L, "강의명1", "강연자1"),
				new ApplySuccessResult(2L, "강의명2", "강연자2"),
				new ApplySuccessResult(3L, "강의명3", "강연자3"),
				new ApplySuccessResult(4L, "강의명4", "강연자4"))
			);

		// when
		final List<ApplySuccessResult> applySuccessResults = lectureManager.findAllApplySuccessByStudentId(studentId);

		// then
		assertThat(applySuccessResults).hasSize(4);
	}

}
