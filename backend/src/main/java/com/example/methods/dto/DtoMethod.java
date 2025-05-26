package com.example.methods.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class DtoMethod {
    private String id;
    private String problemId;
    private String name;
    private String description;
    private String example;
    private String imageUrl;
}
