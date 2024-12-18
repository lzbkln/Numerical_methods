package com.example.methods.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DtoMethod {
    private String id;
    private String name;
    private String fullDescription;
    private String shortDescription;
    private String example;
}
