package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.CourseRelationship;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserCourseRegistration;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.CourseRelationshipRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.CourseRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserCourseRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseService {
    final HttpServletResponse response;
    final CourseRelationshipRepository relationshipRepository;
    final CourseRepository courseRepository;
    final UserCourseRepository userCourseRepository;

    public List<Course> getAllActiveCourse() {
        return courseRepository.findAll();
    }

    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Course getCourseById(String id) {
        return courseRepository.findById(id).orElse(null);
    }

    public void addCourse(Course course) {
        courseRepository.save(course);
    }

    public void addCourse(List<Course> courses) {
        courseRepository.saveAll(courses);
    }

    public Optional<Course> getActiveCourse(String courseId) {
        List<Course> courses = getCourse(courseId);
        if (courses.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(courses.get(0));
    }

    public List<Course> getCourse(String courseId) {
        return courseRepository.findAllByIdOrderByVersionDesc(courseId);
    }

    public Course insertCourse(Course course) {// TODO: must auth
        String courseId = course.getId();
        Optional<Course> existingCourse = getActiveCourse(courseId);
        if (existingCourse.isEmpty()) {
            course.setVersion(0);
        } else {
            course.setVersion(existingCourse.get().getVersion() + 1);
        }
        return courseRepository.save(course);
    }

    public CourseRelationship insertCourseRelationship(CourseRelationship courseRelationship) {
        return relationshipRepository.save(courseRelationship);
    }


    public void insertUserCourseRegistration(List<UserCourseRegistration> registrations) {
        userCourseRepository.saveAll(registrations);
    }

    public void addRelationship(List<CourseRelationship> relationships) {
        relationshipRepository.saveAll(relationships);
    }

    public Optional<CourseRelationship> getRelationshipById(Long relationshipId) {
        return relationshipRepository.findById(relationshipId);
    }
}
