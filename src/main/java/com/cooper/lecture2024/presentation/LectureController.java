package com.cooper.lecture2024.presentation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.cooper.lecture2024.business.LectureApplyFacade;
import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.presentation.dto.response.LectureQueryResponse;

@RestController
@RequestMapping("/api/lectures")
@RequiredArgsConstructor
public class LectureController {

	private final LectureApplyFacade lectureApplyFacade;

	@GetMapping("/{startAt}")
	public ResponseEntity<List<LectureQueryResponse>> findLecturesByStartAtBetween(
		@PathVariable(name = "startAt") @DateTimeFormat(pattern = "yyyy-MM-dd") final LocalDate lectureStartDate) {
		LocalDateTime startDateTime = lectureStartDate.atStartOfDay();
		LocalDateTime deadLineOfStart = startDateTime.plusDays(1).minusNanos(1);

		final List<LectureQueryResult> lectureQueryResults =
			lectureApplyFacade.findLecturesByStartAtBetween(startDateTime, deadLineOfStart);

		final List<LectureQueryResponse> lectureQueryResponses =
			lectureQueryResults.stream().map(LectureQueryResponse::from).toList();

		return ResponseEntity.ok().body(lectureQueryResponses);
	}
}
