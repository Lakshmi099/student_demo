package com.studentdemo.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;


@Entity
//defining class name as Table name
@Table
@Data
public class Student {

	@Id
	@Column
	private int studentId;

	@Column
	private String name;
	@Column
	private String hallTicketNumber;
	

}