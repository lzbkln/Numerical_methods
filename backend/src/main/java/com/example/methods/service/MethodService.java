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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public DtoMethod getMethodByName(String name) {
        Method method = methodRepository.findByName(name);
        return methodMapper.mapToDto(method);
    }

    public List<DtoMethod> getAllMethodsByProblemName(String name) {
        Problem problem = problemRepository.findByName(name);
        List<Method> methods = methodRepository.findAllByProblemId(problem.getId());
        return methods.stream()
                .map(methodMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public SolveProblemResponse solveProblem(SolveProblemRequest solveProblemRequest) {
        SolveProblem solveProblem;
        switch (solveProblemRequest.getMethodName()) {
            case "Метод Дихотомия" -> solveProblem = bisectionMethodService;
            case "Метод неподвижных хорд" -> solveProblem = fixedChordsMethodService;
            case "Метод Ньютона" -> solveProblem = newtonMethodService;
            case "Метод подвижных хорд" -> solveProblem = noFixedChordsMethodService;
            default -> throw new IllegalArgumentException();
        }
        return solveProblemResponseMapper.mapToDto(solveProblem.solveProblem(solveProblemRequest.getUserFunction(), solveProblemRequest.getA(),
                solveProblemRequest.getB(), solveProblemRequest.getEpsilon()));
    }
}
