package vn.edu.hust.ehustclassregistrationjavabackend.utils;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import vn.edu.hust.ehustclassregistrationjavabackend.model.dto.request.ClassDto;
import vn.edu.hust.ehustclassregistrationjavabackend.model.entity.Class;

import java.io.InputStream;
import java.util.*;

public class ExcelUtil {
    static int SEMESTER_POSITION = 0;
    static int SCHOOL_NAME_POSITION = 1;
    static int CLASS_ID_POSITION = 2;
    static int THEORY_CLASS_ID_POSITION = 3;
    static int COURSE_ID_POSITION = 4;
    static int COURSE_NAME_POSITION = 5;
    static int COURSE_NAME_ENGLISH_POSITION = 6;
    static int COURSE_CREDIT_INFO_POSITION = 7;
    static int CLASS_NOTE_POSITION = 8;
    static int DAY_OF_WEEK_POSITION = 10;
    static int TIME_POSITION = 11;
    static int WEEK_POSITION = 15;
    static int PLACE_POSITION = 16;
    static int NEED_EXPERIMENT_POSITION = 17;
    static int CURRENT_REGISTER_COUNT_POSITION = 18;
    static int MAX_REGISTER_POSITION = 19;
    static int STATUS_POSITION = 20;
    static int CLASS_TYPE_POSITION = 21;
    static int SEMESTER_TYPE_POSITION = 22;
    static int COURSE_TYPE_POSITION = 23;

    public static List<ClassDto> getClassDtoRequest(InputStream excelInputStream) {
        Map<String, ClassDto.ClassDtoBuilder> builderMap = new HashMap<>();
        try (Workbook workbook = new XSSFWorkbook(excelInputStream)) {
            Sheet sheet = workbook.getSheetAt(0);
            for (int i = 4; i < sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                String classId = row.getCell(CLASS_ID_POSITION).getStringCellValue();
                String semester = row.getCell(SEMESTER_POSITION).getStringCellValue();
                String semesterType = row.getCell(SEMESTER_TYPE_POSITION).getStringCellValue();
                int maxStudent = Integer.parseInt(row.getCell(MAX_REGISTER_POSITION).getStringCellValue());
                String theoryClassId = row.getCell(THEORY_CLASS_ID_POSITION).getStringCellValue();
                String classTypeString = row.getCell(CLASS_TYPE_POSITION).getStringCellValue();
                String classStatusString = row.getCell(STATUS_POSITION).getStringCellValue();
                String courseId = row.getCell(COURSE_ID_POSITION).getStringCellValue();

                String week = row.getCell(WEEK_POSITION).getStringCellValue();


                Class.ClassType classType = switch (classTypeString) {
                    case "LT" -> Class.ClassType.THEORY;
                    case "LT+BT" -> Class.ClassType.THEORY_EXERCISE;
                    case "BT" -> Class.ClassType.EXERCISE;
                    default -> Class.ClassType.EXPERIMENT;
                };

                Class.Status status = switch (classStatusString) {
                    case "Đang xếp TKB" -> Class.Status.OPEN;
                    case "Huỷ lớp" -> Class.Status.CANCEL;
                    default -> Class.Status.CLOSE;
                };

                ClassDto.ClassDtoBuilder builder = builderMap.get(classId);
                if (builder == null) {
                    builder = ClassDto.builder()
                            .id(classId)
                            .semester(semester)
                            .semesterType(semesterType)
                            .maxStudent(maxStudent)
                            .theoryClassId(theoryClassId)
                            .classType(classType)
                            .status(status)
                            .courseId(courseId)
                            .timetables(new Vector<>());
                }
                try {
                    int dayOfWeek = Integer.parseInt(row.getCell(DAY_OF_WEEK_POSITION).getStringCellValue());
                    String time = row.getCell(TIME_POSITION).getStringCellValue();
                    String from = time.split("-")[0];
                    String to = time.split("-")[1];
                    String place = row.getCell(PLACE_POSITION).getStringCellValue();
                    Class.Timetable timetable = new Class.Timetable(week, from, to, place, dayOfWeek);
                    builder.timetable(timetable);
                } catch (Exception e) {
                    System.out.println("This class "+classId+" doesnot have timetable");
                }
                builderMap.put(classId, builder);
            }


            Set<String> ids = new HashSet<>();
            for(int i = 4;i<sheet.getLastRowNum();i++){
                ids.add(sheet.getRow(i).getCell(2).getStringCellValue());
            }
            System.out.println("number of ids: "+ids.size());

            return builderMap.values().stream().map(ClassDto.ClassDtoBuilder::build).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
