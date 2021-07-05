package com.studentdemo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table
public class Marks {

	@Id
	@Column
	private String hallTicketNumber;

	@Column
	private float avg;
	@Column
	private int totalMarks;
	@Column
	private int physics;
	@Column
	private int chemistry;
	@Column
	private int maths;

	

}
