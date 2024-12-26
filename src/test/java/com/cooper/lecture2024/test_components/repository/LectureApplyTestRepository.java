package com.cooper.lecture2024.test_components.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooper.lecture2024.domain.LectureApply;

public interface LectureApplyTestRepository extends JpaRepository<LectureApply, Long> {
	List<LectureApply> findLectureAppliesByLectureId(Long lectureId);

	List<LectureApply> findLectureAppliesByStudentIdAndLectureId(Long studentId, Long lectureId);
}
