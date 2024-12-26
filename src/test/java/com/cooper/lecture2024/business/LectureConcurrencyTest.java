package com.cooper.lecture2024.business;

import static org.assertj.core.api.SoftAssertions.assertSoftly;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.lecture2024.business.repository.LectureRepository;
import com.cooper.lecture2024.test_components.repository.LectureApplyTestRepository;
import com.cooper.lecture2024.domain.Lecture;
import com.cooper.lecture2024.domain.LectureApply;

@SpringBootTest
@Sql({"classpath:sql/lecture_sample.sql", "classpath:sql/lecturer_sample.sql", "classpath:sql/students_sample.sql"})
class LectureConcurrencyTest {

	@Autowired
	private LectureApplyFacade lectureApplyFacade;

	@Autowired
	private LectureRepository lectureRepository;

	@Autowired
	private LectureApplyTestRepository lectureApplyTestRepository;

	@Test
	@DisplayName("[성공] 40명 신청해도 30명만 신청 가능")
	void closeLectureRegistration() throws InterruptedException {
		// given
		final Long lectureId = 3L;

		// when
		ExecutorService executorService = Executors.newFixedThreadPool(40);

		AtomicLong studentId = new AtomicLong();

		for (int i = 0; i < 40; i++) {
			executorService.submit(() -> lectureApplyFacade.applyLecture(studentId.incrementAndGet(), lectureId));
		}

		executorService.shutdown();
		executorService.awaitTermination(1, TimeUnit.MINUTES);

		// then
		final Lecture lecture = lectureRepository.findLectureByIdForUpdate(lectureId);
		final List<LectureApply> lectureApplies = lectureApplyTestRepository.findLectureAppliesByLectureId(lectureId);

		assertSoftly(softAssertions -> {
			softAssertions.assertThat(lecture.getRemainingCount()).isZero();
			softAssertions.assertThat(lectureApplies).hasSize(30);
		});
	}
}