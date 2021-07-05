package com.studentdemo.repository;
import org.springframework.data.repository.CrudRepository;

import com.studentdemo.model.Student;
public interface StudentRepository extends CrudRepository<Student, Integer>
{
}
