package vn.edu.hust.ehustclassregistrationjavabackend.controller.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.edu.hust.ehustclassregistrationjavabackend.config.MessageException;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.service.UserService;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.BaseResponse;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    final UserService userService;
    final HttpServletRequest request;

    @GetMapping("/get-info")
    public ResponseEntity<?> getInfo() {
        User user = (User) request.getAttribute("user");
        if (user == null) {
            throw new MessageException("Không tồn tại");
        }
        return BaseResponse.ok(user);
    }

}
