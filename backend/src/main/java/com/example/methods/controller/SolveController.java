package com.example.methods.controller;

import com.example.methods.dto.DtoMethod;
import com.example.methods.dto.DtoProblem;
import com.example.methods.dto.SolveProblemRequest;
import com.example.methods.dto.SolveProblemResponse;
import com.example.methods.service.MethodService;
import com.example.methods.service.ProblemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/numerical_methods")
@RequiredArgsConstructor
public class SolveController {
    private final MethodService methodService;
    private final ProblemService problemService;

    @GetMapping("/problems")
    public List<DtoProblem> getAllProblems() {
        return problemService.getAllProblems();
    }

    @GetMapping("/methods/{methodId}")
    public DtoMethod getAllAboutMethod(@PathVariable Long methodId) {
        return methodService.getMethodById(methodId);
    }

    @PostMapping("/nonlinear_equation")
    public SolveProblemResponse solveNonlinearEquation(@RequestBody SolveProblemRequest request) {
        return methodService.solveProblem(request);
    }
}
