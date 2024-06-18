package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
import vn.edu.hust.ehustclassregistrationjavabackend.repository.MetadataRepository;

import java.sql.Timestamp;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MetadataService {
    private final MetadataRepository metadataRepository;
    private final HttpServletRequest httpServletRequest;

    /**
     * @param key:
     * @param semester: pass empty String for metadata not depends on semester
     * @return String
     */
    public String getMetadata(Metadata.MetadataKey key, String semester) {
        Metadata metadata = metadataRepository.findByMetadataKeyAndSemester(key, semester).orElseThrow();
        return metadata.getValue();
    }

    public String getMetadata(Metadata.MetadataKey key) {
        return getMetadata(key, "");
    }

    /**
     * @param year require
     * @return Timestamp for response to client only, server doesnot use this
     */
    public Timestamp getDayStartWeek1(int year) {
        return Timestamp.valueOf(getMetadata(Metadata.MetadataKey.START_WEEK_1, year + "1"));
    }

    public String getCurrentSemester() {
        return getMetadata(Metadata.MetadataKey.CURRENT_SEMESTER);
    }

    public boolean isElitechOfficialRegisterClass(String semester) {
        return isTimeBetween(Metadata.MetadataKey.START_REGISTER_CLASS_OFFICIAL_ELITECH, Metadata.MetadataKey.END_REGISTER_CLASS_OFFICIAL_ELITECH, semester, System.currentTimeMillis());
    }

    public boolean isStandardOfficialRegisterClass(String semester) {
        return isTimeBetween(Metadata.MetadataKey.START_REGISTER_CLASS_OFFICIAL_STANDARD, Metadata.MetadataKey.END_REGISTER_CLASS_OFFICIAL_STANDARD, semester, System.currentTimeMillis());
    }

    public boolean isElitechUnofficialRegisterClass(String semester) {
        return isTimeBetween(Metadata.MetadataKey.START_REGISTER_CLASS_UNOFFICIAL_ELITECH, Metadata.MetadataKey.END_REGISTER_CLASS_UNOFFICIAL_ELITECH, semester, System.currentTimeMillis());
    }

    public boolean isStandardUnofficialRegisterClass(String semester) {
        return isTimeBetween(Metadata.MetadataKey.START_REIGSTER_CLASS_UNOFFICIAL_STANDARD, Metadata.MetadataKey.END_REIGSTER_CLASS_UNOFFICIAL_STANDARD, semester, System.currentTimeMillis());
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

        return Long.parseLong(start) <= timeInMillis && timeInMillis <= Long.parseLong(end);
    }

    public Metadata updateMetadata(Metadata.MetadataKey key, String semester, String value) {
        User superAdmin = (User) httpServletRequest.getAttribute("user");
        Optional<Metadata> metadataDB = metadataRepository.findByMetadataKeyAndSemester(key, semester);
        Metadata metadata;
        if (metadataDB.isPresent()) {
            metadata = metadataDB.get();
        } else {
            metadata = new Metadata();
            metadata.setMetadataKey(key);
            metadata.setSemester(semester);
        }
        metadata.setValue(value);
        metadata.setUserModified(superAdmin);
        return metadataRepository.saveAndFlush(metadata);
    }
}
