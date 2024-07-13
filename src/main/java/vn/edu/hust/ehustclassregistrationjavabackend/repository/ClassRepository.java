package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import jakarta.annotation.Nonnull;
import lombok.NonNull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = {"classes"})
public interface ClassRepository extends JpaRepository<Class, ClassPK> {

    @Cacheable(key = "#semester")
    List<Class> findAllByClassPK_Semester(String semester);

    @Cacheable(key = "#classPK.id + #classPK.semester")
    Optional<Class> findByClassPK(ClassPK classPK);

    @Override
    @Nonnull
    @CacheEvict(allEntries = true)
    <S extends Class> List<S> saveAll(@NonNull Iterable<S> entities);

//      default List<Class> findAllByClassPK_SemesterAndClassPK_IdIn(String semester,List<String> classIds){
//        List<Class> result = new ArrayList<>();
//        for(String classId : classIds){
//            result.add(findByClassPK(new ClassPK(classId,semester)).orElseThrow());
//        }
//    }


}
