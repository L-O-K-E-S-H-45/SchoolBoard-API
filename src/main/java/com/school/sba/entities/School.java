package com.school.sba.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@Data
@Builder
public class School {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int schoolId;
	private String schoolName;
	private long schoolContactNo;
	private String schoolEmail;
	private String schoolAddress;
	
	@OneToOne(mappedBy = "school")
	private Schedule schedule;
	

}












