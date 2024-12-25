package com.cooper.lecture2024.business.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.domain.Lecture;
import com.cooper.lecture2024.domain.LectureApply;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(value = {"com.cooper.lecture2024.common.jpa", "com.cooper.lecture2024.infra.rdb"})
class LectureRepositoryTest {

	@Autowired
	private LectureRepository lectureRepository;

	@Test
	@DisplayName("[성공] 검색 날짜와 일치하는 강의 존재시, 목록 반환")
	@Sql(scripts = {"classpath:sql/lecture_sample.sql", "classpath:sql/lecturer_sample.sql"})
	void findLecturesByStartAtBetween() {
		// given
		final LocalDateTime startDateTime = LocalDateTime.of(2024, 12, 24, 0, 0);
		final LocalDateTime deadLineToStart = startDateTime.plusDays(1).minusNanos(1);

		// when
		final List<LectureQueryResult> lectureQueryResultList =
			lectureRepository.findLecturesByStartAtBetween(startDateTime, deadLineToStart);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(lectureQueryResultList.size()).isEqualTo(4);
			for (LectureQueryResult lectureQueryResult : lectureQueryResultList) {
				softAssertions.assertThat(lectureQueryResult.startAt().getMonth().getValue()).isEqualTo(12);
				softAssertions.assertThat(lectureQueryResult.startAt().getDayOfMonth()).isEqualTo(24);
			}
		});
	}

	@Test
	@DisplayName("[성공] 검색 날짜와 일치하는 강의 미존재시, 빈 값 반환")
	@Sql("classpath:sql/lecturer_sample.sql")
	void LecturesZeroByStartTimeAtBetween() {
		// given
		final LocalDateTime startDateTime = LocalDateTime.of(2024, 12, 30, 0, 0);
		final LocalDateTime deadLineToStart = startDateTime.plusDays(1).minusNanos(1);

		// when
		final List<LectureQueryResult> lectureQueryResultList =
			lectureRepository.findLecturesByStartAtBetween(startDateTime, deadLineToStart);

		// then
		assertThat(lectureQueryResultList.size()).isEqualTo(0);
	}

	@Test
	@DisplayName("[성공] 강의 식별자를 통해 강의 조회")
	@Sql("classpath:sql/lecture_sample.sql")
	void findById() {
		// given
		final long lectureId = 1L;

		// when
		final Lecture lecture = lectureRepository.findById(lectureId);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(lecture.getId()).isEqualTo(lectureId);
			softAssertions.assertThat(lecture.getRemainingCount()).isNotNegative();
			softAssertions.assertThat(lecture.getRemainingCount()).isLessThanOrEqualTo(lecture.getCapacity());
		});
	}

	@Test
	@DisplayName("[성공] 강의 신청 데이터 저장 성공")
	void saveLectureApply() {
		// given
		final Long studentId = 1L;
		final Long lectureId = 1L;

		// when
		final LectureApply lectureApply = lectureRepository.saveLectureApply(studentId, lectureId);

		// then
		assertSoftly(softAssertions -> {
			softAssertions.assertThat(lectureApply.getStudentId()).isEqualTo(studentId);
			softAssertions.assertThat(lectureApply.getLectureId()).isEqualTo(lectureId);
		});
	}

}
