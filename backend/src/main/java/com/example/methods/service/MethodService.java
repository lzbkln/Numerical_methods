package com.example.methods.service;

import com.example.methods.dto.*;
import com.example.methods.mapper.MethodMapper;
import com.example.methods.mapper.SolveProblemResponseMapper;
import com.example.methods.model.Method;
import com.example.methods.model.Problem;
import com.example.methods.repository.MethodRepository;
import com.example.methods.repository.ProblemRepository;
import com.example.methods.service.equations.*;
import com.example.methods.service.interpolation.HermiteInterpolationService;
import com.example.methods.service.interpolation.LagrangeInterpolationService;
import com.example.methods.service.interpolation.NewtonInterpolationService;
import com.example.methods.service.interpolation.SolveInterpolationProblem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MethodService {
    private final MethodRepository methodRepository;
    private final MethodMapper methodMapper;
    private final SolveProblemResponseMapper solveProblemResponseMapper;
    private final BisectionMethodService bisectionMethodService;
    private final FixedChordsMethodService fixedChordsMethodService;
    private final NewtonMethodService newtonMethodService;
    private final NoFixedChordsMethodService noFixedChordsMethodService;
    private final ProblemRepository problemRepository;
    private final LagrangeInterpolationService lagrangeInterpolationService;
    private final NewtonInterpolationService newtonInterpolationService;
    private final HermiteInterpolationService hermiteInterpolationService;

    public DtoMethod getMethodById(Long id) {
        Optional<Method> method = methodRepository.findById(id);
        return methodMapper.mapToDto(method.get());
    }

    public List<DtoMethod> getAllMethodsByProblemName(String name) {
        Problem problem = problemRepository.findByName(name);
        List<Method> methods = methodRepository.findAllByProblemId(problem.getId());
        return methods.stream()
                .map(methodMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public SolveProblemResponse solveProblemEquations(SolveProblemRequest solveProblemRequest) {
        SolveEquationsProblem solveProblem = switch (solveProblemRequest.getMethodId()) {
            case "1" -> bisectionMethodService;
            case "2" -> fixedChordsMethodService;
            case "3" -> newtonMethodService;
            case "4" -> noFixedChordsMethodService;
            default -> throw new IllegalArgumentException("Unknown method: " + solveProblemRequest.getMethodId());
        };

        String solutionMessage = solveProblem.solveProblem(
                solveProblemRequest.getUserFunction(),
                solveProblemRequest.getA(),
                solveProblemRequest.getB(),
                solveProblemRequest.getEpsilon(),
                solveProblemRequest.getM()
        );

        return solveProblemResponseMapper.mapToDto(solutionMessage);
    }

    public SolveProblemResponse solveProblemInterpolation(InterpolationRequest interpolationRequest) {
        SolveInterpolationProblem solveProblem = switch (interpolationRequest.getMethodId()) {
            case "6" -> lagrangeInterpolationService;
            case "7" -> newtonInterpolationService;
            default -> throw new IllegalArgumentException("Unknown method: " + interpolationRequest.getMethodId());
        };
        String solutionMessage = solveProblem.solveInterpolationProblem(
                interpolationRequest.getXValues(),
                interpolationRequest.getFxValues(),
                interpolationRequest.getInterpolationPoints()
        );
        return solveProblemResponseMapper.mapToDto(solutionMessage);

    }

    public SolveProblemResponse solveProblemHermitInterpolation(HermiteInterpolationRequest interpolationRequest) {
        String solutionMessage = hermiteInterpolationService.solveInterpolationProblem(
                interpolationRequest.getNodes(),
                interpolationRequest.getMultiplicities(),
                interpolationRequest.getFunctionValues(),
                interpolationRequest.getDerivatives()
        );
        return solveProblemResponseMapper.mapToDto(solutionMessage);
    }
}