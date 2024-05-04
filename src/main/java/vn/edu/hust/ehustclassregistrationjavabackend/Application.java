package vn.edu.hust.ehustclassregistrationjavabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.*;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.MetadataService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import java.util.Vector;

@SpringBootApplication

public class Application {
    static ApplicationContext ctx;

    public static void main(String[] args) {

        ctx = SpringApplication.run(Application.class);

//        List<User> users = getListVirtualUser();
//        UserService userService = ctx.getBean(UserService.class);
//        userService.createUser(users);
//
//        List<Course> courses = getListVirtualCourse();
//        CourseService courseService = ctx.getBean(CourseService.class);
//        courseService.addCourse(courses);
//
//        List<UserCourseRegistration> registrationCourses = getVirtualCourseRegistration(users, courses);
//        courseService.insertUserCourseRegistration(registrationCourses);
//
//        List<CourseRelationship> relationships = getVirtualCourseRelationship(courses);
//        courseService.addRelationship(relationships);
//
//        List<Metadata> metadatas = createMetadata();
//        MetadataService metadataService = ctx.getBean(MetadataService.class);
//        metadataService.saveAll(metadatas);
    }

    static List<User> getListVirtualUser() {
        Vector<User> users = new Vector<>();
        for (int i = 1; i <= 100; i++) {
            users.add(User.builder()
                    .id(20204500 + i + "")
                    .email("virtual_user" + i + "@gmail.com")
                    .role(User.Role.STUDENT)
                    .name("Cuong Nguyen Manh " + i)
                    .build()
            );
        }
        return users;
    }

    static List<Course> getListVirtualCourse() {
        Vector<Course> courses = new Vector<>();
        for (int i = 1; i <= 2000; i++) {
            courses.add(
                    Course.builder()
                            .id("IT_" + i)
                            .courseName("Lập trình " + i)
                            .courseNameE("Code " + i)
                            .credit(2)
                            .build()
            );
        }
        return courses;
    }

    static List<UserCourseRegistration> getVirtualCourseRegistration(List<User> users, List<Course> courses) {
        Random r = new Random();
        double ratio = 0.03;
        List<UserCourseRegistration> registrations = new Vector<>();
        for (User user : users) {
            for (Course course : courses) {
                if (r.nextDouble() < ratio) {
                    registrations.add(
                            UserCourseRegistration.builder()
                                    .semester("20231")
                                    .courseId(course.getId())
                                    .userId(user.getId())
                                    .build()
                    );
                }
            }
        }
        return registrations;
    }

    static List<CourseRelationship> getVirtualCourseRelationship(List<Course> courses) {
        List<CourseRelationship> relationships = new Vector<>();
        double ratio = 0.002;
        Random r = new Random();
        for (int i = 0; i < courses.size(); i++) {
            for (int j = i; j < courses.size(); j++) {
                if (r.nextDouble() < ratio) {
                    int t = r.nextInt(3);
                    relationships.add(
                            CourseRelationship.builder()
                                    .courseId(courses.get(i).getId())
                                    .courseConstraintId(courses.get(j).getId())
                                    .relation((t == 2) ? CourseRelationship.Relation.PREREQUISITE : ((t == 1) ? CourseRelationship.Relation.PRECOURSE : CourseRelationship.Relation.COREQUISITECOURSE))
                                    .build()
                    );
                }
            }
        }
        return relationships;
    }

    static List<Metadata> createMetadata() {
        return List.of(
                new Metadata("this_semester", "20231"),

                new Metadata("open_official_elitech", String.valueOf(Timestamp.valueOf("2024-01-15 00:00:00").getTime())),
                new Metadata("close_official_elitech", String.valueOf(Timestamp.valueOf("2024-01-18 00:00:00").getTime())),

                new Metadata("open_official_standard", String.valueOf(Timestamp.valueOf("2024-01-18 00:00:00").getTime())),
                new Metadata("close_official_standard", String.valueOf(Timestamp.valueOf("2024-01-22 00:00:00").getTime())),

                new Metadata("open_unofficial_elitech", String.valueOf(Timestamp.valueOf("2024-01-22 00:00:00").getTime())),
                new Metadata("close_unofficial_elitech", String.valueOf(Timestamp.valueOf("2024-01-27 00:00:00").getTime())),

                new Metadata("open_unofficial_standard", String.valueOf(Timestamp.valueOf("2024-01-27 00:00:00").getTime())),
                new Metadata("close_unofficial_standard", String.valueOf(Timestamp.valueOf("2024-02-05 00:00:00").getTime())),

                new Metadata("open_free_all", String.valueOf(Timestamp.valueOf("2024-02-05 00:00:00").getTime())),
                new Metadata("close_free_all", String.valueOf(Timestamp.valueOf("2024-05-17 00:00:00").getTime()))
                // TODO: metadata Etag or another v.v
        );
    }
}
