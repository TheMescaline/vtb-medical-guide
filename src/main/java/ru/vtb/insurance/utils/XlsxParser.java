package ru.vtb.insurance.utils;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.vtb.insurance.domain.Clinic;
import ru.vtb.insurance.domain.EmployeeCategory;
import ru.vtb.insurance.domain.MedicalService;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class XlsxParser {

    public Collection<Clinic> readData(String file) throws IOException {
        List<Clinic> result = new ArrayList<>();
        Map<Long, Clinic> midResult = new HashMap<>();
        XSSFWorkbook data = new XSSFWorkbook(this.getClass().getResourceAsStream(file));
        for (int i = 16; i < 66; i++) {
            XSSFRow row = data.getSheetAt(0).getRow(i);

            long id = (long) row.getCell(1).getNumericCellValue();
            String clinicName = row.getCell(2).getStringCellValue();
            Set<EmployeeCategory> employeeCategories = parseCategory(row);
            Set<MedicalService> medicalServices = parseServices(row);
            String address = row.getCell(3).getStringCellValue();
            String description = row.getCell(4).getStringCellValue();
            Clinic clinic = new Clinic(
                    id,
                    clinicName,
                    employeeCategories,
                    medicalServices,
                    address,
                    description);
            midResult.merge(id, clinic, (oldClinic, newClinic) -> {
                newClinic.getEmployeeCategories().addAll(oldClinic.getEmployeeCategories());
                return newClinic;
            });
        }
        return midResult.values();
    }

    private static Set<MedicalService> parseServices(XSSFRow row) {
        String line = row.getCell(5).getStringCellValue();
        return Arrays.stream(line.split(" ")).map(s -> MedicalService.valueOf(s)).collect(Collectors.toSet());
    }

    private static Set<EmployeeCategory> parseCategory(XSSFRow row) {
        String category = null;
        XSSFCell categoryCell = row.getCell(0);

        if (categoryCell.getCellType() == CellType.STRING) {
            category = categoryCell.getStringCellValue();
        }
        if (categoryCell.getCellType() == CellType.NUMERIC) {
            if (String.valueOf(categoryCell.getNumericCellValue()).equals("2.1")) {
                category = "2.1";
            } else {
                category = String.valueOf((int) categoryCell.getNumericCellValue());
            }
        }

        EmployeeCategory categoryEnum = EmployeeCategory.getEnumByCategory(category.trim());
        return new HashSet<>(Arrays.asList(categoryEnum));
    }
}
