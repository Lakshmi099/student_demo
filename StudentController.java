package com.studentdemo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.studentdemo.model.StudentInfo;
import com.studentdemo.service.StudentService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/studentInfo/api/")
@Slf4j
public class StudentController {

	@Autowired
	private StudentService studentService;

	@GetMapping("all/students")
	public ResponseEntity<List<StudentInfo>> getAllStudentsInfo() {
		log.info("inside getAllStudentsInfo method start");

		try {
			List<StudentInfo> studentInfo = studentService.getAllStudentsInfo();
			if (null != studentInfo && studentInfo.size() >= 0) {
				log.info("Number of Student Info : {}", studentInfo.size());
				return new ResponseEntity<List<StudentInfo>>(studentInfo, HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {

			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/student/{studentId}")
	private ResponseEntity<StudentInfo> getStudentInfo(@PathVariable("studentId") int studentId) {
		log.info("inside getStudentInfo method start, Student Id : {}", studentId);

		try {
			StudentInfo studentInfo = studentService.getStudentById(studentId);
			if (null != studentInfo) {

				return new ResponseEntity<StudentInfo>(studentInfo, HttpStatus.ACCEPTED);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {

			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

//creating a delete mapping that deletes a specific student
	@DeleteMapping("/student/{id}")
	private void deleteStudent(@PathVariable("id") int id) {
		studentService.delete(id);
	}

	@PostMapping("/save")
	private ResponseEntity<StudentInfo> saveStudentInfo(@RequestBody StudentInfo student) {
		log.info("inside saveStudentInfo method star ");

		try {
			StudentInfo studentInfo = studentService.saveOrUpdate(student);
			if (null != studentInfo) {

				return new ResponseEntity<StudentInfo>(studentInfo, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {

			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
