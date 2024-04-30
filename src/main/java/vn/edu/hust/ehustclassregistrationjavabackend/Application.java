package vn.edu.hust.ehustclassregistrationjavabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserCourseRegistration;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.MetadataRepository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Random;
import java.util.Vector;

@SpringBootApplication
public class Application {
    static ApplicationContext ctx = SpringApplication.run(Application.class);

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

    public static void main(String[] args) {
        MetadataRepository metadataRepository = ctx.getBean(MetadataRepository.class);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        timestamp.setNanos((int) (System.nanoTime() % 1000000000));
        System.out.println(timestamp);
        metadataRepository.save(new Metadata("aaaa", timestamp.toString()));
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
//                new Metadata("open_class_registration", System.currentTimeMillis() + ""),
//                new Metadata("open_free_class_registraion",System.currentTimeMillis()+86400000*7L+""),
//                new Metadata("close_class_registration", System.currentTimeMillis() +86400000*15L+ ""),
//                new Metadata()
                new Metadata("open_elitech_class_registration", "")
        );
    }
}
