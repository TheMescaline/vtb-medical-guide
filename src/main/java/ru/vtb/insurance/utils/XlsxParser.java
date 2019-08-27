package ru.vtb.insurance.utils;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import ru.vtb.insurance.domain.Clinic;
import ru.vtb.insurance.domain.EmployeeCategory;
import ru.vtb.insurance.domain.MedicalService;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Component
@Profile("xlsx")
public class XlsxParser extends DataFileParser {

    @Override
    public Collection<Clinic> readData(String file) throws IOException {
        Map<Long, Clinic> result = new HashMap<>();
        XSSFWorkbook data = new XSSFWorkbook(this.getClass().getResourceAsStream(file));
        for (int i = 16; i < 2924; i++) {
            XSSFRow row = data.getSheetAt(0).getRow(i);

            long id = (long) row.getCell(1).getNumericCellValue();
            String clinicName = row.getCell(2).getStringCellValue();

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

            Set<EmployeeCategory> employeeCategories = parseCategory(category);
            Set<MedicalService> medicalServices = parseServices(row.getCell(5).getStringCellValue());
            String address = row.getCell(3).getStringCellValue();
            String description = row.getCell(4).getStringCellValue();
            Clinic clinic = new Clinic(
                    id,
                    medicalServices,
                    employeeCategories,
                    clinicName,
                    address,
                    description);
            result.merge(id, clinic, (oldClinic, newClinic) -> {
                newClinic.getEmployeeCategories().addAll(oldClinic.getEmployeeCategories());
                return newClinic;
            });
        }
        return result.values();
    }

}
