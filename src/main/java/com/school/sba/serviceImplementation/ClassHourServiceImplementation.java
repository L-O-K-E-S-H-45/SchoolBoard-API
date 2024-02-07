package com.school.sba.serviceImplementation;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.school.sba.entities.AcademicProgram;
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
import com.school.sba.request_dto.ExcelRequestDto;
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
	
	@Autowired
	private ResponseStructure<String> structure;

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
//					if (!program.getClassHours().isEmpty()) 
//						throw new IllegalRequestException("Failed to generate ClassHours b/z ClassHours is already assigned!!!");
					Schedule schedule=program.getSchool().getSchedule();
					List<ClassHour> classHours = new ArrayList<>();
					ClassHour lastClass=null;
					LocalDate currenDate=program.getProgramBeginsAt();
					
					DayOfWeek dayOfWeek = currenDate.getDayOfWeek();
					System.out.println("Start Day -> "+dayOfWeek);

					int noOfClasses=6;
					
					if(!currenDate.getDayOfWeek().equals(DayOfWeek.MONDAY))
						noOfClasses=noOfClasses+7-currenDate.getDayOfWeek().getValue();
					
					for (int day=1;day<=noOfClasses;day++) {
						if (currenDate.getDayOfWeek().equals(DayOfWeek.SUNDAY))
							currenDate=currenDate.plusDays(1);
						
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
									if (!user1.getSubject().equals(subject)) 
										throw new IllegalRequestException("Failed to Update ClassHour "+classHourRequest.getClassHourId()+" b/z "+user1.getUserRole()+
												" is not assigned with SubjectId: "+classHourRequest.getSubjectId());
									else 
										return user1;
								})
						.orElseThrow(()-> new UserNotFoundByIdException("Failed to Update ClassHour!!!"));
						
						if (classhour.getAcademicProgram().getUsers()==null || !classhour.getAcademicProgram().getUsers().contains(user))
							throw new IllegalRequestException("Failed to Update ClassHour b/z Academic-Program of "+
									classHourRequest.getClassHourId()+" is not assigned with Teacher-"+classHourRequest.getUserId());
						if (!classhour.getAcademicProgram().getSubjects().contains(subject))
							throw new IllegalRequestException("Failed to Update ClassHour b/z Academic-Program of "+
									classHourRequest.getClassHourId()+" is not assigned with Subject-"+classHourRequest.getSubjectId());
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
	
// This is to autoRepeatSchedule if autoRepeatSchedule is true based on Scheduled time
	public void autoRepeatSchedule() {
		System.out.println("START ----------");
		List<AcademicProgram> academicPrograms = academicProgramRepo.findByAutoRepeatScheduleTrue();
		
		if (!academicPrograms.isEmpty()) {
			academicPrograms.forEach(academy->{
				if (!classHourRepo.existsByAcademicProgram(academy)) // shall we call generateClassHour() method
					throw new IllegalRequestException("Failed to Auto Repeat Time Table b/z for "
							+ "AcademicProgram-"+academy.getProgramId()+" ClassHours are not generated");
				
				int classHoursPerDay=academy.getSchool().getSchedule().getClassHoursPerDay();
				
				List<ClassHour> classHours = classHourRepo.findLastNRecordsByAcademicProgram(academy,classHoursPerDay*6);
				
				Collections.reverse(classHours);
				
				classHours.forEach(ch->{
					ClassHour classHour = new ClassHour();
					classHour.setClassBeginsAt(ch.getClassBeginsAt().plusDays(7));
					classHour.setClassEndsAt(ch.getClassEndsAt().plusDays(7));
					classHour.setAcademicProgram(academy);
					classHour.setClassStatus(ClassStatus.SCHEDULED);
					classHour.setRoomNo(ch.getRoomNo());
					classHour.setSubject(ch.getSubject());
					classHour.setUser(ch.getUser());
					classHourRepo.save(classHour);
				});
				
			});
		}
		System.out.println("END--------------");
	}


	@Override  // This is to autoRepeatTimeTable by Client when autorepeatSchedule is false
	public ResponseEntity<ResponseStructure<String>> autoRepeatTimeTable(int programId) {
		
		AcademicProgram academicProgram = academicProgramRepo.findById(programId).orElseThrow(()-> new AcademicProgramNotFoundByIdException("Failed to Auto Repeat Time Table"));
		
		if (!classHourRepo.existsByAcademicProgram(academicProgram)) // shall we call generateClassHour() method
			throw new IllegalRequestException("Failed to Auto Repeat Time Table b/z for "
					+ "AcademicProgram-"+programId+" ClassHours are not generated");
		
		int classHoursPerDay=academicProgram.getSchool().getSchedule().getClassHoursPerDay();
		
		List<ClassHour> classHours = classHourRepo.findLastNRecordsByAcademicProgram(academicProgram,classHoursPerDay*6);
		
		Collections.reverse(classHours);
		
		classHours.forEach(ch->{
			ClassHour classHour = new ClassHour();
			classHour.setClassBeginsAt(ch.getClassBeginsAt().plusDays(7));
			classHour.setClassEndsAt(ch.getClassEndsAt().plusDays(7));
			classHour.setAcademicProgram(academicProgram);
			classHour.setClassStatus(ClassStatus.SCHEDULED);
			classHour.setRoomNo(ch.getRoomNo());
			classHour.setSubject(ch.getSubject());
			classHour.setUser(ch.getUser());
			classHourRepo.save(classHour);
		});

		System.out.println("------END-------");
		
		structure.setStatus(HttpStatus.CREATED.value());
		structure.setMessage("Auto Repeat ClassHour Time Table for Scheduled is Successfull");
		structure.setData("Successfully created ClassHours");
		
		return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.CREATED);
	}

	@Override  // works only for standalone application
	public ResponseEntity<ResponseStructure<String>> writeToExcell(int programId, ExcelRequestDto excelRequestDto) {
		return academicProgramRepo.findById(programId)
				.map(program->{
					if (program.isDeleted())
						throw new IllegalRequestException("Failed to write data to excel b/z program is deleted");
					
					XSSFWorkbook workbook = new XSSFWorkbook();
					Sheet sheet = workbook.createSheet();
					
					int rowNumber=0;
					Row header = sheet.createRow(0);
					header.createCell(0).setCellValue("Date");
					header.createCell(1).setCellValue("BeginsAt");
					header.createCell(2).setCellValue("EndsAt");
					header.createCell(3).setCellValue("Subject");
					header.createCell(4).setCellValue("Teacher");
					header.createCell(5).setCellValue("RoomNo");
					
					LocalDateTime from = excelRequestDto.getFromDate().atTime(LocalTime.MIDNIGHT);
					LocalDateTime to = excelRequestDto.getToDate().atTime(LocalTime.MIDNIGHT).plusDays(1);
					
					List<ClassHour> classHours = classHourRepo.findAllByAcademicProgramAndClassBeginsAtBetween(program, from, to);       
					
					DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:MM");
					DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					
					for (ClassHour classHour : classHours) {
						Row row = sheet.createRow(++rowNumber);
						row.createCell(0).setCellValue(dateFormatter.format(classHour.getClassBeginsAt()));
						row.createCell(1).setCellValue(timeFormatter.format(classHour.getClassBeginsAt()));
						row.createCell(2).setCellValue(timeFormatter.format(classHour.getClassEndsAt()));
						if (classHour.getSubject()==null)
							row.createCell(3).setCellValue("");
						else
							row.createCell(3).setCellValue(classHour.getSubject().getSubjectName());
						if (classHour.getUser()==null)
							row.createCell(4).setCellValue("");
						else
							row.createCell(4).setCellValue(classHour.getUser().getUserName());
						row.createCell(5).setCellValue(classHour.getRoomNo());
					}
					
					try {
						workbook.write(new FileOutputStream(excelRequestDto.getFilePath()+"\\classhour_prid-"+programId+"_from-"+dateFormatter.format(classHours.get(0).getClassBeginsAt())+".xlsx"));
//						workbook.write(new FileOutputStream(excelRequestDto.getFilePath()+"\\classhour.xlsx"));
					} catch (FileNotFoundException e) {
						throw new IllegalRequestException(e.getMessage());
					} catch (IOException e) {
						throw new IllegalRequestException(e.getMessage());
					}
					
					structure.setStatus(HttpStatus.OK.value());
					structure.setMessage("Successfully fetched and written to excel");
					structure.setData("Write to Excel is successfull!!!");
					return new ResponseEntity<ResponseStructure<String>>(structure,HttpStatus.OK);
					
				})
				.orElseThrow(()-> new AcademicProgramNotFoundByIdException("Failed to write data to excel"));
	}

	@Override // This works for web Applications
	public ResponseEntity<?> writeToExcel(int programId, LocalDate fromDate, LocalDate toDate, MultipartFile file) throws IOException{
		return academicProgramRepo.findById(programId)
				.map(program->{
					if (program.isDeleted())
						throw new IllegalRequestException("Failed to write data to excel b/z program is deleted");
					
					LocalDateTime from = fromDate.atTime(LocalTime.MIDNIGHT);
					LocalDateTime to = toDate.atTime(LocalTime.MIDNIGHT).plusDays(1);
					
					List<ClassHour> classHours = classHourRepo.findAllByAcademicProgramAndClassBeginsAtBetween(program, from, to);
					
					if (classHours==null)
						throw new IllegalRequestException("Failed to write data to excel b/z there is no classhours");
					
					DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:MM");
					DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy:MM:dd");
					
					XSSFWorkbook workbook=null;
					try {
						workbook = new XSSFWorkbook(file.getInputStream());
					} catch (IOException e) {
						throw new IllegalRequestException(e.getMessage());
					}
					
					workbook.forEach(sheet->{
						int rowNumber = 0;
						Row header = sheet.createRow(rowNumber);
						header.createCell(0).setCellValue("Date");
						header.createCell(1).setCellValue("BeginsAt");
						header.createCell(2).setCellValue("EndsAt");
						header.createCell(3).setCellValue("Subject");
						header.createCell(4).setCellValue("Teacher");
						header.createCell(5).setCellValue("RoomNo");
						
						for (ClassHour classHour : classHours) {
							Row row = sheet.createRow(++rowNumber);
							row.createCell(0).setCellValue(dateFormatter.format(classHour.getClassBeginsAt()));
							row.createCell(1).setCellValue(timeFormatter.format(classHour.getClassBeginsAt()));
							row.createCell(2).setCellValue(timeFormatter.format(classHour.getClassEndsAt()));
							if (classHour.getSubject()==null)
								row.createCell(3).setCellValue("");
							else
								row.createCell(3).setCellValue(classHour.getSubject().getSubjectName());
							if (classHour.getUser()==null)
								row.createCell(4).setCellValue("");
							else
								row.createCell(4).setCellValue(classHour.getUser().getUserName());
							row.createCell(5).setCellValue(classHour.getRoomNo());
						}
						
					});
					
					ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
					try {
						workbook.write(outputStream);  // here we are writing into same file not creating different file
					} catch (IOException e) {
						throw new IllegalRequestException(e.getMessage());
					}
					try {
						workbook.close();
					} catch (IOException e) {
						throw new IllegalRequestException(e.getMessage());
					}
					
					byte[] byteData = outputStream.toByteArray();
					
					return ResponseEntity.ok()
							.header("Content Disposition", "attachment; filename="+file.getOriginalFilename())
							.contentType(MediaType.APPLICATION_OCTET_STREAM)
							.body(byteData);
					
					
				})
				.orElseThrow(()-> new AcademicProgramNotFoundByIdException("Failed to write data to excel"));
	}

	
	

}












