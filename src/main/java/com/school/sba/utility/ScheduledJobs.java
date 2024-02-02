package com.school.sba.utility;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.school.sba.serviceImplementation.AcademicProgramServiceImplementation;
import com.school.sba.serviceImplementation.ClassHourServiceImplementation;
import com.school.sba.serviceImplementation.SchoolServiceimplementaion;
import com.school.sba.serviceImplementation.UserServiceImplemetation;

@Component
public class ScheduledJobs {
	
	@Autowired
	private UserServiceImplemetation userServiceImplemetation;
	
	@Autowired
	private AcademicProgramServiceImplementation academicProgramServiceImplementation;
	
	@Autowired
	private SchoolServiceimplementaion schoolServiceimplementaion;
	
	@Autowired
	private ClassHourServiceImplementation classHourServiceImplementation;
	
	
//	@Scheduled(fixedDelay = 5000L) // Time in milliSeconds
	private void  testSchedule() {
		System.out.println("Scheduled Jobs -> testSchedule() method");
		
		userServiceImplemetation.permanentlyDeleteUser();
		
		academicProgramServiceImplementation.permanentlydeleteAcademyPrpogram();
		
		schoolServiceimplementaion.permanentlyDeleteSchool();
		
	}
	/** @Scheduled(cron = "* * * * * *")
	 * * -> seconds = 0 to 59
	 * * -> minutes = 0 to 59
	 * * -> hours = 0 to 23
	 * * -> date = 1 to 31
	 * * -> month = 1 to 12
	 * * -> day of week = 1 to 7
	 */
	
	@Scheduled(cron = "0 0 0 * * 1")
	public void callAutoRepeatSchedule() {
		classHourServiceImplementation.autoRepeatSchedule();
	}
	
	@Scheduled(cron = "0 24 20 * * 5")
	public void m1() {
		System.out.println("Wel come");
	}
	
	// if Scheduled method is not working & to know whether Bean(Object) is created or not
//	public ScheduledJobs() {
//		System.out.println("Wel come");
//	}
	

}
