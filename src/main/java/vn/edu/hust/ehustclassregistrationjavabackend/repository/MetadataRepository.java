package vn.edu.hust.ehustclassregistrationjavabackend.repository;

import jakarta.annotation.Nonnull;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;

import java.util.List;

@CacheConfig(cacheNames = "metadata")
public interface MetadataRepository extends JpaRepository<Metadata, Metadata.MetadataPk> {

    @Cacheable(key = "#key + #semester")
    Metadata findByMetadataPk_MetadataKeyAndMetadataPk_Semester(Metadata.MetadataKey key, String semester);

    @Cacheable(key = "#semester")
    List<Metadata> findAllByMetadataPk_Semester(String semester);

    @Override
    @Nonnull
    @CachePut(key = "#entity.metadataPk.metadataKey + #entity.metadataPk.semester")
    @CacheEvict(key = "#entity.metadataPk.semester")
    <S extends Metadata> S save(@Nonnull S entity);
}
