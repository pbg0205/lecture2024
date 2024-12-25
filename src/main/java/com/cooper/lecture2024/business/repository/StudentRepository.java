package com.cooper.lecture2024.business.repository;

import com.cooper.lecture2024.business.dto.response.StudentQueryResult;

public interface StudentRepository {
	StudentQueryResult findStudentQueryById(Long studentId);
}
