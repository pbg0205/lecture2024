package com.cooper.lecture2024.infra.rdb.impl;

import static com.cooper.lecture2024.domain.QStudent.student;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.dto.response.StudentQueryResult;
import com.cooper.lecture2024.business.repository.StudentRepository;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public StudentQueryResult findStudentQueryById(final Long studentId) {
		return jpaQueryFactory.select(Projections.constructor(
			StudentQueryResult.class,
				student.id,
				student.name))
			.from(student)
			.where(student.id.eq(studentId))
			.fetchOne();
	}
}
