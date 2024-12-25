package com.cooper.lecture2024.infra.rdb.impl;

import static com.cooper.lecture2024.domain.QStudent.student;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.repository.StudentRepository;
import com.cooper.lecture2024.domain.Student;

@Repository
@RequiredArgsConstructor
public class StudentRepositoryImpl implements StudentRepository {

	private final JPAQueryFactory jpaQueryFactory;

	@Override
	public Student findById(final Long studentId) {
		return jpaQueryFactory.selectFrom(student)
			.where(student.id.eq(studentId))
			.fetchOne();
	}
}
