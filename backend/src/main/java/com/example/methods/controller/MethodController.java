package com.example.methods.controller;

import com.example.methods.dto.DtoMethod;
import com.example.methods.service.MethodService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController("/methods")
@RequiredArgsConstructor
public class MethodController {
    private final MethodService methodService;

    @GetMapping("/{id}")
    public DtoMethod getMethodByName(@PathVariable String name) {
        return methodService.getMethodByName(name);
    }
}
