package vn.edu.hust.ehustclassregistrationjavabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.ClassRepository;

@Service
@RequiredArgsConstructor
public class ClassService {
    private final ClassRepository classRepository;

    public Class getClassById(Long id){
        return classRepository.findById(id).orElse(null);
    }

}
