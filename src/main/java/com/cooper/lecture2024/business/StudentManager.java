package com.cooper.lecture2024.business;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.errors.StudentErrorType;
import com.cooper.lecture2024.business.errors.exception.StudentNotFoundException;
import com.cooper.lecture2024.business.repository.StudentRepository;
import com.cooper.lecture2024.common.annotations.Manager;
import com.cooper.lecture2024.domain.Student;

@Manager
@RequiredArgsConstructor
public class StudentManager {

	private final StudentRepository studentRepository;

	public Student findById(final Long id) {
		return Optional.ofNullable(studentRepository.findById(id))
			.orElseThrow(() -> new StudentNotFoundException(StudentErrorType.STUDENT_NOT_FOUND));
	}
}
