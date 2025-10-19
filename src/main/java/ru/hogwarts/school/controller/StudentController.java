package ru.hogwarts.school.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {
    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/names-starting-with-a")
    @Operation(summary = "Get student names starting with A")
    public ResponseEntity<List<String>> getStudentNamesStartingWithA() {
        List<String> names = studentService.getStudentNamesStartingWithA();
        return ResponseEntity.ok(names);
    }

    @GetMapping("/another-average-age")
    @Operation(summary = "Get average age of all students")
    public ResponseEntity<Double> getAverageAge() {
        Double averageAge = studentService.getAverageAge();
        return ResponseEntity.ok(averageAge);
    }

    @GetMapping("/longest-faculty")
    @Operation(summary = "Get the longest faculty name")
    public ResponseEntity<String> getLongestFacultyName() {
        String longestFaculty = studentService.getLongestFacultyName();
        return ResponseEntity.ok(longestFaculty);
    }

    @GetMapping("/optimized-sum")
    @Operation(summary = "Calculate optimized sum from 1 to 1,000,000")
    public ResponseEntity<Long> getOptimizedSum() {
        long sum = studentService.calculateOptimizedSum();
        return ResponseEntity.ok(sum);
    }

    @GetMapping("/count")
    public int getStudentsCount() {
        return studentService.getStudentsCount();
    }

    @GetMapping("/average-age")
    public double getStudentsAverageAge() {
        return studentService.getStudentsAverageAge();
    }

    @GetMapping("/last-five")
    public List<Student> getLastFiveStudents() {
        return studentService.getLastFiveStudents();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student.getId(), student);
    }

    @GetMapping("/filter-by-age")
    public List<Student> findByAgeBetween(@RequestParam int min, @RequestParam int max) {
        return studentService.findByAgeBetween(min, max);
    }

    @GetMapping("/{id}")
    public Student getStudent(@PathVariable long id) {
        return studentService.findStudent(id);
    }

    @PutMapping
    public Student editStudent(@RequestBody Student student) {
        return studentService.editStudent(student);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable long id) {
        studentService.deleteStudent(id);
    }

    @GetMapping
    public ResponseEntity<Collection<Student>> findStudents(@RequestParam(required = false) int age) {
        if (age > 0) {
            return ResponseEntity.ok(studentService.getStudentsByAge(age));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
}
