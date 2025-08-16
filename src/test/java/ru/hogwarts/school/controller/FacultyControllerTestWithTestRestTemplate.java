package ru.hogwarts.school.controller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.hogwarts.school.model.Faculty;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacultyControllerTestWithTestRestTemplate {
    @LocalServerPort
    private int port;

//    @Autowired
//    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    private String getBaseUrl() {
        return "http://localhost:" + port + "/faculty";
    }

//    @Test
//    void contextLoads() throws Exception {
//        Assertions.assertThat(facultyController).isNotNull();
//    }

    @Test
    public void testFaculty() throws Exception {
        Assertions.assertThat(this.restTemplate.getForObject(getBaseUrl(), String.class).contains("Я люблю тебя жизнь!"));
    }

    @Test
    void addFaculty_shouldReturnCreatedFacultyAndStatusCreated() {
        Faculty faculty = new Faculty();
        faculty.setName("Первый");
        faculty.setColor("Red");

        ResponseEntity<Faculty> response = restTemplate.postForEntity(
                getBaseUrl(),
                faculty,
                Faculty.class
        );

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Первый", response.getBody().getName());
    }

    @Test
    void getFaculty_shouldReturnFacultyAndStatusOk() {
        Faculty faculty = new Faculty();
        faculty.setName("Второй");
        faculty.setColor("Green");
        Faculty createdFaculty = restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + createdFaculty.getId(),
                Faculty.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Второй", response.getBody().getName());
    }

    @Test
    void editFaculty_shouldReturnUpdatedFacultyAndStatusOk() {
        Faculty faculty = new Faculty();
        faculty.setName("Третий");
        faculty.setColor("Blue");
        Faculty createdFaculty = restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        createdFaculty.setColor("Blue and Black");
        HttpEntity<Faculty> request = new HttpEntity<>(createdFaculty);
        ResponseEntity<Faculty> response = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT,
                request,
                Faculty.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Blue and Black", response.getBody().getColor());
    }

    @Test
    void deleteFaculty_shouldReturnStatusOk() {
        Faculty faculty = new Faculty();
        faculty.setName("Третий");
        faculty.setColor("Blue");
        Faculty createdFaculty = restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        restTemplate.delete(getBaseUrl() + "/" + createdFaculty.getId());

        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + createdFaculty.getId(),
                Faculty.class
        );
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void findFacultiesByColor_shouldReturnFacultiesListAndStatusOk() {
        Faculty faculty = new Faculty();
        faculty.setName("Четвертый");
        faculty.setColor("Gold");
        restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                getBaseUrl() + "?color=Gold",
                Collection.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void findByNameOrColorIgnoreCase_shouldReturnFacultiesListAndStatusOk() {
        Faculty faculty = new Faculty();
        faculty.setName("Пятый");
        faculty.setColor("Red");
        restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                getBaseUrl() + "/filter?search=red",
                Collection.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(response.getBody().isEmpty());
    }

    @Test
    void getStudentsByFacultyId_shouldReturnStudentsListAndStatusOk() {
        Faculty faculty = new Faculty();
        faculty.setName("Шестой");
        faculty.setColor("Green");
        Faculty createdFaculty = restTemplate.postForObject(getBaseUrl(), faculty, Faculty.class);

        ResponseEntity<Collection> response = restTemplate.getForEntity(
                getBaseUrl() + "/" + createdFaculty.getId() + "/students",
                Collection.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }
}
