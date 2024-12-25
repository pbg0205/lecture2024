package com.cooper.lecture2024.integration;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.cooper.lecture2024.presentation.dto.request.LectureApplyRequest;

@SpringBootTest
@AutoConfigureMockMvc
@Sql({"classpath:sql/lecture_sample.sql", "classpath:sql/lecturer_sample.sql",
	"classpath:sql/students_sample.sql", "classpath:sql/lecture_apply_sample.sql"})
@Transactional
public class LectureIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@DisplayName("[성공] 날짜별 수강 신청 목록 조회")
	@Test
	void findLecturesByStartAtBetween() throws Exception {
		// given
		String startDate = "2024-12-24";

		// when
		final ResultActions result = mockMvc.perform(get("/api/lectures/{startDate}", startDate)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
				status().isOk(),
				jsonPath("$").isArray(),
				jsonPath("$", hasSize(4)))
			.andDo(print());
	}

	@DisplayName("[성공] 수강 신청 완료")
	@Test
	void lectureApply() throws Exception {
		// given
		final LectureApplyRequest lectureApplyRequest = new LectureApplyRequest(1L, 1L);
		final String content = objectMapper.writeValueAsString(lectureApplyRequest);

		// when
		final ResultActions result = mockMvc.perform(post("/api/lectures/apply")
			.contentType(MediaType.APPLICATION_JSON)
			.content(content));

		// then
		result.andExpectAll(
				status().isOk(),
				jsonPath("$.studentName").exists(),
				jsonPath("$.lectureName").exists())
			.andDo(print());
	}

	@DisplayName("[성공] 학생 수강 성공 조회")
	@Test
	void findAllApplySuccessByStudentId() throws Exception {
		// given
		final Long studentId = 1L;

		// when
		final ResultActions result = mockMvc.perform(get("/api/lectures/students/{studentId}/success", studentId)
			.contentType(MediaType.APPLICATION_JSON));

		// then
		result.andExpectAll(
				status().isOk(),
				jsonPath("$").isArray(),
				jsonPath("$", hasSize(5)))
			.andDo(print());
	}
}
