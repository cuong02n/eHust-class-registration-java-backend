package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.MetadataRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.GsonUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MetadataService {
    private final MetadataRepository metadataRepository;

    public void saveAll(List<Metadata> metadatas) {
        metadataRepository.saveAll(metadatas);
    }

    public void save(String name, Object value) {
        metadataRepository.save(new Metadata(name, value.toString()));
    }

    public List<Metadata> getAllMetadata() {
        return metadataRepository.findAll();
    }

    public <T> T getMetadata(String name, Class<T> classofT) {
        Metadata metadata = metadataRepository.findById(name).orElse(null);
        if (metadata == null) {
            return null;
        }
        return GsonUtil.gson.fromJson(metadata.getValue(), classofT);
    }

    public boolean isSemesterOpenForStudent(String semester, User student) {
        if (student.getStudentType() == User.StudentType.ELITECH) {
            return false;
        }
        return true;// TODO:
    }

    public boolean isElitechOfficialRegisterClass(String semester, long timeInMillis) {
        return timeBetweenMetadata("open_official_elitech_" + semester, "close_official_elitech_" + semester, timeInMillis);
    }

    public boolean isStandardOfficialRegisterClass(String semester, long timeInMillis) {
        return timeBetweenMetadata("open_official_standard_" + semester, "close_official_standard_" + semester, timeInMillis);
    }

    public boolean isElitechUnofficialRegisterClass(String semester, long timeInMillis) {
        return timeBetweenMetadata("open_unofficial_elitech_" + semester, "close_unofficial_elitech_" + semester, timeInMillis);
    }

    public boolean isStandardUnofficialRegisterClass(String semester, long timeInMillis) {
        return timeBetweenMetadata("open_unofficial_standard_" + semester, "close_unofficial_standard_" + semester, timeInMillis);
    }

    public boolean isFreeRegister(String semester, long timeInMillis) {
        return timeBetweenMetadata("open_free_all_" + semester, "close_free_all_" + semester, timeInMillis);
    }

    private boolean timeBetweenMetadata(@Nonnull String metadataKeyStart, @Nonnull String metadataKeyEnd, long timeInMillis) {
        Metadata start = metadataRepository.findById(metadataKeyStart).orElse(null);
        if (start == null) return false;
        Metadata end = metadataRepository.findById(metadataKeyEnd).orElse(null);
        if (end == null) return false;

        return Long.parseLong(start.getValue()) <= timeInMillis && timeInMillis <= Long.parseLong(end.getValue());
    }

}
