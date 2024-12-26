package com.cooper.lecture2024.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(uniqueConstraints = @UniqueConstraint(name = "lecture_apply_uk", columnNames = {"student_id", "lecture_id"}))
public class LectureApply {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private Long studentId;

	@Column(nullable = false)
	private Long lectureId;

	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "timestamp")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "timestamp")
	private LocalDateTime modifiedAt;

	public LectureApply(final Long studentId, final Long lectureId) {
		this.studentId = studentId;
		this.lectureId = lectureId;
	}
}
