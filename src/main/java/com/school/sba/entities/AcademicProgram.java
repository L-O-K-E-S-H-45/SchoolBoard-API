package com.school.sba.entities;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.school.sba.enums.ProgramType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class AcademicProgram {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int programId;
	
	@Enumerated(EnumType.STRING)
	private ProgramType programType;
	@Column(unique = true)
	private String programName;
//	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate programBeginsAt;
//	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate programEndsAt;
	
	@ManyToOne
	private School school;
	
	@OneToMany(mappedBy = "academicProgram")
	private List<User> usersList;
	
	@ManyToMany
	private List<Subject> subjects;
	
}
