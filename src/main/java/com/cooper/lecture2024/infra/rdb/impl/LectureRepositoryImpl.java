package com.cooper.lecture2024.infra.rdb.impl;

import static com.cooper.lecture2024.domain.QLecture.lecture;
import static com.cooper.lecture2024.domain.QLecturer.lecturer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.business.repository.LectureRepository;
import com.cooper.lecture2024.domain.Lecture;

@Repository
@RequiredArgsConstructor
public class LectureRepositoryImpl implements LectureRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public List<LectureQueryResult> findLecturesByStartAtBetween(
		final LocalDateTime startDateTime,
		final LocalDateTime deadLineToStart) {
		return jpaQueryFactory.select(Projections.constructor(
			LectureQueryResult.class,
			lecture.id, lecture.title, lecture.remainingCount, lecturer.name, lecture.startAt))
			.from(lecture)
			.innerJoin(lecturer).on(lecture.lecturerId.eq(lecturer.id))
			.where(lecture.startAt.between(startDateTime, deadLineToStart))
			.fetch();
	}

	@Override
	public Lecture findById(final Long lectureId) {
		return jpaQueryFactory.selectFrom(lecture)
			.where(lecture.id.eq(lectureId))
			.fetchOne();
	}
}
