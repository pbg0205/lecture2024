package com.cooper.lecture2024.presentation;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.cooper.lecture2024.business.LectureApplyFacade;
import com.cooper.lecture2024.business.dto.response.ApplySuccessResult;
import com.cooper.lecture2024.business.dto.response.LectureApplyResult;
import com.cooper.lecture2024.business.dto.response.LectureQueryResult;
import com.cooper.lecture2024.business.errors.LectureErrorType;
import com.cooper.lecture2024.business.errors.StudentErrorType;
import com.cooper.lecture2024.business.errors.exception.LectureNotFoundException;
import com.cooper.lecture2024.business.errors.exception.LectureRegistrationClosedException;
import com.cooper.lecture2024.business.errors.exception.StudentNotFoundException;
import com.cooper.lecture2024.presentation.dto.request.LectureApplyRequest;

@WebMvcTest(LectureController.class)
class LectureControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockitoBean
	private LectureApplyFacade lectureApplyFacade;

	@DisplayName("[실패] 잘못된 검색 강의 포맷으로 인한 실패")
	@Test
	void invalidStartDateFormat() throws Exception {
		// given
		String startDate = "20241224";

		// when
		final ResultActions result = mockMvc.perform(get("/api/lectures/{startDate}", startDate)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.code").value("COMMON01"),
				jsonPath("$.message").value("요청 값을 지원하지 않거나 올바르지 않은 값입니다."))
			.andDo(print());
	}

	@DisplayName("[성공] 올바른 포팻(yyyy-MM-dd)으로 강의 시작 날짜과 일치하는 경우, 목록 반환")
	@Test
	void findLecturesByStartAtBetween() throws Exception {
		// given
		String startDate = "2024-12-24";

		when(lectureApplyFacade.findAllLectureQueryByStartAtBetween(any(), any()))
			.thenReturn(
				List.of(
					new LectureQueryResult(1L, "title01", 20, "강연자01", LocalDateTime.of(2024, 12, 24, 9, 0)),
					new LectureQueryResult(2L, "title02", 16, "강연자02", LocalDateTime.of(2024, 12, 24, 10, 0)),
					new LectureQueryResult(3L, "title02", 13, "강연자03", LocalDateTime.of(2024, 12, 24, 10, 30))
				)
			);

		// when
		final ResultActions result = mockMvc.perform(get("/api/lectures/{startDate}", startDate)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
				status().isOk(),
				jsonPath("$").isArray(),
				jsonPath("$", hasSize(3)),
				jsonPath("$[0].lectureId").value(1L),
				jsonPath("$[0].title").value("title01"),
				jsonPath("$[0].remainingCount").value(20),
				jsonPath("$[0].lecturerName").value("강연자01"),
				jsonPath("$[0].startAt").value("2024-12-24T09:00:00"))
			.andDo(print());
	}

	@DisplayName("[실패] 수강 신청 시, 학생 조회 실패")
	@Test
	void notFoundStudent() throws Exception {
		// given
		final LectureApplyRequest lectureApplyRequest = new LectureApplyRequest(1L, 1L);
		final String content = objectMapper.writeValueAsString(lectureApplyRequest);

		when(lectureApplyFacade.applyLecture(any(), any()))
			.thenThrow(new StudentNotFoundException(StudentErrorType.STUDENT_NOT_FOUND));

		// when
		final ResultActions result = mockMvc.perform(post("/api/lectures/apply")
			.contentType(MediaType.APPLICATION_JSON)
			.content(content));

		// then
		result.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.code").value("STUDENT01"),
				jsonPath("$.message").value("해당 학생을 찾을 수 없습니다."))
			.andDo(print());
	}

	@DisplayName("[실패] 수강 신청 시, 강의 조회 실패")
	@Test
	void notFoundLectureWhenLectureApply() throws Exception {
		// given
		final LectureApplyRequest lectureApplyRequest = new LectureApplyRequest(1L, 1L);
		final String content = objectMapper.writeValueAsString(lectureApplyRequest);

		when(lectureApplyFacade.applyLecture(any(), any()))
			.thenThrow(new LectureNotFoundException(LectureErrorType.LECTURE_NOT_FOUND));

		// when
		final ResultActions result = mockMvc.perform(post("/api/lectures/apply")
			.contentType(MediaType.APPLICATION_JSON)
			.content(content));

		// then
		result.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.code").value("LECTURE02"),
				jsonPath("$.message").value("강의를 찾을 수 없습니다."))
			.andDo(print());
	}

	@DisplayName("[실패] 수강 신청 마감")
	@Test
	void closeLectureRegistration() throws Exception {
		// given
		final LectureApplyRequest lectureApplyRequest = new LectureApplyRequest(1L, 1L);
		final String content = objectMapper.writeValueAsString(lectureApplyRequest);

		when(lectureApplyFacade.applyLecture(any(), any()))
			.thenThrow(new LectureRegistrationClosedException(LectureErrorType.LECTURE_REGISTRATION_CLOSED));

		// when
		final ResultActions result = mockMvc.perform(post("/api/lectures/apply")
			.contentType(MediaType.APPLICATION_JSON)
			.content(content));

		// then
		result.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.code").value("LECTURE05"),
				jsonPath("$.message").value("해당 강의의 수강 신청이 마감되었습니다."))
			.andDo(print());
	}

	@DisplayName("[성공] 수강 신청 완료")
	@Test
	void lectureApply() throws Exception {
		// given
		final LectureApplyRequest lectureApplyRequest = new LectureApplyRequest(1L, 1L);
		final String content = objectMapper.writeValueAsString(lectureApplyRequest);

		when(lectureApplyFacade.applyLecture(any(), any()))
			.thenReturn(new LectureApplyResult("학생 이름1", "강의명1"));

		// when
		final ResultActions result = mockMvc.perform(post("/api/lectures/apply")
			.contentType(MediaType.APPLICATION_JSON)
			.content(content));

		// then
		result.andExpectAll(
				status().isOk(),
				jsonPath("$.studentName").value("학생 이름1"),
				jsonPath("$.lectureName").value("강의명1"))
			.andDo(print());
	}

	@DisplayName("[실패] 학생 수강 성공 조회시, 학생 조회 실패")
	@Test
	void notFoundStudentWhenApplySuccess() throws Exception {
		// given
		when(lectureApplyFacade.findAllApplySuccessByStudentId(any()))
			.thenThrow(new StudentNotFoundException(StudentErrorType.STUDENT_NOT_FOUND));

		// when
		final ResultActions result = mockMvc.perform(get("/api/lectures/students/{studentId}/success", 1L)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
				status().isBadRequest(),
				jsonPath("$.code").value("STUDENT01"),
				jsonPath("$.message").value("해당 학생을 찾을 수 없습니다."))
			.andDo(print());
	}

	@DisplayName("[성공] 학생 수강 성공 조회")
	@Test
	void findAllApplySuccessByStudentId() throws Exception {
		// given
		when(lectureApplyFacade.findAllApplySuccessByStudentId(any()))
			.thenReturn(List.of(
				new ApplySuccessResult(1L, "강의명1", "강연자1"),
				new ApplySuccessResult(2L, "강의명2", "강연자2"),
				new ApplySuccessResult(3L, "강의명3", "강연자3"),
				new ApplySuccessResult(4L, "강의명4", "강연자4")
			));

		// when
		final ResultActions result = mockMvc.perform(get("/api/lectures/students/{studentId}/success", 1L)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
				status().isOk(),
				jsonPath("$").isArray(),
				jsonPath("$", hasSize(4)),
				jsonPath("$[0].lectureId").value(1L),
				jsonPath("$[0].title").value("강의명1"),
				jsonPath("$[0].lecturerName").value("강연자1"))
			.andDo(print());
	}
}
