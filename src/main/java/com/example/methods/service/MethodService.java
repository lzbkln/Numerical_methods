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
    private MethodRepository methodRepository;
    private MethodMapper methodMapper;

    public DtoMethod getMethodById(Long id) {
        return methodMapper.mapToDto(methodRepository.findById(id).get());
    }

    public void saveNewMethod(DtoMethod dtoMethod) {
        methodRepository.save(methodMapper.mapToModel(dtoMethod));
    }

    public void deleteMethodById(Long id) {
        Method method = methodMapper.mapToModel(getMethodById(id));
        methodRepository.delete(method);
    }
}
