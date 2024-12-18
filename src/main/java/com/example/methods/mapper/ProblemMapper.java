package com.example.methods.mapper;


import com.example.methods.dto.DtoProblem;
import com.example.methods.model.Problem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProblemMapper {
    private MethodMapper methodMapper;

    public DtoProblem mapToDto(Problem problem) {
        return DtoProblem.builder()
                .id(problem.getId().toString())
                .name(problem.getName())
                .fullDescription(problem.getFullDescription())
                .methods(problem.getMethods().stream().map(method -> methodMapper.mapToDto(method)).toList())
                .build();
    }

    public Problem mapToModel(DtoProblem dtoProblem) {
        return Problem.builder()
                .name(dtoProblem.getName())
                .fullDescription(dtoProblem.getFullDescription())
                .build();
    }
}
