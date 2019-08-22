package ru.vtb.insurance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.vtb.insurance.domain.Clinic;
import ru.vtb.insurance.domain.EmployeeCategory;
import ru.vtb.insurance.domain.MedicalService;
import ru.vtb.insurance.service.ClinicRepository;
import ru.vtb.insurance.utils.DataFileParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

@Slf4j
@Configuration
public class LoadDatabase {

    @Autowired
    private DataFileParser parser;

    @Bean
    @Profile("dbInitialization")
    CommandLineRunner initDataBase(ClinicRepository repository) {
        return args -> {
//            for (Clinic clinic : parser.readData("/META-INF/data.xlsx")) {
            for (Clinic clinic : parser.readData("/META-INF/data.csv")) {
                log.info("Init new clinic " + repository.save(clinic));
            }
        };
    }

    @Bean
    @Profile("migrateDataToFile")
    CommandLineRunner migrateData(ClinicRepository repository) throws FileNotFoundException {
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(new File("C:\\Projects\\VTB\\medical-guide\\src\\main\\resources\\META-INF\\data.csv")));
        StringBuilder builder = new StringBuilder();
        return args -> {
            for (Clinic clinic : repository.findAll()) {
                builder.append(clinic.getId())
                        .append(";")
                        .append(prepareToCsv(clinic.getClinicName()))
                        .append(";")
                        .append(prepareToCsv(clinic.getAddress()))
                        .append(";")
                        .append(prepareToCsv(clinic.getDescription()))
                        .append(";");
                for (EmployeeCategory category : clinic.getEmployeeCategories()) {
                    builder.append(category.getCategory()).append(" ");
                }
                builder.setLength(builder.length() - 1);
                builder.append(";");
                for (MedicalService service : clinic.getMedicalServices()) {
                    builder.append(service).append(" ");
                }
                builder.setLength(builder.length() - 1);
                builder.append(";")
                        .append(clinic.getX())
                        .append(";")
                        .append(clinic.getY())
                        .append(";")
                        .append(clinic.getUrl().trim().isEmpty() ? "нет данных" : prepareToCsv(clinic.getUrl()))
                        .append(";")
                        .append(clinic.getPhone().trim().isEmpty() ? "нет данных" : prepareToCsv(clinic.getPhone()))
                        .append("\n");
                writer.write(builder.toString());
                builder.setLength(0);
            }
            writer.close();
        };
    }

    private String prepareToCsv(String rawLine) {
        return rawLine.replaceAll(";", "").replaceAll("\n", "").replaceAll("\r", "");
    }
}
