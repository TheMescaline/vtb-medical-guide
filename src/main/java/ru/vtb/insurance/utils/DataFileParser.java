package ru.vtb.insurance.utils;

import org.springframework.stereotype.Component;
import ru.vtb.insurance.domain.Clinic;
import ru.vtb.insurance.domain.EmployeeCategory;
import ru.vtb.insurance.domain.MedicalService;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public abstract class DataFileParser {

    public abstract Collection<Clinic> readData(String file) throws IOException;

    public Set<MedicalService> parseServices(String unparsedLine) {
        return Arrays.stream(prepareLine(unparsedLine)).map(MedicalService::valueOf).collect(Collectors.toSet());
    }

    public Set<EmployeeCategory> parseCategory(String unparsedLine) {
        return Arrays.stream(prepareLine(unparsedLine)).map(category -> EmployeeCategory.getEnumByCategory(category.trim())).collect(Collectors.toSet());
    }

    private String[] prepareLine(String line) {
        return line.replace("[", "").replace("]", "").replaceAll(",", "").split(" ");
    }
}
