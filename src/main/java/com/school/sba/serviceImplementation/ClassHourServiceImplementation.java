package com.school.sba.serviceImplementation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.school.sba.entities.ClassHour;
import com.school.sba.entities.Schedule;
import com.school.sba.entities.Subject;
import com.school.sba.entities.User;
import com.school.sba.enums.ClassStatus;
import com.school.sba.enums.UserRole;
import com.school.sba.exception.AcademicProgramNotFoundByIdException;
import com.school.sba.exception.ClassHourNotFoundByIdException;
import com.school.sba.exception.IllegalRequestException;
import com.school.sba.exception.ScheduleNotFoundByIdException;
import com.school.sba.exception.SubjectNotFoundByIdException;
import com.school.sba.exception.UserNotFoundByIdException;
import com.school.sba.repository.AcademicProgramRepository;
import com.school.sba.repository.ClassHourRepository;
import com.school.sba.repository.ScheduleRepository;
import com.school.sba.repository.SubjectRepository;
import com.school.sba.repository.UserRepository;
import com.school.sba.request_dto.ClassHourRequest;
import com.school.sba.response_dto.ClassHourResponse;
import com.school.sba.service.ClassHourService;
import com.school.sba.utility.ResponseStructure;

@Service
public class ClassHourServiceImplementation implements ClassHourService {
	
	@Autowired
	private AcademicProgramRepository academicProgramRepo;
	
	@Autowired
	private ScheduleRepository scheduleRepo;
	
	@Autowired
	private ClassHourRepository classHourRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private SubjectRepository subjectRepo;

//	private ClassHour mapRequestToClassHourObject(ClassHourRequest classHourRequest) {
//		return ClassHour.builder()
//				.classBeginsAt(classHourRequest.getClassBeginsAt())
//				.classEndsAt(classHourRequest.getClassEndsAt())
//				.classStatus(classHourRequest.getClassStatus())
//				.roomNo(0)
//				.build();
//	}
	
	@Override
	public ResponseEntity<ResponseStructure<String>> generateClassHours(int programId,
			ClassHourRequest classHourRequest) {
		return academicProgramRepo.findById(programId)
				.map(program->{
					if (program.getSchool().getSchedule()==null)
						throw new IllegalRequestException("Failed to generate ClassHours b/z Schedule is not created!!!" );
					System.out.println("program.getClassHours() -> "+
							program.getClassHours() +" "+ program.getClassHours().isEmpty());
					if (!program.getClassHours().isEmpty()) 
						throw new IllegalRequestException("Failed to generate ClassHours b/z ClassHours is already assigned!!!");
					Schedule schedule=program.getSchool().getSchedule();
					List<ClassHour> classHours = new ArrayList<>();
					ClassHour lastClass=null;
					LocalDate currenDate=program.getProgramBeginsAt();
					for (int day=1;day<=6;day++) {
						for (int cls=1;cls<=schedule.getClassHoursPerDay();cls++) {
							ClassHour curentClass = new ClassHour();
							if (cls==1) {
								curentClass.setClassBeginsAt(LocalDateTime.of(currenDate, schedule.getOpensAt()));
							} else {
								LocalTime lastClassEndTime = lastClass.getClassEndsAt().toLocalTime(); 
								if (lastClassEndTime.equals(schedule.getBreakTime())) {
									lastClassEndTime=lastClassEndTime.plus(schedule.getBreakLengthInMilSecs());
								}
								else if (lastClassEndTime.equals(schedule.getLunchTime())) {
									lastClassEndTime=lastClassEndTime.plus(schedule.getLunchLengthInMilSecs());
								}
								curentClass.setClassBeginsAt(LocalDateTime.of(currenDate,lastClassEndTime));
							}
							curentClass.setClassEndsAt(curentClass.getClassBeginsAt().plus(schedule.getClassHourLengthInMilSec()));
							curentClass.setAcademicProgram(program);
							curentClass.setClassStatus(ClassStatus.NOTSCHEDULED);
							curentClass = classHourRepo.save(curentClass);
							classHours.add(curentClass);
							lastClass=classHours.get(cls-1);
						}
						currenDate=currenDate.plusDays(1);
					}

						program.setClassHours(classHours);
						academicProgramRepo.save(program);

					ResponseStructure<String> structure = new ResponseStructure<>();
					structure.setStatus(HttpStatus.CREATED.value());
					structure.setMessage("class hour saved");
					structure.setData("Class Hours generated Succussfully!!!");
					return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.CREATED);
				})
				.orElseThrow(()-> new AcademicProgramNotFoundByIdException("Failed to generate ClassHours!!!"));
	}

	@Override
	public ResponseEntity<ResponseStructure<List<ClassHourResponse>>> updateClassHour(List<ClassHourRequest> classHourRequests) {
		if (classHourRequests==null || classHourRequests.isEmpty())
			throw new IllegalRequestException("Failed to update b/z ClassHourRequests is empty!!!");
		ResponseStructure<List<ClassHourResponse>> structure = new ResponseStructure<>();
		List<ClassHourResponse> classHourResponses =new ArrayList<>();
		classHourRequests.forEach(classHourRequest->{
			ClassHour classHour = classHourRepo.findById(classHourRequest.getClassHourId())
					.map(classhour->{
						
						Subject subject=subjectRepo.findById(classHourRequest.getSubjectId())
								.orElseThrow(()-> new SubjectNotFoundByIdException("Failed to Update ClassHour!!!"));
						
						User user=userRepo.findById(classHourRequest.getUserId()) 
								.map(user1->{
									if (!user1.getUserRole().equals(UserRole.TEACHER))
										throw new IllegalRequestException("Failed to Update ClassHour "+classHourRequest.getClassHourId()+" b/z User "+ user1.getUserId() +" is not Teacher!!!");
									if (!user1.getSubjects().contains(subject)) 
										throw new IllegalRequestException("Failed to Update ClassHour "+classHourRequest.getClassHourId()+" b/z "+user1.getUserRole()+
												" is not assigned with SubjectId: "+classHourRequest.getSubjectId());
									else 
										return user1;
								})
						.orElseThrow(()-> new UserNotFoundByIdException("Failed to Update ClassHour!!!"));
						
						if (!classhour.getAcademicProgram().getSubjects().contains(subject))
							throw new IllegalRequestException("Failed to Update ClassHour b/z Academic-Program of "+
									classHourRequest.getClassHourId()+" is not assigned with "+classHourRequest.getSubjectId());
						if(classhour.getClassStatus().equals(ClassStatus.SCHEDULED))
							throw new IllegalRequestException("Failed to Update ClassHour "+classHourRequest.getClassHourId()+" b/z ClassHour is Already Scheduled!!!");
						
						if (classHourRepo.existsByRoomNoAndClassBeginsAtBetween(
										classHourRequest.getRoomNoId(), classhour.getClassBeginsAt(), classhour.getClassEndsAt()) )
							throw new IllegalRequestException("Failed to Update ClassHour "+classHourRequest.getClassHourId()+" b/z roomNo is already assigned to classHour: "+classHourRequest.getClassHourId());
						
						classhour.setUser(user);
						classhour.setSubject(subject);
						classhour.setRoomNo(classHourRequest.getRoomNoId());
						classhour.setClassStatus(ClassStatus.SCHEDULED);
						
						ClassHourResponse classHourResponse = new  ClassHourResponse();
						classHourResponse.setClassHourId(classHourRequest.getClassHourId());
						classHourResponse.setUserId(classHourRequest.getUserId());
						classHourResponse.setSubjectId(classHourRequest.getSubjectId());
						classHourResponse.setRoomNo(classHourRequest.getRoomNoId());
						classHourResponses.add(classHourResponse);
						
						return classHourRepo.save(classhour);
						
					})
					.orElseThrow(()-> new ClassHourNotFoundByIdException("Failed to update ClassHour!!!"));
			
		}); 
		
		structure.setStatus(HttpStatus.OK.value());
		structure.setMessage("Successfully Updated Class Hour!!!");
		structure.setData(classHourResponses);
		return new ResponseEntity<ResponseStructure<List<ClassHourResponse>>>(structure,HttpStatus.OK);
		
	}

}












