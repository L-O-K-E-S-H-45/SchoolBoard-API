package com.school.sba.entities;

import java.time.LocalDateTime;

import com.school.sba.enums.ClassStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClassHour {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int classHourId;
	private LocalDateTime classBeginsAt;
	private LocalDateTime classEndsAt;
	private int roomNo;
	@Enumerated(EnumType.STRING)
	private ClassStatus classStatus;
	
	@ManyToOne  // here we cannot give mapped by
	private AcademicProgram academicProgram;
	
	@ManyToOne
	private User user;
	
	@ManyToOne
	private Subject subject;

}
