package ru.vtb.insurance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.vtb.insurance.domain.Clinic;
import ru.vtb.insurance.service.ClinicRepository;

import static ru.vtb.insurance.utils.XlsxParser.readData;

@Slf4j
@Configuration
public class LoadDatabase {

    @Bean
    @Profile("dbInitialization")
    CommandLineRunner initDataBase(ClinicRepository repository) {
        return args -> {
            for (Clinic clinic : readData("C:\\Projects\\VTB\\medical-guide\\src\\main\\resources\\META-INF\\data.xlsx")) {
                log.info("Init ne clinic " + repository.save(clinic));
            }
        };
    }
}
