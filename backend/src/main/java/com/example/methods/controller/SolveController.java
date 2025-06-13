package com.example.methods.controller;

import com.example.methods.dto.*;
import com.example.methods.service.MethodService;
import com.example.methods.service.ProblemService;
import jakarta.validation.Valid;
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
        return methodService.solveProblemEquations(request);
    }

    @PostMapping("/interpolation")
    public SolveProblemResponse interpolation(@RequestBody InterpolationRequest request) {
        return methodService.solveProblemInterpolation(request);
    }

    @PostMapping("/hermit_interpolation")
    public SolveProblemResponse interpolate(@RequestBody HermiteInterpolationRequest requestDto) {
        return methodService.solveProblemHermitInterpolation(requestDto);
    }

    @PostMapping("/gauss")
    public SolveProblemResponse solveGaussian(@Valid @RequestBody LinearSystemRequestDto req) {
        return methodService.systemsGaussSolve(req);
    }

    @PostMapping("/tridiagonal_sweep")
    public SolveProblemResponse solveSweep(@Valid @RequestBody LinearSystemRequestDto req) {
        return methodService.systemsSweepSolve(req);
    }

}
