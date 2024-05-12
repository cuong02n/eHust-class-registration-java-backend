package vn.edu.hust.ehustclassregistrationjavabackend.service;

import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Metadata;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.User;
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

    public <T> T getMetadata(String name, java.lang.Class<T> classofT) {
        Metadata metadata = metadataRepository.findById(name).orElse(null);
        if (metadata == null) {
            return null;
        }
        return GsonUtil.gson.fromJson(metadata.getValue(), classofT);
    }

    public String getCurrentSemester() {
        return getMetadata("current_semester", String.class);
    }

    /**
     * @param student       student to register
     * @param registerClass class
     * @return a String:
     * - Empty String corresponding to true: available for this student
     * - Non empty String represent Error message
     */
    public String isClassOpenForStudent(User student, Class registerClass) {
        String semester = registerClass.getClassPK().getSemester();
        if (!satisfyMaximumCredit(student, semester, registerClass)) {
            return "You cannot register for more than your maximum credit";
        }

        if (isFullSlotClass(registerClass)) {
            return "The class is full, wait or contact admin";
        }

        if (!studentMatchedAllCourseBefore(student, registerClass)) {
            return "This course require some pre-course, prequisite-course, or corequisite-course";
        }

        if (isFreeRegister(semester)) {
            return "";
        }

        if (isStudentRegisterCourseBefore(student, registerClass.getCourseId(), semester)) {// đã đăng kí học phần rồi->
            if (student.getStudentType() == User.StudentType.ELITECH) {
                return (isElitechOfficialRegisterClass(semester) || isElitechUnofficialRegisterClass(semester)) ? "" : "Not your registration time";
            }
            if (student.getStudentType() == User.StudentType.STANDARD) {
                return (isStandardOfficialRegisterClass(semester) || isStandardUnofficialRegisterClass(semester)) ? "" : "Not your registration time";
            }
        } else {
            if (student.getStudentType() == User.StudentType.ELITECH) { // chưa đăng kí học phần
                return isElitechUnofficialRegisterClass(semester) ? "" : "Not your registration time";
            }
            if (student.getStudentType() == User.StudentType.STANDARD) {
                return isStandardUnofficialRegisterClass(semester) ? "" : "Not your registration time";
            }
        }

        return "";
    }

    private boolean isFullSlotClass(Class registedClass) {
        return getNumberOfRegistedClass(registedClass) >= registedClass.getMaxStudent();
    }

    private int getNumberOfRegistedClass(Class registedClass) {
        return userClassRepository.countRegistedByClassIdAndSemester(registedClass.getClassPK().getId(), registedClass.getClassPK().getSemester());
    }

    public boolean studentMatchedAllCourseBefore(User student, Class registerClass) {
        return true;
    }

    public boolean satisfyMaximumCredit(User student, String semester, Class registerClass) {
        return getCreditRegisted(student, semester) + registerClass.getCourse().getCredit() <= student.getMaxCredit();
    }

    public int getCreditRegisted(User user, String semester) {
        return userClassRepository.sumCreditByUserIdAndSemester(user.getId(), semester);
    }

    public boolean isStudentRegisterCourseBefore(User student, String courseId, String semester) {
        return userCourseRepository.findByCourseIdAndSemesterAndUserId(courseId, semester, student.getId()).isPresent();
    }

    public boolean isElitechOfficialRegisterClass(String semester) {
        return timeBetweenMetadata("open_official_elitech_" + semester, "close_official_elitech_" + semester, System.currentTimeMillis());
    }

    public boolean isStandardOfficialRegisterClass(String semester) {
        return timeBetweenMetadata("open_official_standard_" + semester, "close_official_standard_" + semester, System.currentTimeMillis());
    }

    public boolean isElitechUnofficialRegisterClass(String semester) {
        return timeBetweenMetadata("open_unofficial_elitech_" + semester, "close_unofficial_elitech_" + semester, System.currentTimeMillis());
    }

    public boolean isStandardUnofficialRegisterClass(String semester) {
        return timeBetweenMetadata("open_unofficial_standard_" + semester, "close_unofficial_standard_" + semester, System.currentTimeMillis());
    }

    public boolean isFreeRegister(String semester) {
        return timeBetweenMetadata("open_free_all_" + semester, "close_free_all_" + semester, System.currentTimeMillis());
    }

    private boolean timeBetweenMetadata(@Nonnull String metadataKeyStart, @Nonnull String metadataKeyEnd, long timeInMillis) {
        Metadata start = metadataRepository.findById(metadataKeyStart).orElse(null);
        if (start == null) return false;
        Metadata end = metadataRepository.findById(metadataKeyEnd).orElse(null);
        if (end == null) return false;

        return Long.parseLong(start.getValue()) <= timeInMillis && timeInMillis <= Long.parseLong(end.getValue());
    }

}
