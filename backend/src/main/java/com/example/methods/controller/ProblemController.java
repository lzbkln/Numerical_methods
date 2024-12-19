package com.example.methods.controller;

import com.example.methods.dto.DtoProblem;
import com.example.methods.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/problem")
@RequiredArgsConstructor
public class ProblemController {
    private final ProblemService problemService;

    @GetMapping("/name/{name}")
    public DtoProblem getProblemByName(@PathVariable String name) {
        return problemService.getProblemByName(name);
    }
}