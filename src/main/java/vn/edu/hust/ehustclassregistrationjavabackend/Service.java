package vn.edu.hust.ehustclassregistrationjavabackend;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.stereotype.Component;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.ObjectUtil;

import java.lang.reflect.Method;
import java.util.Random;

public class Service {
    public static void main(String[] args) {
        for (int i = 0; i < 15; i++) {
            Random r = new Random(15);
            System.out.println(r.nextInt());
        }
    }
}