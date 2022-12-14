package tr.edu.bilkent.cs.cs102.registerplusplus.server.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tr.edu.bilkent.cs.cs102.registerplusplus.server.entity.Course;
import tr.edu.bilkent.cs.cs102.registerplusplus.server.repo.CourseRepository;

import java.util.List;

@RestController
public class CourseController {
    private final CourseRepository repository;

    CourseController(CourseRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/courses")
    public ResponseEntity<List<Course>> all() {
        return ResponseEntity.ok(repository.findAll());
    }

    @PostMapping("/course")
    public Course newItem(@RequestBody Course course) {
        return repository.save(course);
    }
}
