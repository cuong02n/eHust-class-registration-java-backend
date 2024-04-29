package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;

@MappedSuperclass
@Getter
public abstract class BaseEntity {
    @Column(name = "createdBy")
    @Expose
    @Nullable
    String createdById;

    @JoinColumn(name = "createdBy", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @Expose(serialize = false)
    @Nullable
    User createdBy;

    @Column(name = "updatedBy")
    @Expose
    @Nullable
    String updatedById;

    @JoinColumn(name = "updatedBy", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @Expose(serialize = false)
    @Nullable
    User updatedBy;

    @CreationTimestamp
    @Column(name = "createdTime", updatable = false)
    @Expose
    @Nullable
    Timestamp createdTime;

    @UpdateTimestamp
    @Column(name = "updatedTime")
    @Expose
    @Nullable
    Timestamp updatedTime;

    public void update(String userId) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (createdTime == null) {
            createdTime = now;
        }
        if (createdById == null) {
            createdById = userId;
        }
        updatedTime = now;
        updatedById = userId;
    }
}
