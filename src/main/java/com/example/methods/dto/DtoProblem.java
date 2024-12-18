package com.example.methods.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class DtoProblem {
    private String id;
    private String name;
    private String fullDescription;
    private List<DtoMethod> methods;
}
