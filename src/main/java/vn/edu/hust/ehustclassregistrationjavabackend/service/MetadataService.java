package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.config.MessageException;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.MetadataRepository;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SuppressWarnings("DanglingJavadoc")
@Service
@RequiredArgsConstructor
@CacheConfig(cacheNames = "metadata")
public class MetadataService {
    private final MetadataRepository metadataRepository;

    /**
     * @param key:
     * @param semester: pass empty String for metadata not depends on semester
     * @return String
     */
    public String getMetadata(Metadata.MetadataKey key, String semester) {
        return metadataRepository.findByMetadataPk_MetadataKeyAndMetadataPk_Semester(key, semester).getValue();
    }


    public List<Metadata> getAllMetadataBySemester(String semester) {
        return metadataRepository.findAllByMetadataPk_Semester(semester);
    }

    public boolean isElitechOfficialRegisterClass(String semester) {
        return isTimeBetween(Metadata.MetadataKey.START_REGISTER_CLASS_OFFICIAL_ELITECH, Metadata.MetadataKey.END_REGISTER_CLASS_OFFICIAL_ELITECH, semester, System.currentTimeMillis());
    }

    public boolean isStandardOfficialRegisterClass(String semester) {
        return isTimeBetween(Metadata.MetadataKey.START_REGISTER_CLASS_OFFICIAL_STANDARD, Metadata.MetadataKey.END_REGISTER_CLASS_OFFICIAL_STANDARD, semester, System.currentTimeMillis());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isElitechUnofficialRegisterClass(String semester) {
        return isTimeBetween(Metadata.MetadataKey.START_REGISTER_CLASS_UNOFFICIAL_ELITECH, Metadata.MetadataKey.END_REGISTER_CLASS_UNOFFICIAL_ELITECH, semester, System.currentTimeMillis());
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isStandardUnofficialRegisterClass(String semester) {
        return isTimeBetween(Metadata.MetadataKey.START_REGISTER_CLASS_UNOFFICIAL_STANDARD, Metadata.MetadataKey.END_REGISTER_CLASS_UNOFFICIAL_STANDARD, semester, System.currentTimeMillis());
    }

    public boolean isFreeClassRegister(String semester) {
        return isTimeBetween(Metadata.MetadataKey.START_REGISTER_FREE, Metadata.MetadataKey.END_REGISTER_FREE, semester, System.currentTimeMillis());
    }

    public boolean isAtTimeCourseRegistration(String semester) {
        return isTimeBetween(Metadata.MetadataKey.START_REGISTER_COURSE, Metadata.MetadataKey.END_REGISTER_COURSE, semester, System.currentTimeMillis());
    }

    private boolean isTimeBetween(@Nonnull Metadata.MetadataKey metadataKeyStart, @Nonnull Metadata.MetadataKey metadataKeyEnd, String semester, long timeInMillis) {
        String start = getMetadata(metadataKeyStart, semester);
        String end = getMetadata(metadataKeyEnd, semester);
        Instant instantStart = Instant.parse(start);
        Instant instantEnd = Instant.parse(end);
        Instant now = Instant.ofEpochMilli(timeInMillis);
        return instantStart.isBefore(now) && now.isBefore(instantEnd);
    }

    public Metadata updateMetadata(Metadata.MetadataKey key, String semester, String value) {
        if (semester == null) semester = "";
        /**
         * Ktra thứ 2
         */
        if (key == Metadata.MetadataKey.START_WEEK_1) {
            if (!LocalDate.parse(value, DateTimeFormatter.ISO_DATE_TIME).getDayOfWeek().equals(DayOfWeek.MONDAY))
                throw new MessageException("Ngày bắt đầu năm học phải là thứ 2");
        }
        Metadata metadataDB = metadataRepository.findByMetadataPk_MetadataKeyAndMetadataPk_Semester(key, semester);
        Metadata metadata;
        if (metadataDB != null) {
            metadata = metadataDB;
        } else {
            /** create new*/
            metadata = new Metadata();
            metadata.setMetadataPk(new Metadata.MetadataPk(key, semester));
        }
        metadata.setValue(value);
        return metadataRepository.save(metadata);
    }

    public String getCurrentSemester() {
        return getMetadata(Metadata.MetadataKey.CURRENT_SEMESTER, "");
    }
}
