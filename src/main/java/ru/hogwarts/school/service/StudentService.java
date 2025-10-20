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
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    private static final String TARGET_LETTER = "A";

    private final StudentRepository studentRepository;

    private final Object printLock = new Object();

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getFirstSixStudents() {
        return studentRepository.findFirstSixStudents();
    }

    public void printStudentsParallel() {
        List<Student> students = getFirstSixStudents();

        printName(students.get(0).getName());
        printName(students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            printName(students.get(2).getName());
            printName(students.get(3).getName());
        });

        Thread thread2 = new Thread(() -> {
            printName(students.get(4).getName());
            printName(students.get(5).getName());
        });

        executeThreads(thread1, thread2);
    }

    public void printStudentsSynchronized() {
        List<Student> students = getFirstSixStudents();

        printStudentNameSynchronized(students.get(0).getName());
        printStudentNameSynchronized(students.get(1).getName());

        Thread thread1 = new Thread(() -> {
            printStudentNameSynchronized(students.get(2).getName());
            printStudentNameSynchronized(students.get(3).getName());
        });

        Thread thread2 = new Thread(() -> {
            printStudentNameSynchronized(students.get(4).getName());
            printStudentNameSynchronized(students.get(5).getName());
        });

        executeThreads(thread1, thread2);
    }

    private void executeThreads(Thread thread1, Thread thread2) {
        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Ошибка при выполнении потоков", e);
        }
    }

    private void printName(String studentName) {
        System.out.println(studentName);
    }

    public void printStudentNameSynchronized(String studentName) {
        synchronized (printLock) {
            System.out.println(studentName);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public int getStudentsCount() {
        logger.info("Was invoked method for getStudentsCount");
        return studentRepository.countAllStudents();
    }

    public List<String> getStudentNamesStartingWithA() {
        return studentRepository.findAll().stream()
                .map(Student::getName)
                .filter(name -> name.startsWith(TARGET_LETTER))
                .map(String::toUpperCase)
                .sorted()
                .collect(Collectors.toList());
    }

    public Double getAverageAge() {
        return studentRepository.findAll().stream()
                .mapToInt(Student::getAge)
                .average()
                .orElse(0.0);
    }

    public String getLongestFacultyName() {
        return studentRepository.findAll().stream()
                .filter(student -> student != null && student.getFaculty().getName() != null)
                .map(Student::getFaculty)
                .map(Faculty::getName)
                .reduce((f1, f2) -> f1.length() >= f2.length() ? f1 : f2)
                .orElse("");
    }

    public long calculateOptimizedSum() {
        return LongStream.rangeClosed(1, 1_000_000)
                .parallel()
                .reduce(0, Long::sum);
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
