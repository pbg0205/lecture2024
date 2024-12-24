package com.cooper.lecture2024.business.dto.response;

import java.time.LocalDateTime;

public record LectureQueryResult(Long lectureId,
								 String title,
								 Integer remainingCount,
								 String lecturer,
								 LocalDateTime startAt) {
}
