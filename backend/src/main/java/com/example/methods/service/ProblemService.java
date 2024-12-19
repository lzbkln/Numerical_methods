package com.example.methods.service;

import com.example.methods.dto.DtoProblem;
import com.example.methods.mapper.ProblemMapper;
import com.example.methods.model.Problem;
import com.example.methods.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProblemService {
    private final ProblemMapper problemMapper;
    private final ProblemRepository problemRepository;

    public DtoProblem getProblemByName(String name) {
        Problem problem = problemRepository.findByName(name);
        return problemMapper.mapToDto(problem);
    }
}
