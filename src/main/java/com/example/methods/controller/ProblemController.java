package com.example.methods.controller;

import com.example.methods.dto.DtoProblem;
import com.example.methods.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController("/problem")
@RequiredArgsConstructor
public class ProblemController {
    private ProblemService problemService;

    @GetMapping("/{id}")
    public DtoProblem getProblem(Long id) {
        return problemService.getProblemById(id);
    }

    @PostMapping("/save")
    public void saveMethod(@RequestBody DtoProblem dtoProblem) {
        problemService.saveNewProblem(dtoProblem);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteMethod(@PathVariable Long id) {
        problemService.deleteProblemById(id);
    }
}
