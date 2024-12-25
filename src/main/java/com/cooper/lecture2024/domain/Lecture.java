package com.cooper.lecture2024.domain;

import java.time.LocalDateTime;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import com.cooper.lecture2024.business.errors.LectureErrorType;
import com.cooper.lecture2024.business.errors.exception.LectureConstraintsViolationException;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Lecture {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 200)
	private String title;

	@Column(nullable = false, columnDefinition = "timestamp")
	private LocalDateTime startAt;

	@Column(nullable = false)
	private Long lecturerId;

	@ColumnDefault("30")
	@Column(nullable = false, columnDefinition = "INTEGER CHECK (remaining_count >= 0)")
	private Integer remainingCount;

	@ColumnDefault("30")
	@Column(nullable = false)
	private Integer capacity;

	@CreationTimestamp
	@Column(nullable = false, columnDefinition = "timestamp")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(nullable = false, columnDefinition = "timestamp")
	private LocalDateTime modifiedAt;

	public Lecture(final String title, final LocalDateTime startAt, final Long lecturerId, final Integer remainingCount,
		final Integer capacity) {
		validateRemainingCountNegative(remainingCount);
		validateRemainingCountLessThanCapacity(remainingCount, capacity);
		this.title = title;
		this.startAt = startAt;
		this.lecturerId = lecturerId;
		this.remainingCount = remainingCount;
		this.capacity = capacity;
	}

	private void validateRemainingCountLessThanCapacity(final Integer remainingCount, final Integer capacity) {
		if (remainingCount > capacity) {
			throw new LectureConstraintsViolationException(LectureErrorType.GREATER_THAN_CAPACITY);
		}
	}

	private void validateRemainingCountNegative(final Integer remainingCount) {
		if (remainingCount < 0) {
			throw new LectureConstraintsViolationException(LectureErrorType.REMAINING_COUNT_NEGATIVE);
		}
	}

	public Integer decreaseRemainingCount() {
		this.remainingCount = this.remainingCount - 1;
		return remainingCount;
	}
}
