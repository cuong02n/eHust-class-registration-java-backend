package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.ClassDto;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.ClassRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.ExcelUtil;

import java.io.InputStream;
import java.util.List;
import java.util.Vector;

@Service
@RequiredArgsConstructor
public class ClassService {



    private final ClassRepository classRepository;
    private final HttpServletResponse response;
    private final MetadataService metadataService;

    public Class getClassByIdAndSemester(String id, String semester) {
        return classRepository.findByClassPK(new ClassPK(id, semester));
    }

    public Class getClassByIdAndCurrentSemester(String id) {
        return classRepository.findByClassPK(new ClassPK(id, metadataService.getCurrentSemester()));
    }

    public List<ClassDto> updateClasses(MultipartFile file){
        try(InputStream stream = file.getInputStream()){
            return createClass(ExcelUtil.getClassDtoRequest(stream));
        }catch(Exception e){
            return null;
        }
    }

    public List<ClassDto> createClass(List<ClassDto> classDtos){
        List<ClassPK> classPKS = new Vector<>(classDtos.stream().map(classDto -> new ClassPK(classDto.getId(), classDto.getSemester())).toList());
        // check class PK
       if(classRepository.existsByClassPKIn(classPKS)){
           return null;
       }
       return classRepository.saveAllAndFlush(
               classDtos.stream().map(ClassDto::toClassEntity).toList() // Make entity for update database
       )
               .stream().map(Class::toClassDto).toList();// make Dto for reponse
    }

    public ClassDto cancelClass(ClassPK classPK){
        Class oldClass = classRepository.findByClassPK(classPK);
        if(oldClass==null){
            return null;
        }
        oldClass.setStatus(Class.Status.CANCEL);
        classRepository.saveAndFlush(oldClass);
        return oldClass.toClassDto();
    }

    public List<ClassDto> getClassByCourseId(String courseId, String semester) {
        List<ClassDto> result = new Vector<>();
        classRepository.findAllByCourseIdAndClassPK_Semester(courseId, semester).forEach(c -> result.add(c.toClassDto()));
        return result;
    }
}
