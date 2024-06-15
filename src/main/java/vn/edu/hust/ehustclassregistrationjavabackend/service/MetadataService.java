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

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class MetadataService {
    private final MetadataRepository metadataRepository;
    private final UserClassRepository userClassRepository;
    private final UserCourseRepository userCourseRepository;

    /**
     * @param name:
     * @param semester: pass empty String for metadata not depends on semester
     * @return String
     */
    public String getMetadata(String name, String semester) {
        Metadata metadata = metadataRepository.findByMetadataPk_NameAndMetadataPk_Semester(name, semester).orElseThrow();
        return metadata.getValue();
    }

    public String getMetadata(String name) {
        return getMetadata(name, "");
    }

    /**
     * @param year require
     * @return Timestamp for response to client only, server doesnot use this
     */
    public Timestamp getDayStartWeek1(int year){
        return Timestamp.valueOf(getMetadata("date-start",year+"1"));
    }

    public String getCurrentSemester() {
        return getMetadata("current-semester");
    }

    public boolean isElitechOfficialRegisterClass(String semester) {
        return isTimeBetween("open-class-official-elitech", "close-class-official-elitech", semester, System.currentTimeMillis());
    }

    public boolean isStandardOfficialRegisterClass(String semester) {
        return isTimeBetween("open-class-official-standard", "close-class-official-standard", semester, System.currentTimeMillis());
    }

    public boolean isElitechUnofficialRegisterClass(String semester) {
        return isTimeBetween("open-class-unofficial-elitech", "close-class-unofficial-elitech", semester, System.currentTimeMillis());
    }

    public boolean isStandardUnofficialRegisterClass(String semester) {
        return isTimeBetween("open-class-unofficial-standard", "close-class-unofficial-standard", semester, System.currentTimeMillis());
    }

    public boolean isFreeClassRegister(String semester) {
        return isTimeBetween("open-class-free-all", "close-class-free-all", semester, System.currentTimeMillis());
    }

    public boolean isAtTimeCourseRegistration(String semester) {
        return isTimeBetween("open-course-registration", "close-course-registration", semester, System.currentTimeMillis());
    }

    private boolean isTimeBetween(@Nonnull String metadataKeyStart, @Nonnull String metadataKeyEnd, String semester, long timeInMillis) {
        Metadata start = metadataRepository.findByMetadataPk_NameAndMetadataPk_Semester(metadataKeyStart, semester).orElse(null);
        if (start == null) return false;
        Metadata end = metadataRepository.findByMetadataPk_NameAndMetadataPk_Semester(metadataKeyEnd, semester).orElse(null);
        if (end == null) return false;

        return Long.parseLong(start.getValue()) <= timeInMillis && timeInMillis <= Long.parseLong(end.getValue());
    }

    public Metadata updateMetadata(MetadataRequest metadataRequest) {
        return metadataRepository.saveAndFlush(metadataRequest.toEntity());
    }
}
