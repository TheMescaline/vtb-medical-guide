package ru.vtb.insurance;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.vtb.insurance.domain.Clinic;
import ru.vtb.insurance.domain.MedicalService;
import ru.vtb.insurance.service.ClinicRepository;

import java.util.Arrays;
import java.util.HashSet;

@Slf4j
@Configuration
public class LoadDatabase {

    @Bean
    @Profile("dbInitialization")
    CommandLineRunner initDataBase(ClinicRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Clinic("ГлавКлиника", new HashSet<MedicalService>(Arrays.asList(MedicalService.STOMATOLOGY, MedicalService.AMBULATORY)), "Москва, Пушкинская", 11.2, 23.8)));
            log.info("Preloading " + repository.save(new Clinic("НОВА-МЕД", new HashSet<MedicalService>(Arrays.asList(MedicalService.STATIONARY)), "Москва, Красная Пресня", 55.8, 37.8)));
            log.info("Preloading " + repository.save(new Clinic("Больничка № 4", new HashSet<MedicalService>(Arrays.asList(MedicalService.AMBULATORY)), "Москва, Тверская", 40.2, 41.4)));
            log.info("Preloading " + repository.save(new Clinic("СМ-Клиника", new HashSet<MedicalService>(Arrays.asList(MedicalService.AMBULATORY)), "Москва, Октябрьская", 36.8, 23.4)));
            log.info("Preloading " + repository.save(new Clinic("VIP-LEPILA", new HashSet<MedicalService>(Arrays.asList(MedicalService.AMBULATORY, MedicalService.STATIONARY, MedicalService.STOMATOLOGY)), "Москва, Наташинская", 90.7, 21.4)));
            log.info("Preloading " + repository.save(new Clinic("S-kulap", new HashSet<MedicalService>(Arrays.asList(MedicalService.AMBULATORY, MedicalService.STATIONARY)), "Люберцы, Жулебинский бульвар", 190.7, 221.4)));
        };
    }
}
