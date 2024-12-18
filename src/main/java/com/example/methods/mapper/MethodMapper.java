package com.example.methods.mapper;

import com.example.methods.dto.DtoMethod;
import com.example.methods.model.Method;
import org.springframework.stereotype.Component;

@Component
public class MethodMapper {
    public DtoMethod mapToDto(Method method) {
        return DtoMethod.builder()
                .id(method.getId().toString())
                .name(method.getName())
                .fullDescription(method.getDescription().getFull())
                .shortDescription(method.getDescription().getShortDescr())
                .build();
    }

    public Method mapToModel(DtoMethod dtoMethod) {
        return Method.builder()
                .name(dtoMethod.getName())
                .description(
                        Method.Description.builder()
                                .full(dtoMethod.getFullDescription())
                                .shortDescr(dtoMethod.getShortDescription())
                                .build())
                .build();
    }
}
