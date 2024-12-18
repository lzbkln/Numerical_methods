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
    private MethodService methodService;

    @GetMapping("/{id}")
    public DtoMethod getMethodById(@PathVariable Long id) {
        return methodService.getMethodById(id);
    }

//    @PostMapping("/save")
//    public void saveMethod(@RequestBody
//                           DtoMethod dtoMethod) {
//        methodService.saveNewMethod(dtoMethod);
//    }
//
//    @DeleteMapping("/delete/{id}")
//    public void deleteMethod(@PathVariable Long id) {
//        methodService.deleteMethodById(id);
//    }
}
