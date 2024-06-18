package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;

import java.util.Optional;

public interface MetadataRepository extends JpaRepository<Metadata, Long> {
    Optional<Metadata> findByMetadataKeyAndSemester(Metadata.MetadataKey key, String semester);
    Optional<Metadata> findByMetadataKey(Metadata.MetadataKey key);
}
