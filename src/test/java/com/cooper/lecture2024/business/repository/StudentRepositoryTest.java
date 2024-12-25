package com.cooper.lecture2024.business.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.jdbc.Sql;

import com.cooper.lecture2024.business.dto.response.StudentQueryResult;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(value = {"com.cooper.lecture2024.common.jpa", "com.cooper.lecture2024.infra.rdb"})
@Sql(scripts = {"classpath:sql/students_sample.sql"})
class StudentRepositoryTest {

	@Autowired
	private StudentRepository studentRepository;

	@DisplayName("[실패] 학생 조회 실패")
	@Test
	void studentNotFound() {
		// given
		final Long studentId = 10000L;

		// when
		final StudentQueryResult studentQueryResult = studentRepository.findStudentQueryById(studentId);

		// then
		assertThat(studentQueryResult).isNull();
	}

	@DisplayName("[성공] 학생 조회 성공")
	@Test
	void findById() {
		// given
		final Long studentId = 1L;

		// when
		final StudentQueryResult studentQueryResult = studentRepository.findStudentQueryById(studentId);

		// then
		assertThat(studentQueryResult.id()).isEqualTo(1L);
	}
}
