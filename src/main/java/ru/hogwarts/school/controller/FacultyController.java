package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.service.FacultyService;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private FacultyService facultyService;

    public FacultyController(FacultyService facultyService) {
        this.facultyService = facultyService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyService.addFaculty(faculty);
    }

    @GetMapping("/filter")
    public List<Faculty> findByNameOrColorIgnoreCase(@RequestParam String search) {
        return facultyService.findByNameOrColorIgnoreCase(search);
    }

    @GetMapping("/{id}/students")
    public List<Student> getStudentsByFacultyId(@PathVariable long id) {
        return facultyService.getStudentsByFacultyId(id);
    }

    @GetMapping("/{id}")
    public Faculty getFaculty(@PathVariable long id) {
        return facultyService.findFaculty(id);
    }

    @PutMapping
    public Faculty editFaculty(@RequestBody Faculty faculty) {
        return facultyService.editFaculty(faculty);
    }

    @DeleteMapping("/{id}")
    public void deleteFaculty(@PathVariable long id) {
        facultyService.deleteFaculty(id);
    }

    @GetMapping
    public ResponseEntity<Collection<Faculty>> findFaculties(@RequestParam(required = false) String color) {
        if (color != null && !color.isBlank()) {
            return ResponseEntity.ok(facultyService.getFacultiesByColor(color));
        }
        return ResponseEntity.ok(Collections.emptyList());
    }
}
