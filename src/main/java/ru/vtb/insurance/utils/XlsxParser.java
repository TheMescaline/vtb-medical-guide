package ru.vtb.insurance.utils;

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.vtb.insurance.domain.Clinic;
import ru.vtb.insurance.domain.EmployeeCategory;
import ru.vtb.insurance.domain.MedicalService;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


public class XlsxParser {

    public static Collection<Clinic> readData(String file) throws IOException {
        List<Clinic> result = new ArrayList<>();
        Map<Long, Clinic> midResult = new HashMap<>();
        XSSFWorkbook data = new XSSFWorkbook(new FileInputStream(file));
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
                return newClinic; });
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

        if(categoryCell.getCellType() == CellType.STRING){
            category = categoryCell.getStringCellValue();
        }
        if(categoryCell.getCellType() == CellType.NUMERIC){
            if (String.valueOf(categoryCell.getNumericCellValue()).equals("2.1")) {
                category = "2.1";
            } else {
                category = String.valueOf((int) categoryCell.getNumericCellValue());
            }
        }

        switch (category.trim()) {
            case "1":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.FIRST));
            case "1бс":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.FIRST_NO_STOM));
            case "2":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.SECOND));
            case "2.1":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.SECOND_ONE));
            case "2бс":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.SECOND_NO_STOM));
            case "3":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.THIRD));
            case "3бс":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.THIRD_NO_STOM));
            case "4":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.FOURTH));
            case "4бс":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.FOURTH_NO_STOM));
            case "5":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.FIFTH));
            case "5бс":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.FIFTH_NO_STOM));
            case "6":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.SIXTH));
            case "6бс":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.SIXTH_NO_STOM));
            case "7":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.SEVENTH));
            case "7бс":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.SEVENTH_NO_STOM));
            case "8":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.EIGHTH));
            case "8бс":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.EIGHTH_NO_STOM));
            case "9":return new HashSet<EmployeeCategory>(Arrays.asList(EmployeeCategory.NINTH));
            default: throw new IllegalStateException("Something wrong while parsing with row " + row.getRowNum());
        }
    }
}
