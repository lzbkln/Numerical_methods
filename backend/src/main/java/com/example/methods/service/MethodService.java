package com.example.methods.service;

import com.example.methods.dto.DtoMethod;
import com.example.methods.dto.SolveProblemRequest;
import com.example.methods.dto.SolveProblemResponse;
import com.example.methods.mapper.MethodMapper;
import com.example.methods.mapper.SolveProblemResponseMapper;
import com.example.methods.model.Method;
import com.example.methods.model.Problem;
import com.example.methods.repository.MethodRepository;
import com.example.methods.repository.ProblemRepository;
import com.example.methods.service.equations.*;
import com.example.methods.service.equations.SolveEquationsProblem;
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

    public SolveProblemResponse solveProblem(SolveProblemRequest solveProblemRequest) {
        SolveEquationsProblem solveProblem = switch (solveProblemRequest.getMethodId()) {
            case "1" -> bisectionMethodService;
            case "2" -> fixedChordsMethodService;
            case "3" -> newtonMethodService;
            case "4" -> noFixedChordsMethodService;
            default -> throw new IllegalArgumentException("Unknown method: " + solveProblemRequest.getMethodId());
        };
        String solutionMessage = solveProblem.solveProblem(solveProblemRequest.getUserFunction(), solveProblemRequest.getA(),
                solveProblemRequest.getB(), solveProblemRequest.getEpsilon());
        return solveProblemResponseMapper.mapToDto(solutionMessage);
    }
}
