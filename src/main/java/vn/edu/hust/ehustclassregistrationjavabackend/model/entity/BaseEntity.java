package vn.edu.hust.ehustclassregistrationjavabackend.model.entity;

import com.google.gson.annotations.Expose;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.sql.Timestamp;

@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity implements Serializable {
    @Column(name = "createdBy")
    @Expose
    @CreatedBy

    String createdById;

    @JoinColumn(name = "createdBy", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @Expose(deserialize = false, serialize = false)
    @Nullable

    User createdBy;

    @Column(name = "updatedBy")
    @Expose
    @LastModifiedBy
    String updatedById;

    @JoinColumn(name = "updatedBy", updatable = false, insertable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    @Expose(deserialize = false, serialize = false)
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

}
