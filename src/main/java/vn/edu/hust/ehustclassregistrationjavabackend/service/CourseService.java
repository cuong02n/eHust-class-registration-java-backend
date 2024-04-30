package vn.edu.hust.ehustclassregistrationjavabackend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserCourseRegistration;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.CourseRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserCourseRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final UserCourseRepository userCourseRepository;
    public List<Course> getAllActiveCourse(){
        return courseRepository.findAllByActiveTrue();
    }

    public List<Course> getAllCourses(){
        return courseRepository.findAll();
    }
    public Course getCourseById(long id){
        return courseRepository.findById(id).orElse(null);
    }
    public void addCourse(Course course){
        courseRepository.save(course);
    }
    public void addCourse(List<Course> courses){ courseRepository.saveAll(courses);}
    public Course getActiveCourse(String courseCode){
        return courseRepository.findByActiveTrueAndCourseCode(courseCode);
    }

    public Course insertCourse(Course course){// TODO: must auth
        return courseRepository.save(course);
    }

    public void insertUserCourseRegistration(List<UserCourseRegistration> registrations){
        userCourseRepository.saveAll(registrations);
    }
}
