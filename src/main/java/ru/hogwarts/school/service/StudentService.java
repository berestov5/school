package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.*;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public int getStudentsCount() {
        logger.info("Was invoked method for getStudentsCount");
        return studentRepository.countAllStudents();
    }

    public double getStudentsAverageAge() {
        logger.info("Was invoked method for getStudentsAverageAge");
        return studentRepository.getAverageAge();
    }

    public List<Student> getLastFiveStudents() {
        logger.info("Was invoked method for getLastFiveStudents");
        return studentRepository.findLastFiveStudents();
    }

    public List<Student> findByAgeBetween(int min, int max) {
        logger.info("Was invoked method for findByAgeBetween");
        return studentRepository.findByAgeBetween(min, max);
    }

    public Faculty getFacultyByStudentId(long studentId) {
        logger.info("Was invoked method for getFacultyByStudentId with id={}", studentId);
        return studentRepository.findById(studentId)
                .orElseThrow(() -> {
                    logger.error("There is no student with id={}", studentId);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND,("Student not found"));
                })
                .getFaculty();
    }

    public Student addStudent(long id, Student student) {
        logger.info("Was invoked method for addStudent");
        return studentRepository.save(student);
    }

    public Student findStudent(long id) {
        logger.debug("Was invoked method for findStudent by id={}", id);
        return studentRepository.findById(id).orElseThrow(() -> {
            logger.error("There is no student with id={}", id);
            return null;
        });
    }

    public Student editStudent(Student student) {
        logger.info("Was invoked method for editStudent with id={}", student.getId());
        if (studentRepository.existsById(student.getId())) {
            logger.info("Student with id={} successfully updated", student.getId());
            return studentRepository.save(student);
        }
        logger.error("There is no student with id={}", student.getId());
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found");
    }

    public void deleteStudent(long id) {
        logger.warn("Was invoked method for deleting a student with id={}", id);
        studentRepository.deleteById(id);
        logger.info("Student with id={} was successfully deleted", id);
    }

    public List<Student> getStudentsByAge(int age) {
        logger.info("Was invoked method for getStudentsByAge with age={}", age);
        return studentRepository.findByAge(age);
    }

}
