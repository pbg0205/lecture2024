package com.cooper.lecture2024.infra.rdb.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cooper.lecture2024.domain.LectureApply;

public interface JpaLectureRepository extends JpaRepository<LectureApply, Long> {
}
