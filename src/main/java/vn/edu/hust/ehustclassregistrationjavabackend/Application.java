package vn.edu.hust.ehustclassregistrationjavabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Course;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.UserCourseRegistration;
import vn.edu.hust.ehustclassregistrationjavabackend.service.CourseService;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;

import java.util.List;
import java.util.Vector;

@SpringBootApplication
public class Application {
    static ApplicationContext ctx = SpringApplication.run(Application.class);

    public static void main(String[] args) {
        UserService service = ctx.getBean(UserService.class);
        service.createUser(getListVirtualUser());
        CourseService courseService = ctx.getBean(CourseService.class);
        courseService.addCourse(getListVirtualCourse());
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

    static List<UserCourseRegistration> getVirtualCourseRegistration(){
        return null;
    }
}
