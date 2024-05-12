package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.MetadataPk;

import java.util.Optional;

public interface MetadataRepository extends JpaRepository<Metadata, MetadataPk> {
    Optional<Metadata> findByMetadataPk_NameAndMetadataPk_Semester(String name,String semester);
}
