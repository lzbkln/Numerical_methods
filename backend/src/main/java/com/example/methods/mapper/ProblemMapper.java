package com.example.methods.mapper;


import com.example.methods.dto.DtoProblem;
import com.example.methods.model.Problem;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProblemMapper {
    private final MethodMapper methodMapper;

    public DtoProblem mapToDto(Problem problem) {
        return DtoProblem.builder()
                .id(problem.getId().toString())
                .name(problem.getName())
                .description(problem.getDescription())
                .methods(problem.getMethods().stream().map(methodMapper::mapToDto).toList())
                .build();
    }
}
