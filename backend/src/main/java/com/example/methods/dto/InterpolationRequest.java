package com.example.methods.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class InterpolationRequest {
    private String methodId;
    private List<Double> xValues;
    private List<Double> fxValues;
    private List<Double> interpolationPoints;
}