package com.studentdemo.controller;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studentdemo.model.Marks;
import com.studentdemo.model.StudentInfo;
import com.studentdemo.service.StudentService;

@RunWith(SpringRunner.class)
@WebMvcTest(StudentController.class)
@EnableAutoConfiguration
class StudentControllerTest {

	@Autowired
	MockMvc mockMvc;

	@MockBean
	StudentService studentService;

	@BeforeEach
	public void setUp() {
		assertNotNull(studentService);
	}

	@Test
	void testGetStudentInfo() throws Exception {

		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setStudentId(1);
		studentInfo.setName("Local Test");

		when(studentService.getStudentById(1)).thenReturn(studentInfo);
		mockMvc.perform(get("/studentInfo/api/student/{studentId}", 1).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isOk()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.studentId", is(studentInfo.getStudentId())))
				.andExpect(jsonPath("$.name", is(studentInfo.getName())));
	}

	@Test
	void testGetStudentInfoForInvalidUser() throws Exception {

		when(studentService.getStudentById(5)).thenReturn(null);
		mockMvc.perform(get("/studentInfo/api/student/{studentId}", 5).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isBadRequest());

	}

	@Test
	void testGetStudentInfoForInternalServerException() throws Exception {

		when(studentService.getStudentById(5)).thenThrow(new RuntimeException());

		mockMvc.perform(get("/studentInfo/api/student/{studentId}", 5).accept(MediaType.APPLICATION_JSON))
				.andDo(print()).andExpect(status().isInternalServerError());

	}

	@Test
	void testSaveStudentInfoCreated() throws Exception {
		List<Marks> marksInfo = new ArrayList<>();

		Marks mark = new Marks();
		mark.setHallTicketNumber("12345");
		mark.setChemistry(70);
		mark.setMaths(80);
		mark.setPhysics(90);

		marksInfo.add(mark);
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setStudentId(1);
		studentInfo.setName("Local Test");
		studentInfo.setMarks(marksInfo);

		when(studentService.saveOrUpdate(studentInfo)).thenReturn(studentInfo);
		mockMvc.perform(post("/studentInfo/api/save").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(studentInfo))).andExpect(status().isCreated())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.studentId", is(studentInfo.getStudentId())))
				.andExpect(jsonPath("$.name", is(studentInfo.getName())));
	}

	@Test
	void testSaveStudentInfInternalServerException() throws Exception {

		StudentInfo studentInfo = new StudentInfo();

		when(studentService.saveOrUpdate(studentInfo)).thenThrow(new RuntimeException());
		mockMvc.perform(post("/studentInfo/api/save").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(studentInfo))).andExpect(status().isInternalServerError());

	}

	@Test
	void testSaveStudentInfoBadRequest() throws Exception {

		StudentInfo studentInfo = new StudentInfo();

		when(studentService.saveOrUpdate(studentInfo)).thenReturn(null);
		mockMvc.perform(post("/studentInfo/api/save").contentType(MediaType.APPLICATION_JSON)
				.content(asJsonString(studentInfo))).andExpect(status().isBadRequest());

	}

	@Test
	void testGetAllStudentsInfo() throws Exception {
		List<Marks> marksInfo = new ArrayList<>();

		List<StudentInfo> studentList = new ArrayList<>();

		Marks mark = new Marks();
		mark.setHallTicketNumber("12345");
		mark.setChemistry(70);
		mark.setMaths(80);
		mark.setPhysics(90);

		marksInfo.add(mark);
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setStudentId(1);
		studentInfo.setName("Local Test");
		studentInfo.setMarks(marksInfo);
		studentList.add(studentInfo);
		when(studentService.getAllStudentsInfo()).thenReturn(studentList);
		mockMvc.perform(get("/studentInfo/api/all/students").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isOk()).andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.size()", is(studentList.size())));

	}

	@Test
	void testGetAllStudentsInfoWithEmptyList() throws Exception {

		List<StudentInfo> studentList = new ArrayList<>();

		when(studentService.getAllStudentsInfo()).thenReturn(studentList);
		mockMvc.perform(get("/studentInfo/api/all/students").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isBadRequest());

	}

	@Test
	 void testGetAllStudentsInfoWithNull() throws Exception {

		when(studentService.getAllStudentsInfo()).thenReturn(null);
		mockMvc.perform(get("/studentInfo/api/all/students").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	 void testGetAllStudentsInfoWithInternalserverError() throws Exception {

		when(studentService.getAllStudentsInfo()).thenThrow(new RuntimeException());
		mockMvc.perform(get("/studentInfo/api/all/students").accept(MediaType.APPLICATION_JSON)).andDo(print())
				.andExpect(status().isInternalServerError());
	}

	private static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
