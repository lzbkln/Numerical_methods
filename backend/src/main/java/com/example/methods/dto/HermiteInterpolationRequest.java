package com.example.methods.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class HermiteInterpolationRequest {
    private double[] nodes;
    private int[] multiplicities;
    private double[] functionValues;
    private List<List<Double>> derivatives;
}
