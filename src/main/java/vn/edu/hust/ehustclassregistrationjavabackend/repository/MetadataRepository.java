package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;

import java.util.List;
import java.util.Optional;

public interface MetadataRepository extends JpaRepository<Metadata, Metadata.MetadataPk> {
    Optional<Metadata> findByMetadataPk_MetadataKeyAndMetadataPk_Semester(Metadata.MetadataKey key, String semester);
    Optional<Metadata> findByMetadataPk_MetadataKey(Metadata.MetadataKey key);

    List<Metadata> findAllByMetadataPk_Semester(String semester);
}
