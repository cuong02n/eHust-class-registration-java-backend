package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

@MappedSuperclass
@Data
public abstract class BaseEntity implements Serializable {
    @Column(name = "createdBy")
    @Expose
    String createdById;

    @JoinColumn(name = "createdBy", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @Expose(deserialize = false, serialize = false)
    @Nullable
    User createdBy;

    @Column(name = "updatedBy")
    @Expose
    String updatedById;

    @JoinColumn(name = "updatedBy", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @Expose(deserialize = false, serialize = false)
    @Nullable
    User updatedBy;

    @CreationTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "createdTime", updatable = false)
    @Expose
    @Nullable
    Timestamp createdTime;

    @UpdateTimestamp
    @Temporal(value = TemporalType.TIMESTAMP)
    @Column(name = "updatedTime")
    @Expose
    @Nullable
    Timestamp updatedTime;

    @PrePersist
    protected void onCreate() {
        createdTime = new Timestamp(System.currentTimeMillis());
        updatedTime = createdTime;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedTime = new Timestamp(System.currentTimeMillis());
    }

    public void setUserModified(User user){
        if(createdById == null){
            createdById = user.getEmail();
        }
        updatedById = user.getEmail();
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (createdTime == null) {
            createdTime = now;
        }
        updatedTime = now;
    }
}
