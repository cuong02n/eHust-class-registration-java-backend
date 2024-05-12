package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Table(name = "user")
@Builder
@AllArgsConstructor
public class User extends BaseEntity implements UserDetails {
    @Id
    @Expose
    String id;

    @Enumerated(EnumType.STRING)
    @Expose
    @Column(nullable = false)
    Role role;

    @Enumerated(EnumType.STRING)
    @Expose
    StudentType studentType;

    @Expose
    String name;

    @Expose
    String email;
    @Expose(serialize = false)
    String password;

    @Expose
    int maxCredit;
    public String getPassword(){
        return "";
    }
    @Expose
    @Column(columnDefinition = "BIT(1) default 1")
    boolean active;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return getId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public enum Role {
        ROLE_STUDENT,
        ROLE_ADMIN
    }

    public enum StudentType {
        ELITECH,
        STANDARD
    }

}
