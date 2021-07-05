package com.studentdemo.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
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
		List<StudentInfo> studentInfo = new ArrayList<>();
		try {
			studentInfo = studentService.getAllStudentsInfo();
			if (!CollectionUtils.isEmpty(studentInfo)) {
				log.info("Number of Student Info : {}", studentInfo.size());
				return new ResponseEntity<>(studentInfo, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(studentInfo, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {

			return new ResponseEntity<>(studentInfo, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@GetMapping("/student/{studentId}")
	public ResponseEntity<StudentInfo> getStudentInfo(@PathVariable("studentId") int studentId) {
		log.info("inside getStudentInfo method start, Student Id : {}", studentId);
		StudentInfo studentInfo =new StudentInfo();
		try {
			 studentInfo = studentService.getStudentById(studentId);
			if (null != studentInfo) {

				return new ResponseEntity<>(studentInfo, HttpStatus.OK);
			} else {
				return new ResponseEntity<>(studentInfo, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {

			return new ResponseEntity<>(studentInfo, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/save")
	public ResponseEntity<StudentInfo> saveStudentInfo(@RequestBody StudentInfo student) {
		log.info("inside saveStudentInfo method star ");
		StudentInfo studentInfo  =new StudentInfo();
		try {
			 studentInfo = studentService.saveOrUpdate(student);
			if (null != studentInfo) {

				return new ResponseEntity<>(studentInfo, HttpStatus.CREATED);
			} else {
				return new ResponseEntity<>(studentInfo, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception ex) {

			return new ResponseEntity<>(studentInfo, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
