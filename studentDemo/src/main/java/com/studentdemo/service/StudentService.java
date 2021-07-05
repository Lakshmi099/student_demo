package com.studentdemo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.common.collect.Lists;
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
	public StudentRepository studentRepository;

	@Autowired
	public MarksRepository marksRepository;

//getting all student records
	public List<StudentInfo> getAllStudentsInfo() {
		log.info("inside getAllStudentsInfo method -start");
		List<StudentInfo> studentInfoAll = new ArrayList<>();
		List<Student> students = new ArrayList<>();
		try {

			Iterable<Student> findAll = studentRepository.findAll();
			if (!CollectionUtils.isEmpty(Lists.newArrayList(findAll))) {

				findAll.forEach(student -> students.add(student));

				for (Student student : students) {
					int studentId = student.getStudentId();
					log.info("Student Id : {}", studentId);
					studentInfoAll.add(getStudentById(studentId));
				}

			}
		} catch (Exception ex) {
			log.error("Exception occurred while fetching the student detais : {}", ex);

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
			Optional<Student> studentInfo = studentRepository.findById(id);

			if (studentInfo.isPresent()) {

				Student student = studentInfo.get();
				// student = student.get()
				String hallTicketNumber = student.getHallTicketNumber();
				studenInfo.setStudentId(student.getStudentId());
				studenInfo.setName(student.getName());
				List<Marks> marks = new ArrayList<>();

				Iterable<Marks> findAll = marksRepository.findAll();

				if (!CollectionUtils.isEmpty(Lists.newArrayList(findAll))) {

					findAll.forEach(mark -> marks.add(mark));

					responseMarks = marks.stream().filter(mark -> mark.getHallTicketNumber().equalsIgnoreCase(hallTicketNumber))
							.collect(Collectors.toList());

					studenInfo.setMarks(responseMarks);
				}
			}
		} catch (Exception ex) {
			log.error("Exception occurred while fetching the student and marks detais : {}", ex);
		}
		log.info("inside getStudentById method -end");
		return studenInfo;
	}

	public StudentInfo saveOrUpdate(StudentInfo studentInfo) {
		log.info("inside saveOrUpdate method -start");

		StudentInfo studentResponse = new StudentInfo();
		if (null != studentInfo) {

			Student student = new Student();
			student.setStudentId(studentInfo.getStudentId());
			student.setName(studentInfo.getName());

			List<Marks> marks = studentInfo.getMarks();
			if (!CollectionUtils.isEmpty(marks)) {

				saveOrUpdateStudentMarks(marks, student);
			} else {
				studentRepository.save(student);
			}
			studentResponse = getStudentById(studentInfo.getStudentId());

		}

		log.info("inside saveOrUpdate method -end");
		return studentResponse;

	}

	private void saveOrUpdateStudentMarks(List<Marks> sMarks, Student student) {
		log.info("inside saveOrUpdateStudentMarks method -start");
		for (Marks marks : sMarks) {
			student.setHallTicketNumber(marks.getHallTicketNumber());
			studentRepository.save(student);
			int total = marks.getPhysics() + marks.getChemistry() + marks.getMaths();
			marks.setTotalMarks(total);
			float avg =(float) total/3;
			marks.setAvg(avg);
			marksRepository.save(marks);

		}
		log.info("inside saveOrUpdateStudentMarks method -end");
	}

}