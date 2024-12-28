package com.cooper.lecture2024.infra.rdb.impl;

import static com.cooper.lecture2024.domain.QLecture.lecture;
import static com.cooper.lecture2024.domain.QLectureApply.lectureApply;
import static com.cooper.lecture2024.domain.QLecturer.lecturer;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.dto.response.ApplySuccessResult;
import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.business.errors.LectureErrorType;
import com.cooper.lecture2024.business.errors.exception.LectureDuplicatedRegistrationException;
import com.cooper.lecture2024.business.repository.LectureRepository;
import com.cooper.lecture2024.domain.Lecture;
import com.cooper.lecture2024.domain.LectureApply;
import com.cooper.lecture2024.infra.rdb.jpa.JpaLectureApplyRepository;

@Repository
@RequiredArgsConstructor
@Transactional
public class LectureRepositoryImpl implements LectureRepository {

	private final JPAQueryFactory jpaQueryFactory;
	private final JpaLectureApplyRepository jpaLectureApplyRepository;

	@Override
	public List<LectureQueryResult> findAllLectureQueryByStartAtBetween(
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
	public Lecture findLectureByIdForUpdate(final Long lectureId) {
		return jpaQueryFactory.selectFrom(lecture)
			.where(lecture.id.eq(lectureId))
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)
			.fetchOne();
	}

	@Override
	public LectureApply saveLectureApply(final Long studentId, final Long lectureId) {
		try {
			return jpaLectureApplyRepository.save(new LectureApply(studentId, lectureId));
		} catch (final DataIntegrityViolationException exception) {
			throw new LectureDuplicatedRegistrationException(LectureErrorType.LECTURE_DUPLICATED_REGISTRATION);
		}
	}

	@Override
	public List<ApplySuccessResult> findAllApplySuccessByStudentId(final Long studentId) {
		return jpaQueryFactory.select(Projections.constructor(
				ApplySuccessResult.class,
				lecture.id,
				lecture.title,
				lecturer.name))
			.from(lecture)
			.innerJoin(lectureApply).on(lecture.id.eq(lectureApply.lectureId))
			.innerJoin(lecturer).on(lecturer.id.eq(lecture.lecturerId))
			.where(lectureApply.studentId.eq(studentId))
			.fetch();
	}

	@Override
	public boolean existLectureApplyByStudentIdAndLectureId(final Long studentId, final Long lectureId) {
		return jpaQueryFactory.select(lectureApply.studentId, lectureApply.lectureId)
			.from(lectureApply)
			.where(lectureApply.studentId.eq(studentId).and(lectureApply.lectureId.eq(lectureId)))
			.setLockMode(LockModeType.PESSIMISTIC_WRITE)
			.fetchFirst() != null;
	}
}
