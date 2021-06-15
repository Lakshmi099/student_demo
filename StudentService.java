package com.studentdemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.studentdemo.model.Marks;
import com.studentdemo.model.Student;
import com.studentdemo.model.StudentInfo;
import com.studentdemo.repository.MarksRepository;
import com.studentdemo.repository.StudentRepository;

import lombok.extern.slf4j.Slf4j;

//defining the business logic
@Service
@Slf4j
public class StudentService {
	@Autowired
	StudentRepository studentRepository;

	@Autowired
	MarksRepository marksRepository;

//getting all student records
	public List<StudentInfo> getAllStudentsInfo() {
		log.info("inside getAllStudentsInfo method -start");
		List<StudentInfo> studentInfoAll = new ArrayList<>();
		List<Student> students = new ArrayList<>();
		try {
			studentRepository.findAll().forEach(student -> students.add(student));

			if (null != students) {
				for (Student student : students) {
					int studentId = student.getStudentId();
					log.info("Student Id : {}", studentId);
					studentInfoAll.add(getStudentById(studentId));
				}
			}

		} catch (Exception ex) {
			log.error("Exception occurred while fetching the student detais : {}", ex);
			throw new RuntimeException(ex);
		}
		log.info("inside getAllStudentsInfo method -end");
		return studentInfoAll;
	}

//getting a specific record
	public StudentInfo getStudentById(int id) {
		log.info("inside getStudentById method -start");
		List<Marks> responseMarks = new ArrayList<>();
		StudentInfo studenInfo = new StudentInfo();
		try {
			Student student = studentRepository.findById(id).get();

			if (null != student) {

				String hallTicketNumber = student.getHallTicketNumber();

				List<Marks> marks = new ArrayList<>();
				marksRepository.findAll().forEach(mark -> marks.add(mark));

				responseMarks = marks.stream().filter(mark -> mark.getHallTicketNumber() == hallTicketNumber)
						.collect(Collectors.toList());
				studenInfo.setStudentId(student.getStudentId());
				studenInfo.setName(student.getName());
				studenInfo.setMarks(responseMarks);

			}
		} catch (Exception ex) {
			log.error("Exception occurred while fetching the student and marks detais : {}", ex);
		}
		log.info("inside getStudentById method -end");
		return studenInfo;
	}

	public List<Marks> getAllMarksById(int id) {
		log.info("inside getAllMarksById method -start");
		List<Marks> marks = new ArrayList<>();
		marksRepository.findAll().forEach(mark -> marks.add(mark));
		log.info("inside getAllMarksById method -end");
		return marks;
	}

	public StudentInfo saveOrUpdate(StudentInfo studentInfo) {
		log.info("inside saveOrUpdate method -start");
		if (null != studentInfo) {

			Student student = new Student();
			student.setStudentId(studentInfo.getStudentId());
			student.setName(studentInfo.getName());

			List<Marks> marks = studentInfo.getMarks();
			if (!CollectionUtils.isEmpty(marks) && marks.size() > 0) {
				
				saveOrUpdateStudentMarks(marks,student);
			}

		}
		log.info("inside saveOrUpdate method -end");
		return studentInfo;

	}

	private void saveOrUpdateStudentMarks(List<Marks> sMarks,Student student) {
		log.info("inside saveOrUpdateStudentMarks method -start");
		for (Marks marks : sMarks) {
			student.setHallTicketNumber(marks.getHallTicketNumber());
			studentRepository.save(student);
			int total = marks.getPhysics() + marks.getChemistry() + marks.getMaths();
			marks.setTotalMarks(total);
			marks.setAvg(total / 3);
			marks = 	marksRepository.save(marks);
			log.info("marks :{} ",marks.getHallTicketNumber());

		}
		log.info("inside saveOrUpdateStudentMarks method -end");
	}

//deleting a specific record
	public void delete(int id) {
		studentRepository.deleteById(id);
	}
}