package ru.hogwarts.school.controller;

import netscape.javascript.JSObject;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(FacultyController.class)
public class FacultyControllerWithWebMvcTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    void addFaculty_shouldReturnFacultyAndStatusCreated() throws Exception {
        JSONObject facultyObject = new JSONObject();
        facultyObject.put("name", "Первый");
        facultyObject.put("color", "Red");

        Faculty faculty = new Faculty(1L, "Первый", "Red");

        when(facultyService.addFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(facultyObject.toString()))//"{\"id\":1,\"name\":\"Первый\",\"color\":\"Red\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Первый"))
                .andExpect(jsonPath("$.color").value("Red"))
                .andExpect(jsonPath("$.id").value(1L));
    }

    @Test
    void getFaculty_shouldReturnFacultyAndStatusOk() throws Exception {
        Faculty faculty = new Faculty(1L, "Второй", "Green");

        when(facultyService.findFaculty(1L)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Второй"))
                .andExpect(jsonPath("$.color").value("Green"));
    }

    @Test
    void editFaculty_shouldReturnUpdatedFacultyAndStatusOk() throws Exception {
        Faculty faculty = new Faculty(1L, "Третий", "Blue");

        when(facultyService.editFaculty(any(Faculty.class))).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders
                        .put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"name\":\"Третий\",\"color\":\"Blue\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Третий"))
                .andExpect(jsonPath("$.color").value("Blue"));
    }

    @Test
    void deleteFaculty_shouldReturnStatusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/faculty/1"))
                .andExpect(status().isOk());
    }

    @Test
    void findFacultiesByColor_shouldReturnFacultiesListAndStatusOk() throws Exception {
        Faculty faculty1 = new Faculty(1L, "Четвертый", "Red");
        Faculty faculty2 = new Faculty(2L, "Пятый", "Red");
        List<Faculty> faculties = Arrays.asList(faculty1, faculty2);

        when(facultyService.getFacultiesByColor("Red")).thenReturn(faculties);

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty")
                        .param("color", "Red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Четвертый"))
                .andExpect(jsonPath("$[1].name").value("Пятый"));
    }

    @Test
    void findByNameOrColorIgnoreCase_shouldReturnFacultiesListAndStatusOk() throws Exception {
        Faculty faculty = new Faculty(1L, "Шестой", "Green");

        when(facultyService.findByNameOrColorIgnoreCase("green")).thenReturn(Collections.singletonList(faculty));

        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/filter")
                        .param("search", "green"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Шестой"));
    }

    @Test
    void getStudentsByFacultyId_shouldReturnStudentsListAndStatusOk() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/faculty/1/students"))
                .andExpect(status().isOk());
    }
}
