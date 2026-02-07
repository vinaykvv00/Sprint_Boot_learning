package com.telusko.SpringSecEX.controller;


import com.telusko.SpringSecEX.model.Student;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


import java.util.List;

@RestController
public class StudentController
{
    private List<Student> students = List.of(
            new Student(1, "John Doe",120),
            new Student(2, "Jane Smith", 130),
            new Student(3, "Alice Johnson", 150)
    );

    @GetMapping("/students")
    public List<Student> getStudents()
    {
       return students;
    }

    @GetMapping("/csrf-token")
    public CsrfToken getCsrfToken(HttpServletRequest request)
    {
        return (CsrfToken) request.getAttribute(CsrfToken.class.getName());
    }

    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student)
    {
       students.add(student);
       return student;
    }

}
