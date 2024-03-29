package com.school.sba.entities;

import java.util.List;
import java.util.Set;

import com.school.sba.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
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
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int userId;
	
	@Column(unique = true)
	private String userName;
	private String firstName;
	private String lastName;
	private long contactNo;
	
	@Column(unique = true)
	private String email;
	private String password;
	
	@Enumerated(EnumType.STRING)
	private UserRole userRole;
	private boolean isDeleted;
	
	@ManyToOne
	private School school;
	
	@ManyToMany(mappedBy = "users",fetch=FetchType.EAGER)
	private List<AcademicProgram> academicPrograms;
	
	@ManyToOne
	private Subject subject; 
	
}







