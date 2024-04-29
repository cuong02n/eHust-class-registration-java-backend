package vn.edu.hust.ehustclassregistrationjavabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.ClassRepository;

@Service
@RequiredArgsConstructor
public class ClassService {
    final ClassRepository classRepository;
    public void test(){
        System.out.println(classRepository.countRegistedByClassId(1L,"20231"));
    }
}
