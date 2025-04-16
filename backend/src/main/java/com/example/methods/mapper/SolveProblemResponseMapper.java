package com.example.methods.mapper;

import com.example.methods.dto.SolveProblemResponse;
import org.springframework.stereotype.Component;

@Component
public class SolveProblemResponseMapper {
    public SolveProblemResponse mapToDto(double root) {
        return SolveProblemResponse.builder()
                .root(root)
                .build();
    }
}
