package vn.edu.hust.ehustclassregistrationjavabackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.service.ClassService;

@SpringBootApplication
public class Application {
    static ApplicationContext  ctx = SpringApplication.run(Application.class);
    public static void main(String[] args) {
        ClassService service = ctx.getBean(ClassService.class);
        service.test();
    }
}
