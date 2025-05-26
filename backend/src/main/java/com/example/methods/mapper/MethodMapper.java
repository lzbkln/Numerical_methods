package com.example.methods.mapper;

import com.example.methods.dto.DtoMethod;
import com.example.methods.model.Method;
import org.springframework.stereotype.Component;

@Component
public class MethodMapper {
    public DtoMethod mapToDto(Method method) {
        return DtoMethod.builder()
                .id(method.getId().toString())
                .problemId(method.getProblem().getId().toString())
                .name(method.getName())
                .description(method.getDescription())
                .example(method.getExample())
                .imageUrl(method.getImageUrl())
                .build();
    }
}
