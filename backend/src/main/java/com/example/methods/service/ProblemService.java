package com.example.methods.service;

import com.example.methods.dto.DtoProblem;
import com.example.methods.mapper.ProblemMapper;
import com.example.methods.model.Problem;
import com.example.methods.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemMapper problemMapper;
    private final ProblemRepository problemRepository;

    public DtoProblem getProblemByName(String name) {
        Problem problem = problemRepository.findByName(name);
        return problemMapper.mapToDto(problem);
    }

    public List<DtoProblem> getAllProblems() {
        List<Problem> problems = problemRepository.findAll();
        return problems.stream()
                .map(problemMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
