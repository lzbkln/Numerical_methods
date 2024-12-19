package com.example.methods.service;

import com.example.methods.dto.DtoMethod;
import com.example.methods.mapper.MethodMapper;
import com.example.methods.model.Method;
import com.example.methods.repository.MethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MethodService {
    private final MethodRepository methodRepository;
    private final MethodMapper methodMapper;

    public DtoMethod getMethodByName(String name) {
        Method method = methodRepository.findByName(name);
        return methodMapper.mapToDto(method);
    }
}
