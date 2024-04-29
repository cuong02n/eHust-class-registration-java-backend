package vn.edu.hust.ehustclassregistrationjavabackend;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import vn.edu.hust.ehustclassregistrationjavabackend.model.HibernateProxyTypeAdapter;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserCourseRegistration;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.CourseRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserRepository;

@Component
@RequiredArgsConstructor

public class Service {
    final CourseRepository courseRepository;
    final UserRepository userRepository;
    public void test() {
        Course course = courseRepository.findById(1L).orElseThrow();
        User user =  userRepository.findById("1").orElseThrow();
//        System.out.println();
        System.out.println(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(user));
//        System.out.println(new Gson().fromJson(course,));
    }
}
