package ru.hogwarts.school.controller;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.StudentService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StudentController.class)
public class StudentControllerWithWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;

    @Test
    void addStudent_shouldReturnStudentAndStatusCreated() throws Exception {
        Student student = new Student(1L, "Иван", 55);

        when(studentService.addStudent(anyLong(), any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Иван\",\"age\":55}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Иван"))
                .andExpect(jsonPath("$.age").value(55));
    }

    @Test
    void getStudent_shouldReturnStudentAndStatusOk() throws Exception {
        Student student = new Student(1L, "Петр", 66);

        when(studentService.findStudent(1L)).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Петр"))
                .andExpect(jsonPath("$.age").value(66));
    }

    @Test
    void editStudent_shouldReturnUpdatedStudentAndStatusOk() throws Exception {
        Student student = new Student(1L, "Федор", 77);

        when(studentService.editStudent(any(Student.class))).thenReturn(student);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Федор\",\"age\":77}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Федор"))
                .andExpect(jsonPath("$.age").value(77));
    }

    @Test
    void deleteStudent_shouldReturnStatusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/student/1"))
                .andExpect(status().isOk());
    }

    @Test
    void findStudentsByAge_shouldReturnStudentsListAndStatusOk() throws Exception {
        Student student1 = new Student(1L, "Петр", 66);
        Student student2 = new Student(2L, "Сидр", 66);
        List<Student> students = Arrays.asList(student1, student2);

        when(studentService.getStudentsByAge(66)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student")
                        .param("age", "66"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Петр"))
                .andExpect(jsonPath("$[1].name").value("Сидр"));
    }

    @Test
    void findByAgeBetween_shouldReturnStudentsListAndStatusOk() throws Exception {
        Student student1 = new Student(1L, "Юрий", 32);
        Student student2 = new Student(2L, "Деточкин", 46);
        List<Student> students = Arrays.asList(student1, student2);

        when(studentService.findByAgeBetween(18, 50)).thenReturn(students);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/student/filter-by-age")
                        .param("min", "18")
                        .param("max", "50"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Юрий"))
                .andExpect(jsonPath("$[1].name").value("Деточкин"));
    }

}
