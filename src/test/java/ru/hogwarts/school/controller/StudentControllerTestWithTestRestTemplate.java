package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Student;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerTestWithTestRestTemplate {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/student";
    }

    @Test
    void addStudent_shouldReturnCreatedStudentAndStatusOk() {
        Student student = new Student();
        student.setName("Иван");
        student.setAge(200);

        ResponseEntity<Student> response = restTemplate.postForEntity(
                getBaseUrl(),
                student,
                Student.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Иван", response.getBody().getName());
        assertEquals(200, response.getBody().getAge());
    }

    @Test
    void getStudent_shouldReturnStudentAndStatusOk() {
        Student student = new Student();
        student.setName("Петр");
        student.setAge(1);
        Student createdStudent = restTemplate.postForObject(getBaseUrl(), student, Student.class);

        ResponseEntity<Student> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + createdStudent.getId(),
                Student.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Петр", response.getBody().getName());
        assertEquals(1, response.getBody().getAge());
    }

    @Test
    void editStudent_shouldReturnUpdatedStudentAndStatusOk() {
        Student student = new Student();
        student.setName("Гриша");
        student.setAge(23);
        Student createdStudent = restTemplate.postForObject(getBaseUrl(), student, Student.class);

        createdStudent.setAge(23);
        HttpEntity<Student> request = new HttpEntity<>(createdStudent);
        ResponseEntity<Student> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT,
                request,
                Student.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(23, response.getBody().getAge());
    }
    @Test
    void deleteStudent_shouldReturnStatusOk() {
        Student student = new Student();
        student.setName("Огогошевич");
        student.setAge(2025);
        Student createdStudent = restTemplate.postForObject(getBaseUrl(), student, Student.class);

        restTemplate.delete(getBaseUrl() + "/" + createdStudent.getId());

        ResponseEntity<Student> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + createdStudent.getId(),
                Student.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findStudentsByAge_shouldReturnStudentsListAndStatusOk() {
        Student student = new Student();
        student.setName("Чебуратор");
        student.setAge(25);
        restTemplate.postForObject(getBaseUrl(), student, Student.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                getBaseUrl() + "?age=25",
                Collection.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }
    @Test
    void findByAgeBetween_shouldReturnStudentsListAndStatusOk() {
        Student student1 = new Student();
        student1.setName("Гена");
        student1.setAge(13);
        restTemplate.postForObject(getBaseUrl(), student1, Student.class);

        Student student2 = new Student();
        student2.setName("Крокодил");
        student2.setAge(14);
        restTemplate.postForObject(getBaseUrl(), student2, Student.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                getBaseUrl() + "/filter-by-age?min=12&max=16",
                Collection.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().size() >= 2);
    }

}
