package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import vn.edu.hust.ehustclassregistrationjavabackend.config.MessageException;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin.CourseRelationshipRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.student.StudentCourseRegistrationRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.CourseRelationship;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserCourseRegistration;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.CourseRelationshipRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.CourseRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserCourseRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.ExcelUtil;
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
    private final MetadataService metadataService;

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

    public List<Course> insertCourses(List<Course> courses) {
        List<Course> duplicateCourses = courseRepository.findAllByIdIn(courses.stream().map(Course::getId).distinct().toList());
        if (!duplicateCourses.isEmpty()) {
            throw new RuntimeException("There is duplicated course, please take attention: "+duplicateCourses.stream().map(Course::getId).toList());
        }
        return courseRepository.saveAll(courses);
    }

    public List<Course> insertCourses(MultipartFile file) {
        return insertCourses(ExcelUtil.getCourseRequest(file));
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

    public CourseRelationship insertCourseRelationship(CourseRelationshipRequest request) {
        CourseRelationship relationship =
                CourseRelationship.builder()
                        .courseId(request.getCourseId())
                        .courseConstraintId(request.getCourseConstraintId())
                        .relation(request.getRelation())
                        .build();
        return relationshipRepository.saveAndFlush(relationship);
    }

    public CourseRelationship deleteCourseRelationShip() {
        return null;
        //TODO
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

    public List<UserCourseRegistration> getRegistedCourse(String semester) {
        User user = (User) httpServletRequest.getAttribute("user");
        return getRegistedCourse(user.getId(), semester);
    }

    public List<UserCourseRegistration> getRegistedCourse(String semester, String userId) {
        return userCourseRepository.findAllByUserIdAndSemester(userId, semester);
    }

    public int countCourseCreditByCourseIds(List<String> courseIds){
         return courseRepository.sumCreditByCourseIds(courseIds);
    }

    public List<UserCourseRegistration> registerCourse(StudentCourseRegistrationRequest courseRequest) {
        User user = (User) httpServletRequest.getAttribute("user");

        if(!metadataService.isAtTimeCourseRegistration(courseRequest.getSemester())){
            throw new MessageException("không phải thời gian đăng ký học phần");
        }

        if(!courseRegistedNotExceedMaximumCredit(user,courseRequest.getSemester(),courseRequest.getCourseIds())){
            throw new MessageException("Đã vượt quá số tín cho phép");
        }

        List<UserCourseRegistration> registrations = new Vector<>();
        for (String courseId : courseRequest.getCourseIds()) {
            var courseRegistration = UserCourseRegistration.builder()
                    .semester(courseRequest.getSemester())
                    .userId(user.getId())
                    .courseId(courseId)
                    .build();
            courseRegistration.setCreatedById(user.getId());
            courseRegistration.setUpdatedById(user.getId());
            registrations.add(courseRegistration);
        }
        return userCourseRepository.saveAllAndFlush(registrations);
    }



    public long unregisterCourse(String semester, List<String> courseIds) {
        User user = (User) httpServletRequest.getAttribute("user");
        return userCourseRepository.deleteAllByUserIdAndSemesterAndCourseIdIn(user.getId(), semester, courseIds);
    }

    public long unregisterCourse(List<String> courseIds) {
        return unregisterCourse(metadataService.getCurrentSemester(), courseIds);
    }


    public boolean studentMatchedAllCourseBefore(User student, Class registerClass) {
        // TODO
        return true;
    }

    public boolean isStudentRegisterCourseBefore(User student, String courseId, String semester) {
        return userCourseRepository.findByCourseIdAndSemesterAndUserId(courseId, semester, student.getId()).isPresent();
    }

    public boolean courseRegistedNotExceedMaximumCredit(User student, String semester, List<String> courseIds) {
        return getCourseCreditRegisted(student, semester)
                + courseRepository.sumCreditByCourseIds(courseIds)<=student.getMaxCredit();
    }
    public int getCourseCreditRegisted(User student,String semester){
        return userCourseRepository.sumCreditRegistedByUserIdAndSemester(student.getId(),semester);
    }
}
