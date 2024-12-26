package com.cooper.lecture2024.infra.rdb.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooper.lecture2024.domain.LectureApply;

public interface JpaLectureApplyRepository extends JpaRepository<LectureApply, Long> {
}
