package ru.vtb.insurance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.vtb.insurance.domain.Clinic;
import ru.vtb.insurance.service.ClinicRepository;
import ru.vtb.insurance.utils.XlsxParser;


@Slf4j
@Configuration
public class LoadDatabase {

    @Bean
    @Profile("dbInitialization")
    CommandLineRunner initDataBase(ClinicRepository repository) {
        return args -> {
            for (Clinic clinic : new XlsxParser().readData("/META-INF/data.xlsx")) {
                log.info("Init new clinic " + repository.save(clinic));
            }
        };
    }
}
