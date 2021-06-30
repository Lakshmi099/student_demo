
package com.studentdemo.service;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.studentdemo.model.Marks;
import com.studentdemo.model.Student;
import com.studentdemo.model.StudentInfo;
import com.studentdemo.repository.MarksRepository;
import com.studentdemo.repository.StudentRepository;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class StudentServiceTest {

	@Autowired
	public StudentService serivce;

	@MockBean
	private StudentRepository studentRepository;

	@MockBean
	private MarksRepository marksRepository;

	@Before
	public void init() {
		assertNotNull(serivce);
		assertNotNull(studentRepository);
		assertNotNull(marksRepository);
	}

	@Test
	public void testGetStudentById() {
		int studentId = 1;
		String hallTicketNum = "062021123123";
		Student student = new Student();
		student.setName("Test");
		student.setStudentId(studentId);
		student.setHallTicketNumber(hallTicketNum);
		List<Student> studentList = new ArrayList<>();
		studentList.add(student);
		Optional<Student> studentObj = Optional.ofNullable(student);

		ArrayList<Marks> marksList = new ArrayList<>();

		Marks marks = new Marks(hallTicketNum, 70, 67, 54);
		marksList.add(marks);

		when(studentRepository.findById(1)).thenReturn(studentObj);
		when(marksRepository.findAll()).thenReturn(marksList);

		StudentInfo studentInfo = serivce.getStudentById(1);
		assertNotNull(studentInfo);
		assertEquals(studentInfo.getStudentId(), studentId);
		assertEquals(studentInfo.getMarks().get(0).getHallTicketNumber(), hallTicketNum);
		verify(studentRepository, times(1)).findById(1);
		verify(marksRepository, times(1)).findAll();

	}

	@Test
	public void testGetStudentByIdWithNull() {

		Optional<Student> studentObj = Optional.ofNullable(null);
		when(studentRepository.findById(1)).thenReturn(studentObj);
		StudentInfo studentInfo = serivce.getStudentById(1);
		assertNotNull(studentInfo);
		assertEquals(0, studentInfo.getStudentId());
		assertNull(studentInfo.getMarks());
		verify(studentRepository, times(1)).findById(1);

	}

	@Test
	public void testGetStudentByIdWithException() {

		when(studentRepository.findById(1)).thenThrow(new RuntimeException());

		StudentInfo studentInfo = serivce.getStudentById(1);
		assertNotNull(studentInfo);
		assertEquals(studentInfo.getStudentId(), 0);
		assertNull(studentInfo.getName());
		verify(studentRepository, times(1)).findById(1);
	}

	@Test
	public void testGetStudentByIdWithMarksNull() {
		Student student = new Student();
		student.setName("Test");
		student.setStudentId(1);
		student.setHallTicketNumber("123123");
		Optional<Student> studentObj = Optional.ofNullable(student);
		when(studentRepository.findById(1)).thenReturn(studentObj);
		StudentInfo studentInfo = serivce.getStudentById(1);
		assertNotNull(studentInfo);
		assertThat(studentInfo.getStudentId(),is(1));
		assertNull(studentInfo.getMarks());
		verify(studentRepository, times(1)).findById(1);

	}

	@Test
	public void testGetStudentByIdWithDifferentHallTicketNumber() {
		Student student = new Student();
		student.setName("Test");
		student.setStudentId(1);
		student.setHallTicketNumber("123123");

		List<Marks> marks = new ArrayList<>();
		Marks mark = new Marks("123", 70, 67, 54);
		marks.add(mark);

		Optional<Student> studentObj = Optional.ofNullable(student);
		when(studentRepository.findById(1)).thenReturn(studentObj);
		when(marksRepository.findAll()).thenReturn(marks);
		StudentInfo studentInfo = serivce.getStudentById(1);
		assertNotNull(studentInfo);
		assertEquals(1, studentInfo.getStudentId());
		assertEquals(0, studentInfo.getMarks().size());
		verify(studentRepository, times(1)).findById(1);
		verify(marksRepository, times(1)).findAll();

	}

	@Test
	public void testSaveOrUpdateStudentInfo() {

		int studentId = 1;
		String hallTicketNum = "062021123123";
		Student student = new Student();
		student.setName("Test");
		student.setStudentId(studentId);
		student.setHallTicketNumber(hallTicketNum);
		List<Student> studentList = new ArrayList<>();
		studentList.add(student);
		Optional<Student> studentObj = Optional.ofNullable(student);

		ArrayList<Marks> marksList = new ArrayList<>();

		Marks marks = new Marks(hallTicketNum, 70, 67, 54);
		marksList.add(marks);

		when(studentRepository.findById(1)).thenReturn(studentObj);
		when(marksRepository.findAll()).thenReturn(marksList);

		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setStudentId(1);
		studentInfo.setName("test");
		studentInfo.setMarks(marksList);
		StudentInfo studentInfoResponse = serivce.saveOrUpdate(studentInfo);
		assertNotNull(studentInfoResponse);
		assertEquals(studentInfoResponse.getStudentId(), studentId);
		assertEquals(studentInfoResponse.getMarks().size(), 1);
		assertEquals(studentInfoResponse.getMarks().get(0).getHallTicketNumber(), hallTicketNum);
		verify(studentRepository, times(1)).findById(studentId);
		verify(marksRepository, times(1)).findAll();

	}
	
	
	
	@Test
	public void testSaveOrUpdateStudentInfoWithEmptyRequest() {

		StudentInfo studentInfoResponse = serivce.saveOrUpdate(null);

		assertNull(studentInfoResponse.getName());
		assertNull(studentInfoResponse.getMarks());

	}
	
	
	
	@Test
	public void testSaveOrUpdateStudentInfoWithEmptyMarks() {

		int studentId = 1;
		String hallTicketNum = "062021123123";
		Student student = new Student();
		student.setName("Test");
		student.setStudentId(studentId);
		student.setHallTicketNumber(hallTicketNum);
		List<Student> studentList = new ArrayList<>();
		studentList.add(student);
		Optional<Student> studentObj = Optional.ofNullable(student);
		when(studentRepository.findById(1)).thenReturn(studentObj);
		StudentInfo studentInfo = new StudentInfo();
		studentInfo.setStudentId(1);
		studentInfo.setName("test");
		StudentInfo studentInfoResponse = serivce.saveOrUpdate(studentInfo);
		assertNotNull(studentInfoResponse);
		assertEquals(studentInfoResponse.getStudentId(), studentId);
		assertNull(studentInfoResponse.getMarks());
		assertThat(studentInfoResponse.getName(),is("Test"));
		verify(studentRepository, times(1)).findById(studentId);

	}
	
	

	@Test
	public void testGetAllStudentsInfo() {
		Student student = new Student();
		student.setName("Test1");
		student.setStudentId(1);
		student.setHallTicketNumber("1234");

		Student student2 = new Student();
		student2.setName("Test2");
		student2.setStudentId(2);
		student2.setHallTicketNumber("1234");
		List<Student> studentList = new ArrayList<>();
		studentList.add(student2);
		ArrayList<Marks> marksList = new ArrayList<>();

		Marks marks = new Marks("1234", 70, 67, 54);
		marksList.add(marks);

		when(studentRepository.findById(1)).thenReturn(Optional.ofNullable(student));
		when(studentRepository.findById(2)).thenReturn(Optional.ofNullable(student2));
		when(marksRepository.findAll()).thenReturn(marksList);
		when(studentRepository.findAll()).thenReturn(studentList);
		List<StudentInfo> info = serivce.getAllStudentsInfo();

		assertThat(info.size(), is(1));
		verify(marksRepository, times(1)).findAll();
		verify(studentRepository, times(1)).findAll();

	}

	@Test
	public void testGetAllStudentsInfoWithEmptyList() {
		
		List<Student> studentList = new ArrayList<>();
		when(studentRepository.findAll()).thenReturn(studentList);
		List<StudentInfo> info = serivce.getAllStudentsInfo();

		assertThat(info.size(), is(0));
		verify(studentRepository, times(1)).findAll();

	}
	
	
	@Test
	public void TestGetAllStudentsInfoWithException() {
		when(studentRepository.findAll()).thenThrow(new RuntimeException());
		serivce.getAllStudentsInfo();
		
	}

}
