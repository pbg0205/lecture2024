package com.cooper.lecture2024.business;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cooper.lecture2024.business.errors.StudentErrorCode;
import com.cooper.lecture2024.business.errors.StudentErrorType;
import com.cooper.lecture2024.business.errors.exception.StudentNotFoundException;
import com.cooper.lecture2024.business.repository.StudentRepository;
import com.cooper.lecture2024.domain.Student;

@ExtendWith(MockitoExtension.class)
class StudentManagerTest {

	@InjectMocks
	private StudentManager studentManager;

	@Mock
	private StudentRepository studentRepository;

	@DisplayName("학생 조회 실패")
	@Test
	void notFoundStudent() {
		// given
		final String name = "학생 이름1";

		when(studentRepository.findById(anyLong()))
			.thenThrow(new StudentNotFoundException(StudentErrorType.STUDENT_NOT_FOUND));

		// when, then
		assertThatThrownBy(() -> studentManager.findById(1L)).isInstanceOf(StudentNotFoundException.class)
			.extracting("errorType")
			.isInstanceOf(StudentErrorType.class)
			.satisfies(
				errorType -> assertThat(((StudentErrorType)errorType).getErrorCode()).isEqualTo(
					StudentErrorCode.STUDENT01));
	}

	@DisplayName("학생 조회 성공")
	@Test
	void findById() {
		// given
		final String name = "학생 이름1";

		when(studentRepository.findById(anyLong())).thenReturn(new Student(name));

		// when
		final Student student = studentManager.findById(1L);

		// then
		assertThat(student).extracting("name").isEqualTo(name);
	}
}
