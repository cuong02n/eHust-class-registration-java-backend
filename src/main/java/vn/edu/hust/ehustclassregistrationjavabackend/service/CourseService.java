package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.CourseRelationship;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserCourseRegistration;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.CourseRelationshipRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.CourseRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserCourseRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.ObjectUtil;

import java.util.List;
import java.util.Vector;

@Service
@RequiredArgsConstructor
public class CourseService {
    final HttpServletResponse response;
    final CourseRelationshipRepository relationshipRepository;
    final CourseRepository courseRepository;
    final UserCourseRepository userCourseRepository;
    private final HttpServletRequest httpServletRequest;

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

    public Course getActiveCourse(String courseId) {
        return courseRepository.findById(courseId).orElse(null);
    }


    public Course insertCourse(Course course) {// TODO: must auth
        String courseId = course.getId();
        if (courseRepository.findById(courseId).isPresent()) {
            return null;
        }
        return courseRepository.save(course);
    }

    public Course updateCourse(Course newCourse) {
        Course existingCourse = courseRepository.findById(newCourse.getId()).orElse(null);
        if (existingCourse == null) return null;
        ObjectUtil.mergeEntityWithoutNullFieldAndId(existingCourse, newCourse);
        return courseRepository.save(existingCourse);
    }

    public Course deleteCourse(String courseId) {
        Course existingCourse = courseRepository.findById(courseId).orElse(null);
        if (existingCourse == null) {
            return null;
        }
        var deleted = relationshipRepository.deleteAllByCourseConstraintIdOrCourseId(courseId, courseId);
        System.out.println("deleted " + deleted.size());
        courseRepository.deleteById(courseId);
        return existingCourse;
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

    public CourseRelationship getRelationshipById(Long relationshipId) {
        return relationshipRepository.findById(relationshipId).orElse(null);
    }

    public List<UserCourseRegistration> getRegistedCourse(String semester){
        User user = (User)httpServletRequest.getAttribute("user");
        return userCourseRepository.findAllByUserIdAndSemester(user.getId(),semester);
    }
    public List<UserCourseRegistration> getRegistedCourse(String semester,String userId) {
        return userCourseRepository.findAllByUserIdAndSemester(userId,semester);
    }

    public List<UserCourseRegistration> registerCourse(String semester, List<String> courseIds) {
        // TODO: handle max credit for student
        User user = (User) httpServletRequest.getAttribute("user");
        List<UserCourseRegistration> registrations = new Vector<>();
        for (String courseId : courseIds) {
            registrations.add(
                    UserCourseRegistration.builder()
                            .semester(semester)
                            .userId(user.getId())
                            .courseId(courseId).build()
            );
        }

        return userCourseRepository.saveAllAndFlush(registrations);
    }

    public String unregisterCourse(String semester, List<String> courseIds) {
        User user = (User) httpServletRequest.getAttribute("user");
        return userCourseRepository.deleteAllByUserIdAndSemesterAndCourseIdIn(user.getId(), semester, courseIds) + " registration has been deleted";
    }
}
