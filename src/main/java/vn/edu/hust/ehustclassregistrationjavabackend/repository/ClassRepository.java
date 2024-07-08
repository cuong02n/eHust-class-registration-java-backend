package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = {"classes"})
public interface ClassRepository extends JpaRepository<Class, ClassPK> {

    List<Class> findAllByCourseIdAndClassPK_Semester(String courseId, String semester);

    List<Class> findAllByClassPK_Semester(String semester);

    List<Class> findAllByClassPKIn(List<ClassPK> classPKS);

    boolean existsByClassPKIn(List<ClassPK> classPKS);

    @Cacheable(key = "#classPK.id + #classPK.semester")
    Optional<Class> findByClassPK(ClassPK classPK);

//    default List<Class> findAllByClassPK_SemesterAndClassPK_IdIn(String semester,List<String> classIds){
//        List<Class> result = new ArrayList<>();
//        for(String classId : classIds){
//            result.add(findByClassPK(new ClassPK(classId,semester)).orElseThrow());
//        }
//    }

}
