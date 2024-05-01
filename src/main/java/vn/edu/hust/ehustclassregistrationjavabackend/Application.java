package vn.edu.hust.ehustclassregistrationjavabackend;

import com.google.gson.Gson;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.Meta;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserCourseRegistration;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.MetadataRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.TimestampUtil;

import java.sql.Timestamp;
import java.util.*;

@SpringBootApplication
public class Application {
    static ApplicationContext ctx;

//    public static void main(String[] args) {
//        List<User> users = getListVirtualUser();
//        UserService userService = ctx.getBean(UserService.class);
//        userService.createUser(users);
//
//        List<Course> courses = getListVirtualCourse();
//        CourseService courseService = ctx.getBean(CourseService.class);
//        courseService.addCourse(courses);
//
//        List<UserCourseRegistration> registrationCourses = getVirtualCourseRegistration(users,courses);
//        courseService.insertUserCourseRegistration(registrationCourses);
//    }

//    public static void main(String[] args) {
//        ctx =  SpringApplication.run(Application.class);
//    }

    public static void main(String[] args) {
//        Map<String,String > map = new
//        map.put("123","123");
//        map.put("123","231");
//        System.out.println(new Gson().toJson(map));
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
                            .courseCode("IT_" + i)
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

    static List<Metadata> createMetadata() {
        return List.of(
                new Metadata("this_semester", "20231"),

                new Metadata("open_official_elitech", Timestamp.valueOf("2024-01-15 00:00:00").toString()),
                new Metadata("close_official_elitech", Timestamp.valueOf("2024-01-18 00:00:00").toString()),

                new Metadata("open_official_standard", Timestamp.valueOf("2024-01-18 00:00:00").toString()),
                new Metadata("close_official_standard", Timestamp.valueOf("2024-01-22 00:00:00").toString()),

                new Metadata("open_unofficial_elitech", Timestamp.valueOf("2024-01-22 00:00:00").toString()),
                new Metadata("close_unofficial_elitech", Timestamp.valueOf("2024-01-27 00:00:00").toString()),

                new Metadata("open_unofficial_standard", Timestamp.valueOf("2024-01-27 00:00:00").toString()),
                new Metadata("close_unofficial_standard", Timestamp.valueOf("2024-02-05 00:00:00").toString()),

                new Metadata("open_free_all", Timestamp.valueOf("2024-02-05 00:00:00").toString()),
                new Metadata("close_free_all", Timestamp.valueOf("2024-02-17 00:00:00").toString())
                // TODO: metadata Etag or another v.v
        );
    }
}
