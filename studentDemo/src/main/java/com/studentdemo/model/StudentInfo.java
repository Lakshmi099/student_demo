
package com.studentdemo.model;

import java.util.List;

import lombok.Data;

@Data
public class StudentInfo {
	private int studentId;
	private String name;
	private List<Marks> marks;

}
