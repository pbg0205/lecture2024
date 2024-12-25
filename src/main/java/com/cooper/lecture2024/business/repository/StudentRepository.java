package com.cooper.lecture2024.business.repository;

import com.cooper.lecture2024.domain.Student;

public interface StudentRepository {
	Student findById(Long studentId);
}
