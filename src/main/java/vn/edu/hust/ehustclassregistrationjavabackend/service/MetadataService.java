package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.admin.MetadataRequest;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.MetadataRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserClassRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.UserCourseRepository;
import vn.edu.hust.ehustclassregistrationjavabackend.utils.GsonUtil;

@Service
@RequiredArgsConstructor
public class MetadataService {
    private final MetadataRepository metadataRepository;
    private final UserClassRepository userClassRepository;
    private final UserCourseRepository userCourseRepository;

    /**
     * @param name:
     * @param semester: pass empty String for metadata not depends on semester
     * @param classofT: class want to return
     * @return T
     */
    public <T> T getMetadata(String name, String semester, java.lang.Class<T> classofT) {
        Metadata metadata = metadataRepository.findByMetadataPk_NameAndMetadataPk_Semester(name, semester).orElse(null);
        if (metadata == null) {
            return null;
        }
        return GsonUtil.gson.fromJson(metadata.getValue(), classofT);
    }

    public <T> T getMetadata(String name, java.lang.Class<T> classOfT) {
        return getMetadata(name, "", classOfT);
    }

    public String getCurrentSemester() {
        return getMetadata("current_semester", String.class);
    }

    public boolean isElitechOfficialRegisterClass(String semester) {
        return timeBetweenMetadata("open_class_official_elitech" , "close_class_official_elitech" , semester, System.currentTimeMillis());
    }

    public boolean isStandardOfficialRegisterClass(String semester) {
        return timeBetweenMetadata("open_class_official_standard", "close_class_official_standard" ,semester, System.currentTimeMillis());
    }

    public boolean isElitechUnofficialRegisterClass(String semester) {
        return timeBetweenMetadata("open_class_unofficial_elitech", "close_class_unofficial_elitech",semester, System.currentTimeMillis());
    }

    public boolean isStandardUnofficialRegisterClass(String semester) {
        return timeBetweenMetadata("open_class_unofficial_standard", "close_class_unofficial_standard" ,semester, System.currentTimeMillis());
    }

    public boolean isFreeClassRegister(String semester) {
        return timeBetweenMetadata("open_class_free_all", "close_class_free_all",semester, System.currentTimeMillis());
    }
    public boolean isAtTimeCourseRegistration(String semester){
        return timeBetweenMetadata("open_course_registration","close_course_registration",semester,System.currentTimeMillis());
    }

    private boolean timeBetweenMetadata(@Nonnull String metadataKeyStart, @Nonnull String metadataKeyEnd, String semester, long timeInMillis) {
        Metadata start = metadataRepository.findByMetadataPk_NameAndMetadataPk_Semester(metadataKeyStart, semester).orElse(null);
        if (start == null) return false;
        Metadata end = metadataRepository.findByMetadataPk_NameAndMetadataPk_Semester(metadataKeyEnd, semester).orElse(null);
        if (end == null) return false;

        return Long.parseLong(start.getValue()) <= timeInMillis && timeInMillis <= Long.parseLong(end.getValue());
    }

    public Metadata updateMetadata(MetadataRequest metadataRequest){
        return metadataRepository.saveAndFlush(metadataRequest.toEntity());
    }
}
