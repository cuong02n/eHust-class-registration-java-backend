package vn.edu.hust.ehustclassregistrationjavabackend.service;

import lombok.RequiredArgsConstructor;
import org.apache.xmlbeans.impl.xb.xsdschema.Attribute;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.ClassPK;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    final UserRepository userRepository;

    public void createUser(User user) {
        userRepository.save(user);
    }

    public void createUser(List<User> users) {
        userRepository.saveAll(users);
    }

    public List<User> getAllStudent(){
        return userRepository.findAllByRole(User.Role.ROLE_STUDENT);
    }
    public List<User> getAllAdmin(){
        return userRepository.findAllByRole(User.Role.ROLE_ADMIN);
    }
    public List<User> getAllSuperAdmin(){
        return userRepository.findAllByRole(User.Role.ROLE_SUPER_ADMIN);
    }

    @Override
    public User loadUserByUsername(String id) throws UsernameNotFoundException {
        return userRepository.findById(id).orElse(null);
    }
    public User findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow();
    }

    public User findStudentById(String id){
        return userRepository.findByEmailContaining(id.substring(2)).orElseThrow();
    }
}
