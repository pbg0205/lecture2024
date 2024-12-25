package com.cooper.lecture2024.business;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.dto.response.StudentQueryResult;
import com.cooper.lecture2024.business.errors.StudentErrorType;
import com.cooper.lecture2024.business.errors.exception.StudentNotFoundException;
import com.cooper.lecture2024.business.repository.StudentRepository;
import com.cooper.lecture2024.common.annotations.Manager;

@Manager
@RequiredArgsConstructor
public class StudentManager {

	private final StudentRepository studentRepository;

	public StudentQueryResult findStudentQueryById(final Long id) {
		return Optional.ofNullable(studentRepository.findStudentQueryById(id))
			.orElseThrow(() -> new StudentNotFoundException(StudentErrorType.STUDENT_NOT_FOUND));
	}
}
