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
import lombok.NoArgsConstructor;

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

}
