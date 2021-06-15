package com.studentdemo.repository;

import org.springframework.data.repository.CrudRepository;

import com.studentdemo.model.Marks;

public interface MarksRepository extends CrudRepository<Marks, String> {

}
