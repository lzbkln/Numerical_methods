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
    private ProblemMapper problemMapper;
    private ProblemRepository problemRepository;

    public DtoProblem getProblemById(Long id) {
        return problemMapper.mapToDto(problemRepository.findById(id).get());
    }

    public void saveNewProblem(DtoProblem dtoProblem) {
        problemRepository.save(problemMapper.mapToModel(dtoProblem));
    }

    public void deleteProblemById(Long id) {
        Problem problem = problemMapper.mapToModel(getProblemById(id));
        problemRepository.delete(problem);
    }
}
